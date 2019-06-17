package com.github.sundaymore.magicexpr.executors;

import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author chaofan
 */
public class RandomDigitExecutor implements CommandExecutor{

    @Override
    public Result exec(Command command) {
        //位数
        Integer count = Integer.valueOf(command.getFirstParam());
        StringBuilder sb = new StringBuilder(count);
        for(int i = 1; i <= count; i++){
            sb.append(randGetSingleDigit());
        }
        return Result.success(sb.toString());
    }

    private int randGetSingleDigit(){
        return RandomUtils.nextInt(0,9);
    }

    @Override
    public void validate(Command command) {
        if(command.getFirstParam() == null){
            throw new IllegalArgumentException("first param is null");
        }
        if(NumberUtils.isDigits(command.getFirstParam())){
            throw new IllegalArgumentException("first param is digit");
        }
    }

    @Override
    public void init(Environment environment) {

    }
}
