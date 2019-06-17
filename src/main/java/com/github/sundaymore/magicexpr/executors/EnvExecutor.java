package com.github.sundaymore.magicexpr.executors;


import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;
import org.apache.commons.lang3.Validate;

/**
 * {env local wh}
 * {env global wh}
 * @author chaofan
 */
public class EnvExecutor implements CommandExecutor {

    private Environment environment;

    private static final int PARAM_NAME_INDEX = 0;
    private static final int CONTEXT_TYPE_INDEX = 1;

    @Override
    public Result exec(Command command) {
        String paramName = command.getFirstParam();
        String contextType = command.getSecondParam() != null ? command.getSecondParam() : "local";
        Result result = null;
        Object value = null;
        if(ContextType.GLOBAL.code.equals(contextType)){
            value = environment.getGlobalContextParam(paramName);
        }else if(ContextType.THREAD_LOCAL.code.equals(contextType)){
            value = environment.getLocalContextParam(paramName);
        }
        if(value == null){
            result = Result.paramError();
        }else{
            result = Result.success(String.valueOf(value));
        }
        return result;
    }

    @Override
    public void validate(Command command) {
        Validate.notNull(command.getFirstParam(),"param error");
        String contextType = command.getSecondParam();
        if(contextType != null) {
            Validate.isTrue(ContextType.THREAD_LOCAL.code.equals(contextType) ||
                    ContextType.GLOBAL.code.equals(contextType), "context type error");
        }
    }

    @Override
    public void init(Environment environment) {
        this.environment = environment;
    }

    enum ContextType {
        //
        THREAD_LOCAL("local", "thread local context"),
        GLOBAL("global", "global context")
        ;
        ContextType(String code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public String code;
        public String desc;
    }
}
