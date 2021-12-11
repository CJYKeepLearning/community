package com.foling.community.service;

import com.foling.community.dto.NotificationDTO;
import com.foling.community.dto.PaginationDTO;
import com.foling.community.enums.NotificationStatusEnum;
import com.foling.community.enums.NotificationTypeEnum;
import com.foling.community.exception.CustomizeErrorCode;
import com.foling.community.exception.CustomizeException;
import com.foling.community.mapper.NotificationMapper;
import com.foling.community.mapper.UserMapper;
import com.foling.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author foling
 * @Date2021-08-04 9:59
 * @Version 1.0
 * @Other Be happy~
 **/
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;
    public PaginationDTO listByUserId(Long userId, Integer page, Integer size) {

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        Integer totalPage= (totalCount + size -1 ) / size;

        if (page < 1)
            page = 1;
        if(page > totalPage)
            page = totalPage;

        Integer offset = size*(page-1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        //显示时倒序排列
        example.setOrderByClause("gmt_create desc");
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        paginationDTO.setPagination(totalCount,page,size);
        if (notifications.size() == 0 ){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification:notifications
             ) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);

        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.NOREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user){
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}
