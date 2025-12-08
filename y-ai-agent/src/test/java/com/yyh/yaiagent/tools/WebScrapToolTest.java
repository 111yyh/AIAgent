package com.yyh.yaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapToolTest {

    @Test
    void test_scrapWebPage() {
        WebScrapTool webScrapTool = new WebScrapTool();
        String res = webScrapTool.scrapWebPage("https://www.codefather.cn");
        Assertions.assertNotNull(res);
    }
}