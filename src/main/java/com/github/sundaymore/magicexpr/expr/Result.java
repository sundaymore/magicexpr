package com.github.sundaymore.magicexpr.expr;

/**
 * @author chaofan
 */
public class Result {

    public static int SUCCESS = 200;
    public static int PARAM_ERROR = 400;
    public static int SYSTEM_ERROR = 500;

    /**
     * 缓存失败结果
     */
    private static final Result systemErrorResult;
    private static final Result paramErrorResult;
    private static final Result emptyReturnSuccessResult;
    static {
        systemErrorResult = new Result();
        systemErrorResult.code = SYSTEM_ERROR;
        paramErrorResult = new Result();
        paramErrorResult.code = PARAM_ERROR;
        emptyReturnSuccessResult = new Result();
        emptyReturnSuccessResult.code = SUCCESS;
    }
    /**
     * 200-成功
     * 400-参数错误
     * 500-系统异常
     */
    private int code;

    private String output;

    private String errorMsg;

    public boolean isSuccess(){
        return code == SUCCESS;
    }

    public static Result success(String output){
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setOutput(output);
        return result;
    }

    public void setCode(int code){
        this.code = code;
    }

    public void setOutput(String output){
        this.output = output;
    }

    public String getOutput(){
        return output;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static Result success(){
        return emptyReturnSuccessResult;
    }

    public static Result paramError(){
        return paramErrorResult;
    }

    public static Result systemError(){
        return systemErrorResult;
    }

    public static Result systemError(String msg){
        Result result = new Result();
        result.errorMsg = msg;
        result.setCode(SYSTEM_ERROR);
        return result;
    }
}
