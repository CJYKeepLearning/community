package com.foling.community.enums;

/**
 * @Author foling
 * @Date2021-08-04 9:18
 * @Version 1.0
 * @Other Be happy~
 **/
public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论"),
    ;
    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(Integer type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type)
                return notificationTypeEnum.getName();
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
