package com.github.sundaymore.magicexpr.executors;

import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author chaofan
 */
public class TimeExecutor implements CommandExecutor{

    private List<String> AVAILABLE_TIME_FORMATS = Lists.newArrayList(
            "yyyyMMdd",
            "yyyyMMddHHmmss",
            "yyyy-MM-dd"
    );

    @Override
    public Result exec(Command command) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(command.getParams().get(0));
        String formatResult = formatter.format(LocalDateTime.now());
        return Result.success(formatResult);
    }

    @Override
    public void validate(Command command) {
        if(command == null){
            throw new IllegalArgumentException("command null");
        }
        if(command.getParams() == null){
            throw new IllegalArgumentException("param null");
        }
        if(command.getParams().size() < 1){
            throw new IllegalArgumentException("param empty");
        }
        if(!AVAILABLE_TIME_FORMATS.contains(command.getParams().get(0))){
            throw new IllegalArgumentException("time format error");
        }
    }

    @Override
    public void init(Environment environment) {

    }

}
