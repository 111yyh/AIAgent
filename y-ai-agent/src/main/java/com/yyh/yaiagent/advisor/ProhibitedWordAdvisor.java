package com.yyh.yaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProhibitedWordAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final String DEFAULT_BANNED_DIR = "/static/banned.txt";

    private final List<String> prohibitedWords;

    public ProhibitedWordAdvisor() {
        prohibitedWords = getProhibitedWords(DEFAULT_BANNED_DIR);
        log.info("初始化违禁词校验器,违禁词数量：{}", prohibitedWords.size());
    }

    public ProhibitedWordAdvisor(String path) {
        prohibitedWords = getProhibitedWords(path);
        log.info("初始化违禁词校验器,违禁词数量：{}", prohibitedWords.size());
    }

    private List<String> getProhibitedWords(String filePath) {
        List<String> tempList = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            br.lines()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .forEach(tempList::add);

        } catch (IOException e) {
            log.error("出现异常",e);
            return new ArrayList<>();
        }
        return tempList;
    }

    private AdvisedRequest checkRequest(AdvisedRequest advisedRequest) {
        String userMessage = advisedRequest.userText();
        if (containProhibitedWord(userMessage)) {
            throw new ProhibitedWordException("包含违禁词");
        }
        return advisedRequest;
    }

    public static class ProhibitedWordException extends RuntimeException {
        public ProhibitedWordException(String message) {
            super(message);
        }
    }

    private boolean containProhibitedWord(String userMsg) {
        if (userMsg == null) return false;
        for (String word : prohibitedWords) {
            if (userMsg.toLowerCase().contains(word.toLowerCase()))
            {
                log.warn("违禁词:{}", word);
                return true;
            }
        }
        return false;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(checkRequest(advisedRequest));
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(checkRequest(advisedRequest));
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
