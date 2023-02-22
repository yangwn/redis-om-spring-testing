package com.example.redisjsonindex.cache;

import com.example.redisjsonindex.cache.wrapper.DataWrapper;
import org.springframework.data.domain.Pageable;
import redis.clients.jedis.UnifiedJedis;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Bruce.Yang
 * @date 2023/2/9 -11:11
 */
public interface ISearchIndexerTemplate<T> extends Indexer, IStorage<T> {

    /**
     * Get UnifiedJedis
     *
     * @return
     */
    UnifiedJedis getUnifiedJedis();

    /**
     * Query by condition.
     * Notice: The relationship of condition is AND.
     * If set complex conditions, please use way of redis search expression.
     *
     * @param indexName
     * @param conditions: columnName,value
     * @return
     */
    Optional<List<T>> query(String indexName, Map<String, ?> conditions);

    /**
     * Query by page.
     *
     * @param indexName
     * @param conditions
     * @param pageable
     * @return
     */
    Optional<List<T>> query(String indexName, Map<String, ?> conditions, Pageable pageable);

    /**
     * Query by condition for first one.
     *
     * @param indexName
     * @param conditions
     * @return
     */
    Optional<T> queryOne(String indexName, Map<String, ?> conditions);

    /**
     * Get data by key.
     *
     * @param key
     * @return
     */
    Optional<T> getData(String key);


    /**
     * Match by expression.
     *
     * @param indexName
     * @param expression: MUST use redis search expression.
     * @return
     */
    Optional<List<T>> match(String indexName, String expression);

    /**
     * Match by page
     *
     * @param indexName
     * @param expression
     * @param pageable
     * @return
     */
    Optional<List<T>> match(String indexName, String expression, Pageable pageable);

    /**
     * match by expression for first one.
     *
     * @param indexName
     * @param expression
     * @return
     */
    Optional<T> matchOne(String indexName, String expression);
}
