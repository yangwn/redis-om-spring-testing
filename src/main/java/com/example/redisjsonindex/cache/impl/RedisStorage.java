package com.example.redisjsonindex.cache.impl;

import com.example.redisjsonindex.cache.IStorage;
import com.example.redisjsonindex.cache.wrapper.DataWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.Path;

import java.util.Arrays;

/**
 * @author Bruce.Yang
 * @date 2023/2/8 -16:45
 */
@Component
@RequiredArgsConstructor
public class RedisStorage<T> implements IStorage<T> {

    private final UnifiedJedis unifiedJedis;

    @Override
    public void save(String key, T t) {
        unifiedJedis.jsonSet(key, Path.ROOT_PATH, new DataWrapper<>(t));
    }

    @Override
    public void update(String key, Object value) {
        unifiedJedis.jsonSetWithEscape(key, new DataWrapper<>(value));
        unifiedJedis.expire(key, 100000);
    }

    @Override
    public void update(String key, String path, String value) {
        unifiedJedis.jsonSetWithPlainString(key, Path.of(path), value);
    }

    @Override
    public void remove(String key, String... paths) {
        if (ArrayUtils.isEmpty(paths)) {
            unifiedJedis.jsonDel(key);
            return;
        }
        Arrays.stream(paths).forEach(path -> unifiedJedis.jsonDel(key, Path.of(path)));
    }
}
