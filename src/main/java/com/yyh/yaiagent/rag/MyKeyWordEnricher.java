package com.yyh.yaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyKeyWordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

     public List<Document> enrichDocuments(List<Document> documents) {
         KeywordMetadataEnricher keyWordEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
         return keyWordEnricher.apply(documents);
     }
}
