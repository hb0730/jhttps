package com.hb0730.commons.lang.map;

import com.hb0730.commons.lang.builder.Builder;

import java.util.Map;

/**
 * 链式map
 *
 * @author bing_huang
 * @since 2.0.3
 */
public class MapBuilder<K, V> implements Builder<Map<K, V>> {
    private static final long serialVersionUID = -5240705513723579529L;
    private final Map<K, V> map;

    /**
     * 创建{@link java.util.HashMap}类型的map链
     *
     * @param <K> key类型
     * @param <V> map类型
     * @return 链式Map {@link MapBuilder}
     * @see java.util.HashMap
     * @see MapUtils#newHashMap()
     */
    public static <K, V> MapBuilder<K, V> builder() {
        return builder(MapUtils.newHashMap());
    }

    /**
     * 创建{@link java.util.HashMap}类型的map链
     *
     * @param initialCapacity 初始容量
     * @param <K>             key类型
     * @param <V>             map类型
     * @return 链式Map {@link MapBuilder}
     * @see java.util.HashMap
     * @see MapUtils#newHashMap(int)
     */
    public static <K, V> MapBuilder<K, V> builder(int initialCapacity) {
        return builder(MapUtils.newHashMap(initialCapacity));
    }

    /**
     * 创建链式map
     *
     * @param map map实现
     * @param <K> map key类型
     * @param <V> map value类型
     * @return 链式map {@link MapBuilder}
     */
    public static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    /**
     * 创建链式的Map
     *
     * @param map 要使用的map
     */
    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    /**
     * 链式Map创建
     *
     * @param key   Key类型
     * @param value Value类型
     * @return 当前类
     */
    public MapBuilder<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * 链式Map创建
     *
     * @param map 合并map
     * @return 当前类
     */
    public MapBuilder<K, V> putAll(Map<K, V> map) {
        this.map.putAll(map);
        return this;
    }

    @Override
    public Map<K, V> build() {
        return this.map;
    }
}
