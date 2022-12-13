package com.zsy.config;

import com.zsy.response.Result;
import com.zsy.utils.HttpUtils;
import com.zsy.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 郑书宇
 * @create 2022/12/13 16:05
 * @desc
 */
public class JwtFilter extends BasicAuthenticationFilter {

    private final String TOKEN_PREFIX="Bearer ";

    private final UserDetailsService userDetailsService;

    public JwtFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isEmpty(tokenHeader) || !tokenHeader.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        String token=tokenHeader.substring(TOKEN_PREFIX.length());

        String username = JwtUtils.verifyTokenAndGetUsername(token);

        if(username==null){
            HttpUtils.writeJsonToResponse(Result.fail(403,"Token可能已失效,请重新登录!"),response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToke=new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword() );

        Authentication authenticate = super.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToke);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        chain.doFilter(request,response);

    }
}
