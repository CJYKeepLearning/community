package com.foling.community.controller;

import com.foling.community.dto.CommentDTO;
import com.foling.community.dto.QuestionDTO;
import com.foling.community.enums.CommentTypeEnum;
import com.foling.community.mapper.QuestionExtMapper;
import com.foling.community.service.CommentService;
import com.foling.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @GetMapping("/question/{id}")
    public String questions(@PathVariable("id")Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
         List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
         //累加阅读数
        questionService.incView(id);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
