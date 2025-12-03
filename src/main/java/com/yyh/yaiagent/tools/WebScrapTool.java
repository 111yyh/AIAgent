package com.yyh.yaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WebScrapTool {

    @Tool(description = "Scrape the content of web page")
    public String scrapWebPage(@ToolParam(description = "Url of the web page to scrape") String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0")
                    .get();
            return doc.text();
        } catch (Exception e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
