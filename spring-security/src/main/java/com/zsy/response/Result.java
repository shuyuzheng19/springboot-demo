package com.zsy.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2022/12/13 14:15
 * @desc
 */
@Data
public class Result {

    private int code;

    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object data;

    private Result(){

    }

    private Result(int code,String message){
        this(code,message,null);
    }

    private Result(int code,String message,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }


    public static Result success(){
        return new Result(200,"成功");
    }

    public static Result fail(){
        return new Result(10001,"失败");
    }

    public static Result error(){
        return new Result(500,"服务器异常");
    }

    public static Result success(Object data){
        return new Result(200,"成功",data);
    }

    public static Result fail(int code,String message){
        return new Result(code,message);
    }



}
