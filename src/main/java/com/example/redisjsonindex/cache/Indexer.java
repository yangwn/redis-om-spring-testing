package com.example.redisjsonindex.cache;

import com.example.redisjsonindex.cache.constant.RedisDType;

import java.util.Map;

/**
 * Index interface
 *
 * @author Bruce.Yang
 * @date 2023/2/8 -21:57
 */
public interface Indexer {

    /**
     * build index
     *
     * @param indexName
     * @param prefix
     * @param columns
     */
    void create(String indexName, String prefix, Map<String, RedisDType> schemas);

    /**
     * drop index by name
     *
     * @param indexName
     */
    void drop(String indexName);
}
