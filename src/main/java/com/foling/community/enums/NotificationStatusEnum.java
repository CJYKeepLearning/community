package com.foling.community.enums;

/**
 * @Author foling
 * @Date2021-08-04 9:30
 * @Version 1.0
 * @Other Be happy~
 **/
public enum NotificationStatusEnum {
    NOREAD(0),READ(1)
    ;
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
