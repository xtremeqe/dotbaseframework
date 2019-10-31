package com.bissdom.dotframework.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.util.StringBuilders;

public class JSONMessage {
    public JSONMessage() {
    }

    public static String asJsonLog(Map<String, Object> context, LogResult result, String comments, String[] ids, Exception ex) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        Iterator var7 = context.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, Object> entry = (Entry)var7.next();
            ((ObjectNode)rootNode).put((String)entry.getKey(), String.valueOf(entry.getValue()));
        }

        if (null != result) {
            ((ObjectNode)rootNode).put("result", result.toString());
        }

        if (null != comments) {
            ((ObjectNode)rootNode).put("message", comments);
        }

        if (null != ex) {
            ((ObjectNode)rootNode).put("exception", ex.getMessage());
        }

        if (null != ids) {
            ((ObjectNode)rootNode).put("testcase_ids", String.join(",", ids));
        }

        try {
            return mapper.writeValueAsString(rootNode);
        } catch (Exception var9) {
            var9.printStackTrace();
            return var9.getMessage();
        }
    }

    private static String replaceDQuotes(String value) {
        return value.replace("\"", "");
    }

    public static String asJsonLog(Map<String, Object> map, LogResult result, String message) {
        return asJsonLog(map, result, message, (String[])null, (Exception)null);
    }

    public static String asJson(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append('{');
        Iterator var3 = data.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, Object> entry = (Entry)var3.next();
            if (!first) {
                sb.append(", ");
            }

            first = false;
            StringBuilders.appendDqValue(sb, entry.getKey()).append(':');
            StringBuilders.appendDqValue(sb, String.valueOf(entry.getValue()));
        }

        sb.append('}');
        return sb.toString();
    }
}
