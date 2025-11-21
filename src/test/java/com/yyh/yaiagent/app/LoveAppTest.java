package com.yyh.yaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void test_doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String firstContent =  loveApp.doChat("你好，我是葛煜昊", chatId);
        Assertions.assertNotNull(firstContent);
        // 第二轮
        String secondContent = loveApp.doChat("我要见家长了", chatId);
        Assertions.assertNotNull(secondContent);
        // 第三轮
        String thirdContent = loveApp.doChat("我叫什么名字?我最近有什么事? 刚和你说过，帮我回忆一下", chatId);
        Assertions.assertNotNull(thirdContent);
    }

    @Test
    void test_doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String msg = "你好，我是葛煜昊，我要见家长了，不知道该怎么办，妈逼";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(msg, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void test_doChatWithMap() {
        String chatId = UUID.randomUUID().toString();
        String msg = "你好，我是葛煜昊，我要见家长了，不知道该怎么办";
        Map<String, Object> map = loveApp.doChatWithMap(msg, chatId);
        Assertions.assertNotNull(map);
    }

    @Test
    void test_doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String msg = "我是一名程序员，想谈对象，你帮我相亲介绍一个前端程序员";
        String content = loveApp.doChatWithRag(msg, chatId);
        Assertions.assertNotNull(content);
    }

    @Test
    void doChatWithCloudRag() {
        String chatId = UUID.randomUUID().toString();
        String msg = "我是一名程序员，想谈对象，你帮我相亲介绍一个";
        String content = loveApp.doChatWithCloudRag(msg, chatId);
        Assertions.assertNotNull(content);
    }
}