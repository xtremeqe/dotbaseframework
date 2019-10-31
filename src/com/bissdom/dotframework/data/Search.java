package com.bissdom.dotframework.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import java.io.IOException;
import java.util.Map;

public class Search {
    TypeReference<Map<String, Object>> typeRef;
    Document criteria;

    public Search(String project, String feature, String scenario, String environment, String tag) {
        this.typeRef = new NamelessClass_1();
        this.criteria = null;
        this.criteria = (new Document()).append("project", project).append("scenario", scenario).append("environment", environment).append("tag", tag);
    }

    public Search(String project, String feature, String scenario, String environment, String tag, String tenantId) {
        this.typeRef = new NamelessClass_1();
        this.criteria = null;
        this.criteria = (new Document()).append("project", project).append("scenario", scenario).append("environment", environment).append("tag", tag).append("tenantId", tenantId);
    }

    public Search(String jsonCriteria) {
        this.typeRef = new NamelessClass_1();
        this.criteria = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map dataMap = (Map)mapper.readValue(jsonCriteria, this.typeRef);
            this.criteria = (new Document()).append("project", dataMap.get("project")).append("scenario", dataMap.get("scenario")).append("environment", dataMap.get("environment")).append("tag", dataMap.get("tag"));
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public Search(Map<String, String> tags) {
        class NamelessClass_1 extends TypeReference<Map<String, Object>> {
            NamelessClass_1() {
            }
        }

        this.typeRef = new NamelessClass_1();
        this.criteria = null;
        this.criteria = (new Document()).append("project", tags.get("project")).append("scenario", tags.get("scenario")).append("environment", tags.get("environment")).append("tag", tags.get("tag"));
    }

    public Document getCriteria() {
        return this.criteria;
    }

    public boolean isValid() {
        return this.criteria.containsKey("project") && this.criteria.containsKey("feature") && this.criteria.containsKey("scenario") && this.criteria.containsKey("environment") && this.criteria.containsKey("tag");
    }
}
