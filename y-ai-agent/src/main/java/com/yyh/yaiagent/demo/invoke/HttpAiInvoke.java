package com.yyh.yaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;

public class HttpAiInvoke {
    public static void main(String[] args) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 从环境变量读取，与 cURL 的 $DASHSCOPE_API_KEY 一致
        String apiKey = TestApiKey.API_KEY;

        // 组装 JSON 请求体
        JSONObject payload = new JSONObject();
        payload.set("model", "qwen-plus");

        JSONArray messages = new JSONArray();
        messages.add(JSONUtil.createObj()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));
        messages.add(JSONUtil.createObj()
                .set("role", "user")
                .set("content", "你是谁？"));

        JSONObject input = new JSONObject();
        input.set("messages", messages);
        payload.set("input", input);

        JSONObject parameters = new JSONObject();
        parameters.set("result_format", "message");
        payload.set("parameters", parameters);

        // 发起请求
        HttpResponse resp = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(payload))
                .execute();

        // 输出响应
        System.out.println(resp.getStatus());
        System.out.println(resp.body());
    }
}
