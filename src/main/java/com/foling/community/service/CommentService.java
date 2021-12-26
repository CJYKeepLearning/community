package com.foling.community.service;

import com.foling.community.dto.CommentDTO;
import com.foling.community.enums.CommentTypeEnum;
import com.foling.community.enums.NotificationStatusEnum;
import com.foling.community.enums.NotificationTypeEnum;
import com.foling.community.exception.CustomizeErrorCode;
import com.foling.community.exception.CustomizeException;
import com.foling.community.mapper.*;
import com.foling.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author foling
 * @Date2021-07-25 19:29
 * @Version 1.0
 * @Other Be happy~
 **/
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;


    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId()==0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            /*创建通知*/
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, comment.getParentId());

        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            /*创建通知*/
            createNotify(comment,question.getCreator(), question.getTitle(),commentator.getName(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String outerTitle, String notifierName, NotificationTypeEnum notificationTypeEnum, Long outerId) {
        if (receiver == comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setOuterid(outerId);
        notification.setStatus(NotificationStatusEnum.NOREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        //获取评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users =  userMapper.selectByExample(userExample);
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user->user));

        //转换comment为CommentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
