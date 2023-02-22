package com.example.redisjsonindex.cache;

/**
 * create index from dataset.
 *
 * @author Bruce.Yang
 * @date 2023/2/8 -16:04
 */
public interface IStorage<T> {

    /**
     * Add and Save object.
     * will refresh all of columns.
     *
     * @param key
     * @param path
     * @param t
     */
    void save(String key, T t);

    /**
     * Update all object.
     *
     * @param key
     * @param value
     */
    void update(String key, Object value);

    /**
     * Update sepical column.
     *
     * @param key
     * @param path
     * @param value
     */
    void update(String key, String path, String value);

    /**
     * Remove item by JSON path.
     *
     * @param key
     * @param paths
     */
    void remove(String key, String... paths);
}
