package com.zsy.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 郑书宇
 * @create 2022/12/13 16:45
 * @desc
 */
public class MyUserDetails implements UserDetails {

    private String username;

    private String password;

    private Collection<GrantedAuthority> grantedAuthorities;

    public MyUserDetails(String username,String password,String... roles){
        this.username=username;
        this.password=password;
        this.grantedAuthorities= Stream.of(roles).map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
