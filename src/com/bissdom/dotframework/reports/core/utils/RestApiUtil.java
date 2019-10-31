package com.bissdom.dotframework.reports.core.utils;

import com.bissdom.dotframework.reports.core.model.FeatureResult;
import com.bissdom.dotframework.reports.core.model.RunResults;
import com.bissdom.dotframework.reports.core.model.ScenarioResult;
import com.bissdom.dotframework.reports.core.util.RestApiUtil;
import com.jayway.restassured.response.Response;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TestCaseUpdate {
    public static final String batchUpdateAPI = "https://inovalondev.visualstudio.com/_apis/wit/$batch?api-version=4.1";
    public static final String batchUpdateURI = "/_apis/wit/workitems/{testcaseID}?api-version=4.1";
    public static final String contentTypeHeader = "application/json-patch+json";
    public static final String batchOperation = "add";
    public static final String batchTestCaseStatusPath = "/fields/ScrumR1.TestResult";
    public static final String batchUpdateRequestType = "PATCH";
    public static final String testcaseFailedState = "Fail";
    public static final String testcasePassedState = "Pass";

    public TestCaseUpdate() {
    }

    public static void batchTestUpdate(RunResults runResults, String vstsAuth) throws IOException {
        JSONArray postBodyObject = new JSONArray();
        Iterator var4 = runResults.getFeatureResults().iterator();

        while(var4.hasNext()) {
            FeatureResult featureResult = (FeatureResult)var4.next();
            Iterator var6 = featureResult.getScenarioResults().iterator();

            while(var6.hasNext()) {
                ScenarioResult scResult = (ScenarioResult)var6.next();
                String[] var10;
                int var9 = (var10 = scResult.getTestcaseList()).length;

                for(int var8 = 0; var8 < var9; ++var8) {
                    String testcase = var10[var8];
                    postBodyObject.add(getTestCaseObject(testcase, scResult.getIsSuccess()));
                }
            }
        }

        String postBodyString = postBodyObject.toString().replaceAll("\\\\", "");
        Response response = RestApiUtil.triggerRestCall("PATCH", "https://inovalondev.visualstudio.com/_apis/wit/$batch?api-version=4.1", postBodyString, vstsAuth);
        System.out.println("VSTS Testupdate status : " + response.getStatusCode());
    }

    public static JSONObject getTestCaseObject(String testcaseID, boolean status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "PATCH");
        String apiUri = new String("/_apis/wit/workitems/{testcaseID}?api-version=4.1");
        apiUri = apiUri.replace("{testcaseID}", testcaseID);
        jsonObject.put("uri", apiUri);
        JSONObject headerObject = new JSONObject();
        headerObject.put("Content-Type", "application/json-patch+json");
        jsonObject.put("headers", headerObject);
        JSONArray bodyObject = new JSONArray();
        JSONObject testObject = new JSONObject();
        testObject.put("op", "add");
        testObject.put("path", "/fields/ScrumR1.TestResult");
        testObject.put("value", status ? "Pass" : "Fail");
        bodyObject.add(testObject);
        jsonObject.put("body", bodyObject);
        return jsonObject;
    }
}
