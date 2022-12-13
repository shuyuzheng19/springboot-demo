package com.zsy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2022/12/13 1:03
 * @desc
 */
public class JwtUtils {

    private static final Algorithm DEFAULT_ALGORITHM=Algorithm.HMAC256("wafkasjfaosifjasionf");

    //token失效时间 单位:小时
    private static final long EXPIRE_TIME=2;


    public static String generateAccessToken(UserDetails userDetails){
        return JWT.create()
                .withClaim("username",userDetails.getUsername())
                .withClaim("role",userDetails.getAuthorities().stream().map(role->role.getAuthority()).collect(Collectors.toList()))
                .withKeyId(UUID.randomUUID().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*60*60)*EXPIRE_TIME))
                .withSubject(userDetails.getUsername())
                .withJWTId(UUID.randomUUID().toString())
                .sign(DEFAULT_ALGORITHM);
    }

    public static String verifyTokenAndGetUsername(String token){
        JWTVerifier verifier = JWT.require(DEFAULT_ALGORITHM).build();
        try{
            DecodedJWT verify = verifier.verify(token);
            String username = verify.getSubject();
            return username;
        }catch (Exception e){
            return null;
        }
    }
}
