package com.swzl.common;


import lombok.Data;

@Data
public class Result {


    private int code;
    private String msg;
    private Long total;
    private Object data;

    public static Result fail(String 记录不存在){
        return result(400,"请求失败",0L,null);
    }

    public static Result fails(String errorMsg) {
        if (errorMsg == null || "默认值".equals(errorMsg)) {
            return result(400, "请求失败", 0L, null);
        }
        return result(400, errorMsg, 0L, null);
    }

    public static Result suc(){
        return result(200,"请求成功",0L,null);
    }
    public static Result suc(Object data){
        return result(200,"请求成功",0L,data);
    }
    public static Result suc(Object data,Long total){
        return result(200,"请求成功",total,data);
    }

    private static Result result(int code,String msg,Long total,Object data){
        Result res = new Result();
        res.setData(data);
        res.setMsg(msg);
        res.setCode(code);
        res.setTotal(total);
        return res;
    }
}
