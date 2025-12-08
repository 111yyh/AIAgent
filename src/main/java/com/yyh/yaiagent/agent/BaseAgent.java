package com.yyh.yaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.yyh.yaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象基础代理，用于管理代理状态喝执行流程
 */
@Data
@Slf4j
public abstract class BaseAgent {

    private String name;

    private String systemPrompt;
    private String nextStepPrompt;

    private AgentState state = AgentState.IDLE;

    private int currentStep = 0;
    private int maxSteps = 10;

    private ChatClient chatClient;

    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行 Agent
     * @param userPrompt
     * @return
     */
    public String run(String userPrompt) {
        //基础校验
        if (state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty userPrompt");
        }
        // 更改状态
        state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step {}/{} ", stepNumber, maxSteps);
                // 单步执行
                String stepRes = step();
                String result = "Step " + stepNumber + ":" + stepRes;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "Error executing agent";
        } finally {
            cleanup();
        }
    };

    public abstract String step();

    protected void cleanup() {};
}
