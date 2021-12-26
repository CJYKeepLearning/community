package com.foling.community.controller;

import com.foling.community.dto.NotificationDTO;
import com.foling.community.enums.NotificationTypeEnum;
import com.foling.community.model.User;
import com.foling.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author foling
 * @Date2021-08-04 20:01
 * @Version 1.0
 * @Other Be happy~
 **/
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id")Long id,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO =  notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getName() == notificationDTO.getTypeName()
        || NotificationTypeEnum.REPLY_QUESTION.getName() == notificationDTO.getTypeName()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }
        return "profile";
    }
}
