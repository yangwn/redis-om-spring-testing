package com.example.redisjsonindex.cache.impl;

import com.example.redisjsonindex.cache.ISearchIndexerTemplate;
import com.example.redisjsonindex.cache.ISearcher;
import com.example.redisjsonindex.cache.IStorage;
import com.example.redisjsonindex.cache.Indexer;
import com.example.redisjsonindex.cache.constant.RedisDType;
import com.example.redisjsonindex.cache.wrapper.DataWrapper;
import com.example.redisjsonindex.config.ClazzInstanceSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.SearchResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Bruce.Yang
 * @date 2023/2/9 -11:12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSearchIndexTemplate<T> implements ISearchIndexerTemplate<T> {

    private final ObjectMapper objectMapper;
    private final UnifiedJedis unifiedJedis;
    private final ClazzInstanceSupport clazzInstanceSupport;

    private final Indexer redisIndexer;
    private final ISearcher redisSearcher;
    private final IStorage<T> redisStorage;

    @Override
    public UnifiedJedis getUnifiedJedis() {
        return unifiedJedis;
    }

    @Override
    public Optional<List<T>> query(String indexName, Map<String, ?> conditions) {
        return this.match(indexName, redisSearcher.combineTagQueryKey(conditions));
    }

    @Override
    public Optional<List<T>> query(String indexName, Map<String, ?> conditions, Pageable pageable) {
        return this.match(indexName, redisSearcher.combineTagQueryKey(conditions), pageable);
    }

    @Override
    public Optional<T> queryOne(String indexName, Map<String, ?> conditions) {
        return this.matchOne(indexName, redisSearcher.combineTagQueryKey(conditions));
    }

    @Override
    public Optional<T> getData(String key) {
        return Optional.ofNullable(convertToBean(unifiedJedis.jsonGet(key)));
    }

    @Override
    public Optional<List<T>> match(String indexName, String expression) {
        SearchResult searchResult = redisSearcher.match(indexName, expression);
        List<T> result = searchResult.getDocuments().stream()
                .map(doc -> StreamSupport.stream(doc.getProperties().spliterator(), true)
                        .map(this::convertToBean).findFirst().orElse(null))
                .collect(Collectors.toList());
        return Optional.of(result);
    }

    @Override
    public Optional<List<T>> match(String indexName, String expression, Pageable pageable) {
        SearchResult searchResult = redisSearcher.match(indexName, expression, pageable);
        List<T> result = searchResult.getDocuments().stream()
                .map(doc -> StreamSupport.stream(doc.getProperties().spliterator(), true)
                        .filter(it -> StringUtils.equals(it.getKey(), "$"))
                        .map(this::convertToBean).findFirst().orElse(null)
                ).collect(Collectors.toList());
        return Optional.of(result);
    }

    @Override
    public Optional<T> matchOne(String indexName, String expression) {
        SearchResult searchResult = redisSearcher.matchOne(indexName, expression);
        Document doc = searchResult.getDocuments().stream().findFirst().orElseThrow();
        return StreamSupport.stream(doc.getProperties().spliterator(), false).map(this::convertToBean).findFirst();
    }

    @Override
    public void save(String key, T t) {
        redisStorage.save(key, t);
    }

    @Override
    public void update(String key, Object value) {
        redisStorage.update(key, value);
    }

    @Override
    public void update(String key, String path, String value) {
        redisStorage.update(key, path, value);
    }

    @Override
    public void remove(String key, String... paths) {
        redisStorage.remove(key, paths);
    }

    @Override
    public void create(String indexName, String prefix, Map<String, RedisDType> columns) {
        redisIndexer.create(indexName, prefix, columns);
    }

    @Override
    public void drop(String indexName) {
        redisIndexer.drop(indexName);
    }

    @SneakyThrows
    private T convertToBean(Object object) {
        DataWrapper dataWrapper = objectMapper.convertValue(object, DataWrapper.class);
        Class<T> clazz = clazzInstanceSupport.getClass(dataWrapper.getClazz());
        return objectMapper.convertValue(dataWrapper.getData(), clazz);
    }

    @SneakyThrows
    private T convertToBean(Map.Entry entry) {
        DataWrapper dataWrapper = objectMapper.readValue((String) entry.getValue(), DataWrapper.class);
        Class<T> clazz = clazzInstanceSupport.getClass(dataWrapper.getClazz());
        return objectMapper.convertValue(dataWrapper.getData(), clazz);
    }
}
