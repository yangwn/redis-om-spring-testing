package com.example.redisjsonindex;

import com.example.redisjsonindex.cache.ISearchIndexerTemplate;
import com.example.redisjsonindex.cache.constant.RJPath;
import com.example.redisjsonindex.cache.constant.RedisDType;
import com.example.redisjsonindex.cache.wrapper.DataWrapper;
import com.example.redisjsonindex.model.LiveCall;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static redis.clients.jedis.params.ScanParams.SCAN_POINTER_START;

/**
 * @author Bruce.Yang
 * @date 2023/2/13 -11:18
 */
@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ISearchIndexerTemplateTests {

    @Autowired
    private ISearchIndexerTemplate<LiveCall> redisSearchIndexTemplate;

    private final String INDEX_NAME = "cmma-call-index";
    private final long COUNT = 300;
    private final String INDEX_NAME_PERFIX = "cmma:call:";
    private final Map<String, RedisDType> columnWithTypeMap = Map.of("cmsId", RedisDType.Tag, "callId", RedisDType.Tag, "ttl", RedisDType.Number, "poolingOrder", RedisDType.Number);

    @BeforeAll
    public void setUp() {
        redisSearchIndexTemplate.drop(INDEX_NAME);
        redisSearchIndexTemplate.create(INDEX_NAME, INDEX_NAME_PERFIX, columnWithTypeMap);
        for (int i = 0; i < COUNT; i++) {
            LiveCall liveCall = new LiveCall("id" + i, "cmsId" + i, "callId" + i, "conferenceId" + i, i, i - COUNT);
            redisSearchIndexTemplate.save(INDEX_NAME_PERFIX + i, liveCall);
        }
    }

    @Test
    public void queryOne() {
        LiveCall liveCall = redisSearchIndexTemplate.queryOne(INDEX_NAME, Map.of()).get();
        System.out.println(liveCall);
        assertNotNull(liveCall);
    }

    @Test
    public void queryAll() {
        List<LiveCall> res = redisSearchIndexTemplate.query(INDEX_NAME, Map.of()).get();
        assertEquals(COUNT, res.stream().count(), "list count is " + COUNT);
    }

    @Test
    public void queryOneByIndexNameAndConditions() {
        Map<String, ?> cond = Map.of("cmsId", "cmsId0", "callId", "callId0");
        List<LiveCall> res = redisSearchIndexTemplate.query(INDEX_NAME, cond).get();
        assertEquals(1, res.stream().count(), "list count is one");
    }

    @Test
    public void queryByIndexNameAndConditionAndPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("ttl")));
        List<LiveCall> res = redisSearchIndexTemplate.query(INDEX_NAME, Map.of(), pageable).get();
        res.forEach(it -> log.info(String.valueOf(it)));
        assertEquals(10, res.stream().count(), "list count is " + 10);
    }

    @Test
    public void queryByIndexNameAndConditionAndPageable2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("ttl")));
        List<LiveCall> res = redisSearchIndexTemplate.query(INDEX_NAME, Map.of(), pageable).get();
        res.forEach(it -> log.info(String.valueOf(it)));
        assertEquals(10, res.stream().count(), "list count is " + 10);
    }

    @Test
    public void save() {
        for (int i = 0; i <= 100000; i++) {
            LiveCall liveCall1 = new LiveCall("" + i, "cmsId" + i, "callId" + i, "conferenceId" + i, i, i - 100);
            redisSearchIndexTemplate.save(INDEX_NAME_PERFIX + i, liveCall1);
        }
    }

    @Test
    public void update() {
        redisSearchIndexTemplate.update(INDEX_NAME_PERFIX + "1", RJPath.S_DATA.param() + "callId", "callId1000");
        LiveCall livecall = redisSearchIndexTemplate.getData(INDEX_NAME_PERFIX + "1").get();
        assertEquals("callId1000", livecall.getCallId(), "update callId");
    }

    @Test
    public void updateObj() {
        LiveCall liveCall1 = redisSearchIndexTemplate.getData("cmma:call:1").get();
        System.out.println(liveCall1);

        LiveCall liveCall = new LiveCall();
        liveCall.setCmsId("100110cmsID");
        DataWrapper<LiveCall> dataWrapper = new DataWrapper(liveCall);
        redisSearchIndexTemplate.update("cmma:call:1", dataWrapper);
        LiveCall livecall = redisSearchIndexTemplate.getData(INDEX_NAME_PERFIX + "1").get();
        System.out.println(livecall);
        assertEquals("10000cmsID", livecall.getCallId(), "update callId");
    }

    @Test
    public void remove() {
        redisSearchIndexTemplate.remove(INDEX_NAME_PERFIX + "1", RJPath.S_DATA.param() + "callId");
    }

    @Test
    public void createIndexName() {
        redisSearchIndexTemplate.create(INDEX_NAME, INDEX_NAME_PERFIX, columnWithTypeMap);
    }

    @Test
    public void drop() {
        redisSearchIndexTemplate.drop(INDEX_NAME);
    }

    @Test
    public void clear() {
        ScanParams scanParams = new ScanParams().count(10).match("*");
        String cur = SCAN_POINTER_START;
        do {
            ScanResult<String> scanResult = redisSearchIndexTemplate.getUnifiedJedis().scan(cur, scanParams);
            redisSearchIndexTemplate.getUnifiedJedis().del(scanResult.getResult().toArray(new String[0]));
            cur = scanResult.getCursor();
        } while (!cur.equals(SCAN_POINTER_START));
    }

    @Test
    public void size() {
        int size = redisSearchIndexTemplate.match(INDEX_NAME, INDEX_NAME_PERFIX + "*").orElse(List.of()).size();
        System.out.println(size);
    }
}
