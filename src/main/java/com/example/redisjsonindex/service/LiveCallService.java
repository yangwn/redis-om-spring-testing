//package com.example.redisjsonindex.service;
//
//import com.example.redisjsonindex.model.LiveCall;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.UnifiedJedis;
//import redis.clients.jedis.json.Path;
//import redis.clients.jedis.search.*;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
///**
// * @author Bruce.Yang
// * @date 2023/2/7 -14:24
// */
//@Service
//@RequiredArgsConstructor
//public class LiveCallService {
//
//    private final UnifiedJedis unifiedJedis;
//
//    private final ObjectMapper objectMapper;
//
//    //@PostConstruct
//    public void run() {
//        index();
//        add();
//        search().forEach(System.out::println);
//        System.out.println("--------");
//
////        update();
////        search().forEach(System.out::println);
//    }
//
//    public void save() {
//        LiveCall liveCall = new LiveCall("1", "cmsId1", "callId1", "confereceId1", 1);
//        unifiedJedis.jsonSet("cmsId-1", Path.of("."), liveCall);
//    }
//
//    public void flush() {
//        unifiedJedis.jsonDel("cmsId-1");
//    }
//
//    public void removePath() {
//        unifiedJedis.jsonDel("cmsId-1", Path.of("callId"));
//    }
//
//    public void upgrade() {
//        unifiedJedis.jsonSet("cmsId-1", Path.of("cmsId"), "cmsId2");
//    }
//
//    public LiveCall getLiveCall() {
//        return unifiedJedis.jsonGet("cmsId-1", LiveCall.class, Path.of("cmsId"));
//    }
//
//    public void index() {
//        Schema sc = new Schema().addTagField("$.cmsId").as("cmsId").addTagField("$.callId").as("callId");
//        IndexDefinition def = new IndexDefinition(IndexDefinition.Type.JSON).setPrefixes(new String[]{"cmma:call:"});
//        try {
//            unifiedJedis.ftDropIndex("cmma-call-index");
//        } catch (Exception ex) {
//        }
//        unifiedJedis.ftCreate("cmma-call-index", IndexOptions.defaultOptions().setDefinition(def), sc);
//    }
//
//    public void add() {
//        for (int i = 0; i < 10; i++) {
//            LiveCall liveCall = new LiveCall("id" + i, "cmsId" + i, "callId" + i, "conferenceId" + i, i);
//            unifiedJedis.jsonSet("cmma:call:" + i, Path.ROOT_PATH, liveCall);
//        }
//    }
//
//    public void update() {
//        unifiedJedis.jsonSet("cmma:call:1", Path.of("cmsId"), "cmsId3");
//    }
//
//    public List<LiveCall> search() {
//        String queryKey = String.format("@callId:{%s} @cmsId:{%s}", "callId0", "cmsId0");
//        Query query = new Query(queryKey);
//        SearchResult searchResult = unifiedJedis.ftSearch("cmma-call-index", query);
//        return searchResult.getDocuments().stream().map(doc -> {
//            System.out.println("doc:" + doc);
//            LiveCall liveCall = StreamSupport.stream(doc.getProperties().spliterator(), false).map(it -> {
//                try {
//                    return objectMapper.readValue((String) it.getValue(), LiveCall.class);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }).findFirst().orElse(new LiveCall());
//            return liveCall;
//        }).collect(Collectors.toList());
//    }
//
//}
