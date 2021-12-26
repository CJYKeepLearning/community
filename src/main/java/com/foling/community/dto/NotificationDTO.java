package com.foling.community.dto;

import com.foling.community.model.User;
import lombok.Data;

/**
 * @Author foling
 * @Date2021-08-04 9:55
 * @Version 1.0
 * @Other Be happy~
 **/
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String  notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
}
