package com.bissdom.dotframework.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.bissdom.dotframework.util.FrameworkProperties;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bson.Document;

public final class MongoRepository extends DataRepository {
    private MongoClient dbClient;
    private MongoDatabase db;

    public MongoRepository() {
        this.setUp();
    }

    public Item getItem(String criteriaString) {
        MongoCollection<Document> colls = this.db.getCollection("test_data");
        Document criteriaDoc = (new Search(criteriaString)).getCriteria();
        Document doc = this.getParamDoc(colls, criteriaDoc);
        Item item = new Item();
        if (null != doc) {
            item.setJson((Document)doc.get("data"));
        }

        return item;
    }

    public Item getItem(String project, String feature, String scenario, String environment, String tag, String tenantId) {
        MongoCollection<Document> colls = this.db.getCollection("test_data");
        Document criteriaDoc = (new Search(project, feature, scenario, environment, tag, tenantId)).getCriteria();
        Document doc = this.getParamDoc(colls, criteriaDoc);
        if (null == doc) {
            criteriaDoc = (new Search(project, feature, "any", environment, tag, tenantId)).getCriteria();
            doc = this.getParamDoc(colls, criteriaDoc);
        }

        if (null == doc) {
            criteriaDoc = (new Search(project, feature, "any", environment, tag)).getCriteria();
            doc = this.getParamDoc(colls, criteriaDoc);
        }

        Item item = new Item();
        if (null != doc) {
            item.setJson((Document)doc.get("data"));
        }

        return item;
    }

    public Item getItem(Map<String, String> tags) {
        Item item = null;
        Search criteria = new Search(tags);
        if (criteria.isValid()) {
            MongoCollection<Document> colls = this.db.getCollection("test_data");
            Document criteriaDoc = criteria.getCriteria();
            Document doc = this.getParamDoc(colls, criteriaDoc);
            if (null != doc) {
                item = new Item();
                item.setJson((Document)doc.get("data"));
            }
        }

        return item;
    }

    public Item getRandomItem() {
        MongoCollection<Document> colls = this.db.getCollection("test_data");
        Document doc = (Document)colls.aggregate(Arrays.asList(Aggregates.sample(1))).first();
        Item item = new Item();
        if (null != doc) {
            item.setJson((Document)doc.get("data"));
        }

        return item;
    }

    public void setUp() {
        if (FrameworkProperties.useMongoDB()) {
            MongoClientURI uri = new MongoClientURI("mongodb://rjonnalagadda:Sehwag9666@bwvmddtauatdb3.medassurant.local:27017/?authMechanism=PLAIN&ssl=true&sslAllowInvalidCertificates=true&authSource=$external");
            this.dbClient = new MongoClient(uri);
            this.db = this.dbClient.getDatabase("automation");
        } else {
            Fongo fongo = new Fongo("Mongo Test Data DB");
            this.db = fongo.getDatabase("automation");
            MongoCollection<Document> colls = this.db.getCollection("test_data");
            String dataFilePath = FrameworkProperties.getProperty("testdata");
            Document[] docs = null;
            File file = null;
            ObjectMapper mapper = new ObjectMapper();
            if (!dataFilePath.isEmpty()) {
                file = new File(dataFilePath);
                if (file.isDirectory()) {
                    List allDataFiles = null;

                    try {
                        allDataFiles = (List)Files.walk(Paths.get(dataFilePath)).filter((x$0) -> {
                            return Files.isRegularFile(x$0, new LinkOption[0]);
                        }).collect(Collectors.toList());
                    } catch (IOException var13) {
                        throw new RuntimeException("Unable to read files from: " + dataFilePath, var13);
                    }

                    Iterator var8 = allDataFiles.iterator();

                    while(var8.hasNext()) {
                        Path path = (Path)var8.next();

                        try {
                            File newFile = new File(path.toString());
                            if (newFile.isFile()) {
                                docs = (Document[])mapper.readValue(newFile, Document[].class);
                                colls.insertMany(Arrays.asList(docs));
                            }
                        } catch (IOException var12) {
                            throw new RuntimeException("Could not import file: " + dataFilePath, var12);
                        }
                    }
                } else if (file.isFile()) {
                    try {
                        docs = (Document[])mapper.readValue(file, Document[].class);
                        colls.insertMany(Arrays.asList(docs));
                    } catch (Exception var11) {
                        throw new RuntimeException("Could not import file: " + dataFilePath, var11);
                    }
                }
            }
        }

    }

    public Document getParamDoc(MongoCollection<Document> colls, Document criteriaDoc) {
        return (Document)colls.aggregate(Arrays.asList(Aggregates.match(criteriaDoc), Aggregates.sample(1))).first();
    }
}