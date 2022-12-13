package com.zsy.dao;

import com.zsy.dto.MyUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2022/12/13 0:57
 * @desc
 */
@Repository
public class UserDao {

    private static final List<UserDetails> USER_DETAILS= Arrays.asList(
      new MyUserDetails("2528959216","123456789","ROLE_ADMIN"),
      new MyUserDetails("2264378768","123456789","ROLE_ADMIN")
    );

    public UserDetails findByUsername(String username){
        return USER_DETAILS.stream()
                .filter(user->user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(()->new UsernameNotFoundException("找不到该账号!"));
    }




}
