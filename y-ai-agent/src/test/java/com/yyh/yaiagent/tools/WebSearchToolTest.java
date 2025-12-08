package com.yyh.yaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String apiKey;

    @Test
    void test_searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        String answer = webSearchTool.searchWeb("疯狂动物城");
        Assertions.assertNotNull(answer);
    }
}