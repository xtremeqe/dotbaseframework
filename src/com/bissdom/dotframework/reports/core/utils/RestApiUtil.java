package com.bissdom.dotframework.reports.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class RestApiUtil {
    public RestApiUtil() {
    }

    public static Response triggerRestCall(String requestType, String url, String postBody, String auth) {
        Response response = null;

        try {
            RestAssured.proxy("ipv4.65.210.5.50.webdefence.global.blackspider.com", 8081);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode element;
            String var7;
            switch ((var7 = requestType.toUpperCase()).hashCode()) {
                case 70454:
                    if (var7.equals("GET")) {
                        response = (Response) ((ValidatableResponse) ((Response) RestAssured.given().contentType("application/json").get(url, new Object[0])).then()).extract().response();
                        return response;
                    }
                    break;
                case 79599:
                    if (var7.equals("PUT")) {
                        element = mapper.readTree(postBody);
                        response = (Response) ((ValidatableResponse) ((Response) RestAssured.given().header("Content-Type", "application/json", new Object[0]).body(element).when().put(url, new Object[0])).then()).extract().response();
                        return response;
                    }
                    break;
                case 2461856:
                    if (var7.equals("POST")) {
                        element = mapper.readTree(postBody);
                        response = (Response) ((ValidatableResponse) ((Response) RestAssured.given().contentType("application/json").header("cache-control", "no-cache", new Object[0]).header("Content-Type", "application/json", new Object[0]).body(element).when().post(url, new Object[0])).then()).extract().response();
                        return response;
                    }
                    break;
                case 75900968:
                    if (var7.equals("PATCH")) {
                        element = mapper.readTree(postBody);
                        response = (Response) ((ValidatableResponse) ((Response) RestAssured.given().contentType("application/json").header("cache-control", "no-cache", new Object[0]).header("Authorization", auth, new Object[0]).body(element).when().patch(url, new Object[0])).then()).extract().response();
                        return response;
                    }
            }

            System.out.println("Input request type: " + requestType + "dont match with GET/PUT/POST");
            return response;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static void triggerVSTSUpdate(String postBody) throws IOException {
        createPythonScript(postBody);
        Process p = Runtime.getRuntime().exec("python updateVSTS.py");
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String s = null;

        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

    }

    public static void createPythonScript(String postBody) {
        BufferedWriter bufferedWriter = null;

        try {
            File file = new File("updateVSTS.py");
            if (!file.exists()) {
                file.createNewFile();
            }

            Writer writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(getRestCallScript(postBody));
            bufferedWriter.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static String getRestCallScript(String postBody) {
        postBody = postBody.replace("\"", "\\\"");
        postBody = "\"" + postBody + "\"";
        String script = "import http.client\r\n\r\nconn = http.client.HTTPSConnection(\"bissdom.visualstudio.com\")\r\n\r\npayload = " + postBody + "\r\n" + "headers = {\r\n" + "    'authorization': \"Basic Oms0b2o0YWY2cDRudGJ0cTMza2N3NmVib3p0d2lrdWRweG5hdzVhYmJlYmNrbjRmMnltaHE=\",\r\n" + "    'content-type': \"application/json\",\r\n" + "    'cache-control': \"no-cache\"\r\n" + "    }\r\n" + "\r\n" + "conn.request(\"PATCH\", \"/_apis/wit/$batch?api-version=4.1\", payload, headers)\r\n" + "\r\n" + "res = conn.getresponse()\r\n" + "data = res.read()";
        return script;
    }
}
