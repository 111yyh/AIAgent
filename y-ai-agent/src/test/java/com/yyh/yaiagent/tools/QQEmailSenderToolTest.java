package com.yyh.yaiagent.tools;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;

class QQEmailSenderToolTest {

    @Test
    void test_sendTextEmail() {
        QQEmailSenderTool qqEmailSenderTool = new QQEmailSenderTool();
        try {
            qqEmailSenderTool.sendTextEmail(
                "l1145986635@outlook.com",
                    "Test",
                    "1111111111"
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}