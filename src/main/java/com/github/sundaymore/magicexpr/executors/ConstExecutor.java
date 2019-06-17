package com.github.sundaymore.magicexpr.executors;


import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;

/**
 * @author chaofan
 */
public class ConstExecutor implements CommandExecutor{

    @Override
    public Result exec(Command command) {
        return Result.success(command.getParams().get(0));
    }

    @Override
    public void validate(Command command) {
        if(command == null){
            throw new IllegalArgumentException("command null");
        }
        if(command.getParams() == null){
            throw new IllegalArgumentException("param null");
        }
        if(command.getParams().size() <= 0){
            throw new IllegalArgumentException("param empty");
        }
    }

    @Override
    public void init(Environment environment) {

    }

}
