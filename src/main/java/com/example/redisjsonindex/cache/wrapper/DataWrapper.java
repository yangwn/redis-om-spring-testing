package com.example.redisjsonindex.cache.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Bruce.Yang
 * @date 2023/2/9 -17:34
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class DataWrapper<T> {

    public DataWrapper(T data) {
        this.data = data;
        this.clazz = data.getClass().getName();
    }

    public DataWrapper<T> of(T data){
        return new DataWrapper<>(data);
    }

    private T data;

    public String clazz;
}
