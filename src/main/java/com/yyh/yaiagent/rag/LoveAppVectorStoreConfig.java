package com.yyh.yaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置(基于内存的向量数据库 Bean)
 */
//@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyKeyWordEnricher myKeyWordEnricher;

//    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = loveAppDocumentLoader.loadDocuments();
        // 自动补充关键字元信息
        List<Document> enrichDocuments = myKeyWordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichDocuments);
        return simpleVectorStore;
    }
}
