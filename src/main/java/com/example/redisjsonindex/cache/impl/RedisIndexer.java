package com.example.redisjsonindex.cache.impl;

import com.example.redisjsonindex.cache.Indexer;
import com.example.redisjsonindex.cache.constant.RJPath;
import com.example.redisjsonindex.cache.constant.RedisDType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Schema;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bruce.Yang
 * @date 2023/2/8 -22:53
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisIndexer implements Indexer {

    private final UnifiedJedis unifiedJedis;

    @Override
    public void create(String indexName, String prefix, Map<String, RedisDType> fields) {
        Map<String, Schema.Field> fieldMap = indexNameToFields(fields);
        Schema schema = new Schema();
        fieldMap.forEach((property, field) -> {
            schema.addField(field).as(property);
        });
        IndexDefinition definition = new IndexDefinition(IndexDefinition.Type.JSON).setPrefixes(prefix);
        this.drop(indexName);
        unifiedJedis.ftCreate(indexName, IndexOptions.defaultOptions().setDefinition(definition), schema);
    }

    @Override
    public void drop(String indexName) {
        try {
            unifiedJedis.ftDropIndex(indexName);
        } catch (JedisDataException jedisDataException) {
            log.info("Drop redis index name:{}, because of {}.", indexName, jedisDataException.getMessage());
        }
    }

    private Map<String, Schema.Field> indexNameToFields(Map<String, RedisDType> columnNameWithType) {
        return columnNameWithType.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,e -> getField(e.getKey(), e.getValue())));
    }

    private Schema.Field getField(String column, RedisDType type) {
        if (type == RedisDType.Tag) {
            return new Schema.TagField(RJPath.C_DATA.param() + column);
        } else if (type == RedisDType.Number) {
            return new Schema.Field(RJPath.C_DATA.param() + column, Schema.FieldType.NUMERIC);
        } else if (type == RedisDType.String) {
            return new Schema.TextField(RJPath.C_DATA.param() + column);
        }
        throw new IllegalArgumentException("column is " + column + ", type is " + type + " no supported.");
    }

}
