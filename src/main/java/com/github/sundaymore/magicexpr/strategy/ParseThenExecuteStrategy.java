package com.github.sundaymore.magicexpr.strategy;


import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.executors.CommandExecutor;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.CommandNode;
import com.github.sundaymore.magicexpr.expr.CommandTreeParser;
import com.github.sundaymore.magicexpr.expr.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chaofan
 */
public class ParseThenExecuteStrategy implements ExprExecuteStrategy{

    @Override
    public String exec(String expr, Environment environment) {
        CommandNode rootNode = CommandTreeParser.parseExpr(expr);
        return recursiveExec(rootNode, environment);
    }

    private String recursiveExec(CommandNode node, Environment environment){
        if(!node.isCommand()){
            return "";
        }
        String cmdName = node.getValue();
        CommandExecutor executor = environment.getExecutor(cmdName);
        List<String> params = new ArrayList<>();
        List<CommandNode> children = node.getChildren();
        if(CollectionUtils.isNotEmpty(children)){
            StringBuilder tmp = new StringBuilder();
            boolean joinFlag = false;
            for(CommandNode child : children){
                if(child.isCommand()){
                    if(!joinFlag){
                        String str = tmp.toString();
                        if(StringUtils.isNotBlank(str)) {
                            params.add(tmp.toString());
                        }
                        tmp = new StringBuilder();
                    }
                    tmp.append(recursiveExec(child, environment));
                }else if(child.isParam()){
                    if(!joinFlag){
                        String str = tmp.toString();
                        if(StringUtils.isNotBlank(str)) {
                            params.add(tmp.toString());
                        }
                        tmp = new StringBuilder();
                    }
                    tmp.append(child.getValue());
                }else if(child.isJoin()){
                    tmp.append(child.getValue());
                    joinFlag = true;
                }
            }
            String str = tmp.toString();
            if(StringUtils.isNotBlank(str)){
                params.add(str);
            }
        }
        Command command = makeCommand(cmdName, params);
        executor.validate(command);
        Result result = executor.exec(command);
        if(!result.isSuccess()){
            throw new RuntimeException("run expr error!" + "cmd is :" + command.getCmd() +
                    "params is:" + command.getParams() + " error msg:" + result.getErrorMsg());
        }
        return result.getOutput();
    }

    public Command makeCommand(String cmd, List<String> params){
        Command command = new Command();
        command.setCmd(cmd);
        command.setParams(params);
        return command;
    }

}
