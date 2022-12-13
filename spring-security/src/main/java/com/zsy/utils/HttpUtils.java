package com.zsy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 郑书宇
 * @create 2022/12/13 14:14
 * @desc
 */
public class HttpUtils {

    public static void writeJsonToResponse(Object data,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            new ObjectMapper().writeValue(response.getOutputStream(),data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
