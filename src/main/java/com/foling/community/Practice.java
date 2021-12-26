package com.foling.community;

import com.foling.community.dto.AccessTokenDTO;
import com.foling.community.provider.GithubProvider;
import com.foling.community.provider.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class Practice {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        return "index";
    }
}
