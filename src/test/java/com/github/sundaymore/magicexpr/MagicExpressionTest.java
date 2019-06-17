package com.github.sundaymore.magicexpr;

import com.github.sundaymore.magicexpr.expr.MagicExpression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MagicExpressionTest {
    /**
     * register global context
     */
    @Test
    public void testRegisterGlobalContext(){
        MagicExpression magicExpression = MagicExpression.builder().registerGlobalContextInfo("email", "wuchaofanaa@126.com").build();
        System.out.println(magicExpression.execute("{env email global}"));
    }

    @Test
    public void testPutLocalContext(){
        MagicExpression magicExpression = MagicExpression.builder().build();
        Map<String, String> localContext = new HashMap<>();
        localContext.put("name", "wuchaofan");
        System.out.println(magicExpression.execute("{env name local}", localContext));
    }

    @Test
    public void testCustomRegisterExecutor(){
        MagicExpression magicExpression = MagicExpression.builder().registerExecutor("hello", HelloCommandExecutor.class).build();
        System.out.println(magicExpression.execute("{hello sunday}"));
    }

    @Test
    public void testTime(){
        MagicExpression magicExpression = MagicExpression.builder().build();
        System.out.println(magicExpression.execute("{time yyyyMMdd}"));
    }

    @Test
    public void testMultiCommand(){
        MagicExpression magicExpression = MagicExpression.builder().registerGlobalContextInfo("number", "987").build();

        System.out.println(magicExpression.execute("{const SLT{env number global}}{const -}{time yyyyMMdd}"));
    }
}
