package com.github.sundaymore.magicexpr.executors;

import com.github.sundaymore.magicexpr.Environment;
import com.github.sundaymore.magicexpr.expr.Command;
import com.github.sundaymore.magicexpr.expr.Result;

/**
 * @author chaofan
 */
public interface CommandExecutor {
    /**
     * 执行
     * @param command
     * @return
     */
    Result exec(Command command);

    /**
     * 校验
     * @param command
     */
    void validate(Command command);

    /**
     * 初始化
     * @param environment
     */
    void init(Environment environment);
}
