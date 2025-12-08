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
        String msg = "我是一名程序员，婚后出现了问题，怎么办";
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

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
//        testMessage("周末想带女朋友去上海约会，推荐一个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
//        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘约会露营计划’PDF，包含餐厅预订、活动流程和礼物清单");

        // 示例：调用工具发送七夕祝福邮件
//        testMessage("发送邮件给 桑甜雨 的QQ邮箱（2539950155@qq.com），主题是爱你哟");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我的另一半在苏州工业园区，请你找到周边5公里内合适的约会地点";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp_ImageSearch() {
        // 测试图片搜索 MCP
        String chatId = UUID.randomUUID().toString();
        String message = "帮我搜索一下有关爱情的图片";
        String answer =  loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }

}