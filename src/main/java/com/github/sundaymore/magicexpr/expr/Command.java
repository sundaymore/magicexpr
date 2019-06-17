package com.github.sundaymore.magicexpr.expr;

import java.util.List;

/**
 * @author chaofan
 */
public class Command {
    private String cmd;
    private List<String> params;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getFirstParam(){
        return indexOfParam(0);
    }

    public String getSecondParam(){
        return indexOfParam(1);
    }

    public String getThirdParam(){
        return indexOfParam(2);
    }

    public String indexOfParam(int index){
        if(params == null){
            return null;
        }
        if(params.size() < index + 1){
            return null;
        }
        return params.get(index);
    }

}
