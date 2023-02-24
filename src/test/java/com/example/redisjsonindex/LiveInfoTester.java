package com.example.redisjsonindex;

import com.example.redisjsonindex.cache.ISearchIndexerTemplate;
import com.example.redisjsonindex.cache.wrapper.DataWrapper;
import com.example.redisjsonindex.model.Expectation;
import com.example.redisjsonindex.model.LiveCall;
import com.example.redisjsonindex.model.LiveInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.json.Path;

/**
 * @author Bruce.Yang
 * @date 2023/2/21 -23:23
 */
@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LiveInfoTester {

    @Autowired
    private ISearchIndexerTemplate<LiveInfo> redisSearchIndexTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final String INDEX_NAME = "cmma-call-index";
    private final long COUNT = 300;

    @Test
    public void save() {
        LiveCall liveCall = new LiveCall("id", "cmsId", "clllId", "cppp10", 100, 100);
        LiveInfo liveInfo = new LiveInfo("1", new Expectation<>(false, false), liveCall);
        redisSearchIndexTemplate.save("1", liveInfo);
    }

    @Test
    public void mapper() throws JsonProcessingException {
        LiveCall liveCall = new LiveCall("id", "cmsId", "clllId", "cppp10", 100, 100);
        LiveInfo liveInfo = new LiveInfo("1", new Expectation<>(false, false), liveCall);
        String msg = objectMapper.writeValueAsString(liveInfo);
        System.out.println(msg);
    }

    @Test
    public void update() throws JsonProcessingException {
        //save();
        //LiveCall liveCall = new LiveCall("id", "cmsId", "clllId", "cppp10", 99, 99);
        //LiveInfo liveInfo = new LiveInfo("1", new Expectation<>(true), liveCall);
        //String str = objectMapper.writeValueAsString(liveInfo);
        //System.out.println(str);

        LiveInfo live = redisSearchIndexTemplate.getData("1").get();
        live.getExpectation().setExpected(true);
        live.getLiveCall().setTtl(99);
        live.getLiveCall().setPoolingOrder(99);
        redisSearchIndexTemplate.save("1", live);
        //redisSearchIndexTemplate.getUnifiedJedis().jsonSet("1", Path.of(".data.expectation"));
        //redisSearchIndexTemplate.getUnifiedJedis().jsonSetWithEscape("1", new DataWrapper<>(liveInfo));
    }
}
