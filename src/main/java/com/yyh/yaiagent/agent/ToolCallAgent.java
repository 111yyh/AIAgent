package com.yyh.yaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.yyh.yaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent{

    private final ToolCallback[] availableTools;

    private ChatResponse toolCallBackChatResponse;

    private ToolCallingManager toolCallingManager;

    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用Spring AI 内置的工具调用机制
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true)
                .build();
    }

    @Override
    public boolean think() {
        try {
            // 1.校验提示词，拼接用户提示词
            if (StrUtil.isNotBlank(getNextStepPrompt())) {
                UserMessage userMessage = new UserMessage(getNextStepPrompt());
                getMessageList().add(userMessage);
            }
            // 2.调用AI大模型，获取工具调用结果
            List<Message> messageList = getMessageList();
            Prompt prompt = new Prompt(messageList, chatOptions);
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();
            // 3.解析工具调用结果，获取要调用的工具
            this.toolCallBackChatResponse = chatResponse;
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            String result = assistantMessage.getText();
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            log.info(getName() + " Thinking: " + result);
            log.info(getName() + " Choose: " + toolCalls.size() + " Tools");
            String toolCallInfo = toolCalls.stream()
                            .map(toolCall -> String.format("toolName:%s, param:%s", toolCall.name(), toolCall.arguments()))
                                    .collect(Collectors.joining("\n"));
            log.info(toolCallInfo);
            if (toolCalls.isEmpty()) {
                // 只有不调用工具时，才需要记录助手消息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "Thinking failed", e.getMessage());
            getMessageList().add(new AssistantMessage("Error in handling: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (!toolCallBackChatResponse.hasToolCalls()) {
            return "No tools need to be invoked";
        }
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallBackChatResponse);
        setMessageList(toolExecutionResult.conversationHistory());
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());

        boolean terminateToolCalled = toolResponseMessage.getResponses().stream()
                .anyMatch(toolResponse -> "doTerminate".equals(toolResponse.name()));
        if (terminateToolCalled) {
            setState(AgentState.FINISHED);
        }

        String result = toolResponseMessage.getResponses().stream()
                .map(toolResponse -> toolResponse.name() + "return: " + toolResponse.responseData())
                .collect(Collectors.joining("\n"));
        log.info(result);
        return result;
    }
}
