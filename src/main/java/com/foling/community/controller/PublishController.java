package com.foling.community.controller;

import com.foling.community.dto.QuestionDTO;
import com.foling.community.mapper.QuestionMapper;
import com.foling.community.mapper.UserMapper;
import com.foling.community.model.Question;
import com.foling.community.model.User;
import com.foling.community.model.UserExample;
import com.foling.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam(value = "id",required = false)Long id,
            HttpServletRequest request,
            Model model
    ){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null || title == ""){
            model.addAttribute("msg","title不能为空");
            return "publish";
        }
        if (description == null || description == ""){
            model.addAttribute("msg","description不能为空");
            return "publish";
        }
        if (tag == null || tag == ""){
            model.addAttribute("msg","tag不能为空");
            return "publish";
        }

        User user=null;
        //发帖前判断用户登陆状态
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String tokenValue = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(tokenValue);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size()!=0){
                        request.getSession().setAttribute("user",users.get(0));
                        user = users.get(0);
                    }
                    break;
                }
            }
        }
        if(user == null){
            model.addAttribute("msg","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setDescription(description);
        question.setTitle(title);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }
}
