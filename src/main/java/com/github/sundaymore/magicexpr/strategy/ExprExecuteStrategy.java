package com.github.sundaymore.magicexpr.strategy;


import com.github.sundaymore.magicexpr.Environment;

/**
 * @author chaofan
 */
public interface ExprExecuteStrategy {
    String exec(String expr, Environment environment);
}
