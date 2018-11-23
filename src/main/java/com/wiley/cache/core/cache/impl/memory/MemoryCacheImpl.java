package com.wiley.cache.core.cache.impl.memory;

import com.wiley.cache.core.cache.Cache;

import java.util.HashMap;
import java.util.Map;

public class MemoryCacheImpl<KeyType,ValueType> implements Cache<KeyType,ValueType> {
    private Map<KeyType,ValueType> cache = new HashMap();
    private int maxsize;

    public MemoryCacheImpl(int maxsize) {
        this.maxsize = maxsize;
    }

    @Override
    public void put(KeyType key, final ValueType value) {
        if(maxsize > cache.size()) {
            cache.put(key, value);
        }
    }

    @Override
    public ValueType get(KeyType key) {
        return cache.get(key);
    }

    @Override
    public ValueType cut(KeyType key) {
        ValueType result = cache.get(key);
        cache.remove(key);
        return result;
    }

    @Override
    public void update(KeyType key, ValueType value) {
        cache.put(key, value);
    }

    @Override
    public void delete(KeyType key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean isContain(KeyType key) {
        return cache.containsKey(key);
    }

    @Override
    public int getEmptySpace() {
        return maxsize - cache.size();
    }

    @Override
    public void setCacheSize(int maxSize) {
        this.maxsize = maxSize;
    }

    @Override
    public int getCacheSize() {
        return maxsize;
    }
}
