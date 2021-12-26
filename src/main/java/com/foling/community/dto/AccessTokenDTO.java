package com.foling.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String state;
    private String redirect_uri;
}
