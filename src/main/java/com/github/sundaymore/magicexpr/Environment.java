package com.github.sundaymore.magicexpr;

import com.github.sundaymore.magicexpr.executors.CommandExecutor;
import com.github.sundaymore.magicexpr.executors.ExecutorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chaofan
 */
public class Environment {
    private ExecutorFactory executorFactory;
    private ThreadLocal<Map<String, String>> localContextThreadLocal = new ThreadLocal<>();
    private Map<String, String> globalContext = new HashMap<>();

    public CommandExecutor getExecutor(String cmdName){
        return executorFactory.getExecutor(cmdName);
    }

    public void setExecutorFactory(ExecutorFactory executorFactory){
        this.executorFactory = executorFactory;
        this.executorFactory.bindEnvironment(this);
    }

    public void registerExecutor(String cmdName, Class<? extends CommandExecutor> clazz){
        executorFactory.getExecutorRegistry().register(cmdName, clazz);
    }

    public void registerExecutor(Map<String, Class<? extends CommandExecutor>> map){
        executorFactory.getExecutorRegistry().register(map);
    }

    public void registerGlobalContextParam(Map<String, String> globalParams){
        if(globalParams != null) {
            globalContext.putAll(globalParams);
        }
    }

    public void registerGlobalContextParam(String paramName, String value){
        if(paramName != null && value != null){
            globalContext.put(paramName, value);
        }
    }

    public void registerLocalContextParam(Map<String, String> localParams){
        if(localParams == null){ return;}
        if(localContextThreadLocal.get() == null){
            localContextThreadLocal.set(new HashMap<>());
        }
        localContextThreadLocal.get().putAll(localParams);
    }

    public void registerLocalContextParam(String paramName, String value){
        if(paramName == null || value == null){return;}
        if(localContextThreadLocal.get() == null){
            localContextThreadLocal.set(new HashMap<>());
        }
        localContextThreadLocal.get().put(paramName, value);
    }

    public String getLocalContextParam(String key){
        Map<String, String> localContext = localContextThreadLocal.get();
        if(localContext != null){
            return localContext.get(key);
        }
        return null;
    }

    public String getGlobalContextParam(String key){
        return globalContext.get(key);
    }

    public void clearLocalContext(){
        localContextThreadLocal.remove();
    }
}
