package com.bissdom.dotframework.api;

import com.bissdom.dotframework.common.TestBase;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

public class APITestBase extends TestBase {
    public APITestBase() {
    }

    public boolean checkStatusCode(Response res, int statusCode) {
        return res.getStatusCode() == statusCode;
    }

    public Headers getHeaders(Response res) {
        return res.getHeaders();
    }

    protected Response executeGet(String url) {
        return (Response)((Response)RestAssured.given().get(url, new Object[0])).andReturn();
    }

    protected Response executePost(String url, Map<String, String> params) {
        return (Response)((Response)RestAssured.given().formParams(params).post(url, new Object[0])).andReturn();
    }

    protected String getPart(String response, String jsonQuery) {
        try {
            return JsonPath.from(response).getString(jsonQuery);
        } catch (Exception var4) {
            return null;
        }
    }

    protected String getPartAtPath(Response response, String jsonPath, String searchTerm) {
        return JsonPath.from(response.asString()).getString(jsonPath);
    }

    public void cleanup() {
    }
}
