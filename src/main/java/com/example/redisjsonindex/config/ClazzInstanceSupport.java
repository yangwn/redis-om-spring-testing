package com.example.redisjsonindex.config;

import com.example.redisjsonindex.model.LiveCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bruce.Yang
 * @date 2023/2/13 -18:25
 */
@Slf4j
public class ClazzInstanceSupport implements InitializingBean {

    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();
    private final List<?> list = List.of(LiveCall.class);

    @Override
    public void afterPropertiesSet() {
        list.forEach(clazz -> classes.put(clazz.getClass().getName(), clazz.getClass()));
    }

    public Class getClass(String clazz) {
        return Optional.ofNullable(classes.get(clazz)).orElseGet(() -> putClass(clazz));
    }

    public Class putClass(String clazz) {
        Class cla = null;
        try {
            cla = Class.forName(clazz);
            classes.put(clazz, cla);
        } catch (ClassNotFoundException e) {
            log.error("{}", e.getMessage());
        }
        return cla;
    }
}
