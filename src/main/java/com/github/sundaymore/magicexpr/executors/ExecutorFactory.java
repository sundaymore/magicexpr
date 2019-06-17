package com.github.sundaymore.magicexpr.executors;

import com.github.sundaymore.magicexpr.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chaofan
 */
public class ExecutorFactory {

    private ExecutorRegistry executorRegistry;
    private Environment environment;
    private Map<String, CommandExecutor> cache = new HashMap<>();

    public ExecutorFactory(ExecutorRegistry executorRegistry){
        this.executorRegistry = executorRegistry;
    }

    public CommandExecutor getExecutor(String cmdName){
        if(cache.containsKey(cmdName)){
            return cache.get(cmdName);
        }
        Class<? extends CommandExecutor> clazz = executorRegistry.getExecutorClass(cmdName);
        CommandExecutor executor = null;
        try {
            executor = clazz.newInstance();
            executor.init(environment);
        }catch (Exception e){
            if(executor != null) {
                executor = null;
            }
        }
        if(executor != null){
            cache.put(cmdName, executor);
        }
        return executor;
    }

    public ExecutorRegistry getExecutorRegistry(){
        return executorRegistry;
    }

    public void bindEnvironment(Environment environment){
        this.environment = environment;
    }

}
