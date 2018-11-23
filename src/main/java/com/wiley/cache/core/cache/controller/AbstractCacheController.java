package com.wiley.cache.core.cache.controller;

import com.wiley.cache.core.cache.Cache;

import java.util.List;

public abstract class AbstractCacheController<KeyType,ValueType> {
    protected List<Cache<KeyType,ValueType>> cacheLevels;

    public AbstractCacheController(List<Cache<KeyType,ValueType>> cacheLevels) {
        this.cacheLevels = cacheLevels;
    }

    public ValueType findObject(KeyType key) {
        ValueType result = null;
        for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
            result = cacheLevel.get(key);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public void removeObject(KeyType key) {
        for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
            ValueType result = cacheLevel.get(key);
            if (result != null) {
                cacheLevel.delete(key);
                break;
            }
        }
    }

    public void cacheObject(KeyType key, ValueType value) {
        if (findObject(key) == null) {
            for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
                if(cacheLevel.getEmptySpace() > 0) {
                    cacheLevel.put(key, value);
                    break;
                }
            }
        }
        else {
            for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
                ValueType result = cacheLevel.get(key);
                if (result != null) {
                    cacheLevel.put(key, value);
                    break;
                }
            }
        }
    }

    public void clearCache() {
        cacheLevels
                .forEach(cacheLevel ->
                cacheLevel.clear());
    }

    public boolean isEmptySpace() {
        return cacheLevels
                .stream()
                .anyMatch(cacheLevels -> cacheLevels.getEmptySpace() !=0);
    }
}
