package com.foling.community.controller;

import com.foling.community.dto.PaginationDTO;
import com.foling.community.dto.QuestionDTO;
import com.foling.community.mapper.QuestionMapper;
import com.foling.community.mapper.UserMapper;
import com.foling.community.model.Question;
import com.foling.community.model.User;
import com.foling.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "search",required = false)String search) {

        User user =(User) request.getSession().getAttribute("user");

        PaginationDTO paginationDTO = questionService.list(search,page,size);
        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
