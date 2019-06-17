package com.github.sundaymore.magicexpr.executors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chaofan
 */
public class ExecutorRegistry {

    /**
     * 命令-命令执行类class映射
     */
    private Map<String, Class<? extends CommandExecutor>> cmdMap = new HashMap<>();
    {
        registerDefault();
    }

    /**
     * 单例
     */
    public Class<? extends CommandExecutor> getExecutorClass(String cmd){
        if(!cmdMap.containsKey(cmd)){
            throw new IllegalArgumentException("不存在的命令:"+cmd);
        }
        Class<? extends CommandExecutor> clazz = cmdMap.get(cmd);
        return clazz;
    }

    public void register(String cmdName, Class<? extends CommandExecutor> clazz){
        cmdMap.put(cmdName, clazz);
    }

    public void register(Map<String,Class<? extends CommandExecutor>> map){
        cmdMap.putAll(map);
    }

    public void registerDefault(){
        register("const", ConstExecutor.class);
        register("time", TimeExecutor.class);
        register("fix", FixLengthExecutor.class);
        register("env", EnvExecutor.class);
        register("rand_n", RandomDigitExecutor.class);
    }
}
