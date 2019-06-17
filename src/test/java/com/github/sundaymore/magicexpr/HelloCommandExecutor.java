package com.github.sundaymore.magicexpr;

import com.github.sundaymore.magicexpr.executors.CommandExecutor;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;
import org.apache.commons.lang3.Validate;

public class HelloCommandExecutor implements CommandExecutor {
    @Override
    public Result exec(Command command) {
        return Result.success("Hello:"+command.getFirstParam());
    }

    @Override
    public void validate(Command command) {
        Validate.notNull(command.getFirstParam(), "param error");
    }

    @Override
    public void init(Environment environment) {

    }
}
