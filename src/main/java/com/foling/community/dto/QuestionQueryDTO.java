package com.foling.community.dto;

import lombok.Data;

/**
 * @Author foling
 * @Date2021-08-05 11:16
 * @Version 1.0
 * @Other Be happy~
 **/
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
