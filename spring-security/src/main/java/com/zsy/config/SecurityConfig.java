package com.zsy.config;

import com.zsy.service.UserDetailsServiceImpl;
import com.zsy.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2022/12/13 15:27
 * @desc
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests(http->{
                    try {
                        http.antMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                })
                .addFilter(loginFilter())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint((request,response,exception)->{
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    Map<String,Object> map=new HashMap<>();
                    map.put("code",401);
                    map.put("error_url",request.getRequestURL());
                    map.put("message","检测到您还未登录,请登陆后重试");
                    HttpUtils.writeJsonToResponse(map,response);
                })
                .and()
                .exceptionHandling().accessDeniedHandler((request,response,exception)->{
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String,Object> map=new HashMap<>();
                    map.put("code",403);
                    map.put("error_url",request.getRequestURL());
                    map.put("message","访问被服务器拒绝,可能因为的您的权限不足,请联系管理员");
                    HttpUtils.writeJsonToResponse(map,response);
                })
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter=new LoginFilter();
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setPostOnly(true);
        loginFilter.setAuthenticationManager(authenticationManager());
        return loginFilter;
    }

    @Bean
    public JwtFilter jwtFilter() throws Exception {
        return new JwtFilter(authenticationManager(), userDetailsService());
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
