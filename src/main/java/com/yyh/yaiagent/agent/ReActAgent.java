package com.yyh.yaiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ReAct （Reasoning and Acting） 模式的代理抽象类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent{

    /**
     * 处理当前状态并决定下一步行动
     * @return 是否需要执行行动
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     * @return 执行结果
     */
    public abstract String act();

    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                return "Thinking complete - no action needed";
            }
            return act();
        } catch (Exception e) {
            log.error("Error in step(): {}", e.getMessage());
            return "Error in step() " + e.getMessage();
        }
    }


}
