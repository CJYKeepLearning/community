package com.foling.community.controller;

import com.foling.community.dto.AccessTokenDTO;
import com.foling.community.mapper.UserMapper;
import com.foling.community.model.User;
import com.foling.community.provider.GiteeProvider;
import com.foling.community.provider.dto.GiteeUser;
import com.foling.community.service.UserService;
import com.foling.community.strategy.LoginUserInfo;
import com.foling.community.strategy.UserStrategy;
import com.foling.community.strategy.UserStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@PropertySource({"classpath:application.properties"})
public class AuthorizeController {

    @Autowired
    private GiteeProvider giteeProvider;

    @Autowired
    private UserStrategyFactory userStrategyFactory;

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    @Value("${gitee.client.id}")
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback/{type}")
    public String newCallback(@PathVariable(name = "type") String type,
                              @RequestParam(name = "code") String code,
                              @RequestParam(name = "state", required = false) String state,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        /*
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);

        if (giteeUser != null && giteeUser.getId()!=null){
            User user = new User();
            //token代替session
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(giteeUser.getName());
            user.setAccountId(String.valueOf(giteeUser.getId()));
            user.setAvatarUrl(giteeUser.getAvatarUrl());
            //user存放到数据库中
            userService.createOrUpdate(user);

            //记录登陆状态
            //通过response写入cookie
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("giteeUser",giteeUser);
            return "redirect:/";
        }else {
            //登陆失败，重新登陆
            return "redirect:/";
        }
        */
        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);
        if (loginUserInfo != null && loginUserInfo.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(loginUserInfo.getName());
            user.setAccountId(String.valueOf(loginUserInfo.getId()));
            user.setAvatarUrl(loginUserInfo.getAvatarUrl());
            user.setType(type);
            /*
            UFileResult fileResult = null;
            try {
                fileResult = uFileService.upload(loginUserInfo.getAvatarUrl());
                user.setAvatarUrl(fileResult.getFileUrl());
            } catch (Exception e) {
                user.setAvatarUrl(loginUserInfo.getAvatarUrl());
            }
             */
            userService.createOrUpdate(user);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            /*
            log.error("callback get github error,{}", loginUserInfo);
             */
            // 登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
