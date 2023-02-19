package com.suancaiyu.resultcommon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private boolean success;
    private String message;
    private Object data;
    public static Result success(Object data){
        return new Result(200,true,"success",data);
    }
    public static Result fail(Integer code,String mes){
        return new Result(code,false,mes,null);
    }
}
