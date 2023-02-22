package com.example.redisjsonindex.service;

import com.example.redisjsonindex.cache.ISearchIndexerTemplate;
import com.example.redisjsonindex.cache.constant.RedisDType;
import com.example.redisjsonindex.model.LiveCall;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author Bruce.Yang
 * @date 2023/2/9 -11:21
 */
@Component
@RequiredArgsConstructor
public class LiveConfService {

    private final ISearchIndexerTemplate<LiveCall> redisSearchIndexTemplate;

    private final String INDEX_NAME = "cmma-call-index";
    private final String INDEX_NAME_PERFIX = "cmma:call:";
    private final Map<String, RedisDType> columnWithTypeMap = Map.of("conferenceId", RedisDType.Tag);

    public void index() {
        redisSearchIndexTemplate.drop(INDEX_NAME);
        redisSearchIndexTemplate.create(INDEX_NAME, INDEX_NAME_PERFIX, columnWithTypeMap);
    }

    public void add() {
        for (int i = 0; i <= 1000; i++) {
            LiveCall liveCall1 = new LiveCall("id" + i, "cmsId" + i, "callId" + i, "conferenceId" + i, i, i -1000);
            redisSearchIndexTemplate.save(INDEX_NAME_PERFIX + i, liveCall1);
        }
    }

    public void update() {
        redisSearchIndexTemplate.update(INDEX_NAME_PERFIX + 1, "$.cmsId", "cmsId3");
    }

    public void search() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.asc("cmsId")));
        Map<String, ?> data = Map.of("conferenceId", "conferenceId1");
        List<LiveCall> res = redisSearchIndexTemplate.query(INDEX_NAME, data).get();
        res.forEach(System.out::println);
    }

    public void clear(){
        redisSearchIndexTemplate.getUnifiedJedis().jsonClear(INDEX_NAME_PERFIX+"*");
    }

    //@PostConstruct
    public void run() {
        index();
        add();
        search();
        clear();
        search();
        //System.out.println("--------");

        //update();
        //search();
    }
}
