package com.example.redisjsonindex.cache;

import org.springframework.data.domain.Pageable;
import redis.clients.jedis.search.SearchResult;

import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * Search Interface
 *
 * @author Bruce.Yang
 * @date 2023/2/8 -16:01
 */
public interface ISearcher {

    /**
     * combine tag
     *
     * @param conditions
     * @return
     */
    default String combineTagQueryKey(Map<String, ?> conditions) {
        Objects.requireNonNull(conditions);
        return conditions.entrySet().stream()
                .map(e -> "@" + e.getKey() + ":{" + e.getValue() + "}")
                .collect(joining(" "));
    }

    /**
     * Query by condition.
     * Notice: The relationship of condition is AND.
     * If set complex conditions, please use way of redis search expression.
     *
     * @param indexName
     * @param conditions: columnName,value
     * @return
     */
    SearchResult query(String indexName, Map<String, String> conditions);

    /**
     * Query by page.
     *
     * @param indexName
     * @param conditions
     * @param pageable
     * @return
     */
    SearchResult query(String indexName, Map<String, String> conditions, Pageable pageable);

    /**
     * Query by condition for first one.
     *
     * @param indexName
     * @param conditions
     * @return
     */
    SearchResult queryOne(String indexName, Map<String, String> conditions);

    /**
     * Match by expression.
     *
     * @param indexName
     * @param expression: MUST use redis search expression.
     * @return
     */
    SearchResult match(String indexName, String expression);

    /**
     * Match by page
     *
     * @param indexName
     * @param expression
     * @param pageable
     * @return
     */
    SearchResult match(String indexName, String expression, Pageable pageable);

    /**
     * match by expression for first one.
     *
     * @param indexName
     * @param expression
     * @return
     */
    SearchResult matchOne(String indexName, String expression);
}
