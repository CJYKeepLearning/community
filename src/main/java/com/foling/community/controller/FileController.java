package com.foling.community.controller;

import com.foling.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author foling
 * @Date2021-08-04 23:37
 * @Version 1.0
 * @Other Be happy~
 **/
@Controller
public class FileController {
    @RequestMapping("/file/upload")
    public FileDTO upload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/logo.png");
        return fileDTO;
    }
}
