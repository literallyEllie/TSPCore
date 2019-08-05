package net.tsp.core.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Ellie :: 25/07/2019
 */
public class MapBuilder<K, V> {

    private Map<K, V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder() {
        this (Maps.newHashMap());
    }

    public MapBuilder linked() {
        this.map = Maps.newLinkedHashMap();
        return this;
    }

    public MapBuilder put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> remove(K key) {
        map.remove(key);
        return this;
    }

    public Map<K, V> getMap() {
        return map;
    }

}
