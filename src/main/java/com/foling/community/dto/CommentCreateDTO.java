package com.foling.community.dto;

import lombok.Data;

/**
 * @Author foling
 * @Date2021-07-25 17:43
 * @Version 1.0
 * @Other Be happy~
 **/
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
