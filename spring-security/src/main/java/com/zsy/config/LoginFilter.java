package com.zsy.config;

import com.zsy.response.Result;
import com.zsy.utils.HttpUtils;
import com.zsy.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 郑书宇
 * @create 2022/12/13 15:33
 * @desc
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String method=request.getMethod();

        if(!method.equals("POST")) return null;

        String username=request.getParameter("username");

        String password=request.getParameter("password");

        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            HttpUtils.writeJsonToResponse(Result.fail(45484,"账号或者密码为空!"),response);
            return null;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,password);

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("登陆成功");
        String accessToken= JwtUtils.generateAccessToken((UserDetails) authResult.getPrincipal());
        HttpUtils.writeJsonToResponse(Result.success(accessToken),response);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("登录失败");
        log.error("异常信息",failed);

        if(failed instanceof BadCredentialsException) {
            HttpUtils.writeJsonToResponse(Result.fail(10001, "账号或密码错误!"), response);
        }

    }
}
