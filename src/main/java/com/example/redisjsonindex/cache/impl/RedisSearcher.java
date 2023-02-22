package com.example.redisjsonindex.cache.impl;

import com.example.redisjsonindex.cache.ISearcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.Map;

/**
 * @author Bruce.Yang
 * @date 2023/2/8 -23:10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSearcher implements ISearcher {

    private final UnifiedJedis unifiedJedis;

    @Override
    public SearchResult query(String indexName, Map<String, String> conditions) {
        return match(indexName, combineTagQueryKey(conditions));
    }

    @Override
    public SearchResult query(String indexName, Map<String, String> conditions, Pageable pageable) {
        String cond = combineTagQueryKey(conditions);
        log.info("condition: {}", cond);
        return match(indexName, combineTagQueryKey(conditions), pageable);
    }

    @Override
    public SearchResult queryOne(String indexName, Map<String, String> conditions) {
        return matchOne(indexName, combineTagQueryKey(conditions));
    }

    @Override
    public SearchResult match(String indexName, String expression) {
        Query query = this.compositeQuery(expression);
        query.limit(0, 500);
        return unifiedJedis.ftSearch(indexName, query);
    }

    @Override
    public SearchResult match(String indexName, String expression, Pageable pageable) {
        Query query = this.compositeQuery(expression);
        query.limit(Long.valueOf(pageable.getOffset()).intValue(), Long.valueOf(pageable.getPageSize()).intValue());
        pageable.getSort().get().forEachOrdered(order -> query.setSortBy(order.getProperty(), order.isAscending()));
        return unifiedJedis.ftSearch(indexName, query);
    }

    @Override
    public SearchResult matchOne(String indexName, String expression) {
        Query query = this.compositeQuery(expression);
        query.limit(0, 1);
        return unifiedJedis.ftSearch(indexName, query);
    }

    private Query compositeQuery(String expression) {
        return switch (expression) {
            case String s && StringUtils.isEmpty(s) -> new Query("*");
            default -> new Query(expression);
        };
    }
}
