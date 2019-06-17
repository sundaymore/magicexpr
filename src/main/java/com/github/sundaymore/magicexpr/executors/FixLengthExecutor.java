package com.github.sundaymore.magicexpr.executors;

import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * 固定编码长度, 输入字符串大于指定长度则不处理, 小于指定长度则前缀位置填充指定字符
 * command 参数： 1.字符串 2.长度  3.填充字符
 * 接受
 * @author chaofan
 */
public class FixLengthExecutor implements CommandExecutor {

    @Override
    public Result exec(Command command) {
        Integer specificLen = Integer.valueOf(command.getSecondParam());
        String fillString = "0";
        if(command.getThirdParam() != null){
            fillString = command.getThirdParam();
        }
        String str = generateFixedLenStr(command.getFirstParam(), specificLen, fillString);
        return Result.success(str);
    }

    @Override
    public void validate(Command command) {
        if(command == null){
            throw new IllegalArgumentException("command null");
        }

        if(command.getSecondParam() == null){
            throw new IllegalArgumentException("param error");
        }

        if(!NumberUtils.isDigits(command.getSecondParam())){
            throw new IllegalArgumentException("param2 error, must be digit");
        }

    }

    @Override
    public void init(Environment environment) {

    }

    private static String generateFixedLenStr(String str, int length, String fillStr){
        if(str.length() < length){
            int lengthOfFill = length - str.length();
            StringBuilder fillContent = new StringBuilder();
            for(;fillContent.length() < lengthOfFill;){
                fillContent.append(fillStr);
            }
            String fillContentStr = fillContent.toString();
            if(fillContentStr.length() > lengthOfFill){
                fillContentStr = fillContentStr.substring(fillContentStr.length() - lengthOfFill, fillContentStr.length());
            }
            return fillContentStr + str;
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println(generateFixedLenStr("st1",10, "abc"));
    }
}
