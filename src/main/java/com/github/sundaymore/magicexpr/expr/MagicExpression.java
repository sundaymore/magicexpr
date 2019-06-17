package com.github.sundaymore.magicexpr.expr;

import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.executors.CommandExecutor;
import com.github.sundaymore.magicexpr.executors.ExecutorFactory;
import com.github.sundaymore.magicexpr.executors.ExecutorRegistry;
import com.github.sundaymore.magicexpr.strategy.ExprExecuteStrategy;
import com.github.sundaymore.magicexpr.strategy.ExprExecuteStrategys;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chaofan
 */
public class MagicExpression {

    private ExprExecuteStrategy strategy;
    private Environment environment;

    public String execute(String expr){
        return execute(expr, null);
    }

    public String execute(String expr, Map<String, String> localContextParams){
        expr = wrapAsConstCmd(expr);
        if(localContextParams != null) {
            registerLocalContextParams(localContextParams);
        }
        String result = strategy.exec(expr, environment);
        clearLocalContextParams();
        return result;
    }

    private void registerLocalContextParams(Map<String, String> localContextParams){
        environment.registerLocalContextParam(localContextParams);
    }

    private void clearLocalContextParams(){
        environment.clearLocalContext();
    }

    private static String wrapAsConstCmd(String expr){
        return String.format("{const %s}",expr);
    }

    private MagicExpression(){

    }

    public static MagicExpressionBuilder builder(){
        return new MagicExpressionBuilder();
    }

    public static class MagicExpressionBuilder{
        private ExprExecuteStrategy strategy = ExprExecuteStrategys.getDefaultStrategy() ;
        private Map<String, Class<? extends CommandExecutor>> registerMap = new HashMap<>();
        /**
         * 全局上下文
         */
        private Map<String, String> globalContextInfo = new HashMap<>();

        public MagicExpressionBuilder setStrategyAsDefault(){
            this.strategy = ExprExecuteStrategys.getDefaultStrategy();
            return this;
        }
        public MagicExpressionBuilder setStrategy(ExprExecuteStrategy strategy){
            this.strategy = strategy;
            return this;
        }
        public MagicExpressionBuilder registerExecutor(String cmdName, Class<? extends CommandExecutor> clazz){
            this.registerMap.put(cmdName, clazz);
            return this;
        }

        public MagicExpressionBuilder registerGlobalContextInfos(Map<String, String> globalContextInfo){
            if(globalContextInfo != null) {
                this.globalContextInfo.putAll(globalContextInfo);
            }
            return this;
        }

        public MagicExpressionBuilder registerGlobalContextInfo(String paramName, String value){
            if(paramName != null && value != null){
                this.globalContextInfo.put(paramName, value);
            }
            return this;
        }

        public MagicExpression build(){
            MagicExpression magicExpression = new MagicExpression();
            if(this.strategy == null){
                throw new IllegalArgumentException("strategy can not be null");
            }
            Environment environment = new Environment();
            environment.setExecutorFactory(new ExecutorFactory(new ExecutorRegistry()));
            if(registerMap != null && registerMap.size() > 0) {
                environment.registerExecutor(registerMap);
            }
            if(globalContextInfo != null && globalContextInfo.size() > 0){
                environment.registerGlobalContextParam(globalContextInfo);
            }
            magicExpression.environment = environment;
            magicExpression.strategy = this.strategy;
            return magicExpression;
        }
    }
}
