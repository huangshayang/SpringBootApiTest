package com.apitest.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apitest.entity.Apis;
import com.apitest.entity.Cases;
import com.apitest.entity.Logs;
import com.apitest.mapper.LogMapper;
import com.apitest.rest.RestRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author huangshayang
 */
@Component
public class RestCompoent {

    private static int envId;
    private static RestCompoent restCompoent;

    @Resource
    private LogMapper logMapper;

    @PostConstruct
    public void init() {
        restCompoent = this;
        restCompoent.logMapper = this.logMapper;
    }

    /**
     * 向外部发送http请求
     */
    public static void taskApiCaseExecByLock(Apis apis, Cases cases) {
        long requestTime = System.currentTimeMillis() / 1000;
        ClientResponse response = restHttp(apis, cases).exchange().block();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", response.statusCode().value());
        jsonObject.put("header", String.valueOf(response.headers().asHttpHeaders()));
        jsonObject.put("body", response.bodyToMono(String.class).block());
        responseWriteToLog(jsonObject, requestTime, cases);
    }

    /**
     * 响应数据检查
     */
    private static JSONObject checkResponse(String body, Cases cases) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> mapResponse = JSONObject.parseObject(body);
            String actMes = String.valueOf(mapResponse.get("message"));
            int actCode = (int) mapResponse.get("status");
            Map expResult = JSON.parseObject(cases.getExpectResult(), Map.class);
            if (!expResult.get("message").toString().equals(actMes)) {
                jsonObject.put("checkBoolean", 0);
                jsonObject.put("erroMsg", "错误的message, actMes is: " + actMes + ", but expMes is: " + expResult.get("message") + ". Please check the Response or your Case");
                return jsonObject;
            }
            if ((int)expResult.get("status") != actCode) {
                jsonObject.put("checkBoolean", 0);
                jsonObject.put("errorMsg", "错误的status, actCode is: " + actCode + ", but expCode is: " + expResult.get("status") + ". Please check the Response or your Case");
                return jsonObject;
            }
            jsonObject.put("checkBoolean", 1);
            jsonObject.put("errorMsg", "用例通过");
        }catch (Exception e) {
            jsonObject.put("checkBoolean", -1);
            jsonObject.put("errorMsg", e.toString());
        }
        return jsonObject;
    }

    /**
     * 响应结果存入Log表里
     */
    private static void responseWriteToLog(JSONObject jsonObject, long requestTime, Cases cases) {
        JSONObject jsonObject1 = checkResponse(jsonObject.getString("body"), cases);
        Logs logs = new Logs();
        logs.setRequestTime(requestTime);
        logs.setCheckBoolean(jsonObject1.getIntValue("checkBoolean"));
        logs.setErrorMsg(jsonObject1.getString("errorMsg"));
        logs.setCode(jsonObject.getIntValue("code"));
        logs.setResponseHeader(jsonObject.getString("header"));
        logs.setResponseBody(jsonObject.getString("body"));
        logs.setApiId(cases.getApiId());
        logs.setCaseName(cases.getName());
        restCompoent.logMapper.save(logs);
    }

    /**
     * 根据api的请求方法调用RestRequest类的对应请求方法
     * @param api
     * @param cases
     * @return
     */
    private static WebClient.RequestHeadersSpec<?> restHttp(Apis api, Cases cases) {
        WebClient.RequestHeadersSpec<?> body = null;
        String method = api.getMethod();
        String url = api.getUrl();
        String jsonData = cases.getJsonData();
        String paramsData = cases.getParamsData();
        envId = api.getEnvId();
        boolean cookie = api.getCookie();
        switch (method) {
            case "get":
                body = RestRequest.doGet(url, jsonData, paramsData, cookie);
                break;
            case "post":
                body = RestRequest.doPost(url, jsonData, paramsData, cookie);
                break;
            case "put":
                body = RestRequest.doPut(url, jsonData, paramsData);
                break;
            case "delete":
                body = RestRequest.doDelete(url, paramsData);
                break;
            default:
        }
        return body;
    }

    public static int getEnvId() {
        return envId;
    }
}
