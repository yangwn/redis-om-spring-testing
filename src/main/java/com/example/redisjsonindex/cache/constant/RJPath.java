package com.example.redisjsonindex.cache.constant;

import redis.clients.jedis.json.Path;

/**
 * @author Bruce.Yang
 * @date 2023/2/8 -22:53
 */
public enum RJPath {

    /**
     * Redis Json Path started with $
     */
    C_DATA("$.data."),

    /**
     * Redis data started
     */
    S_DATA("data."),

    T_DATA("$.data"),
    ;

    private final String path;

    RJPath(String path) {
        this.path = path;
    }

    public String param() {
        return path;
    }

    public Path path(){
        return Path.of(param());
    }
}
