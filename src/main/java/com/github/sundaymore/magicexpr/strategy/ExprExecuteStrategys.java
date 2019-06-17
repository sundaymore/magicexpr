package com.github.sundaymore.magicexpr.strategy;

/**
 * @author chaofan
 */
public class ExprExecuteStrategys {
    private static ExprExecuteStrategy DEFAULT_STRATEGY = new ParseThenExecuteStrategy();

    public static final ExprExecuteStrategy getDefaultStrategy(){
        return DEFAULT_STRATEGY;
    }
}
