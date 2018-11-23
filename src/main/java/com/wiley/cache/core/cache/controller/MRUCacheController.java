package com.wiley.cache.core.cache.controller;

import com.wiley.cache.core.cache.Cache;

import java.util.*;

public class MRUCacheController<KeyType,ValueType> extends AbstractCacheController<KeyType,ValueType> {
    private Map<KeyType, KeyDateInfo> keyFrequencyCallMap = new HashMap<>();

    public MRUCacheController(List<Cache<KeyType, ValueType>> cacheLevels) {
        super(cacheLevels);
    }

    @Override
    public ValueType findObject(KeyType key) {
        return super.findObject(key);
    }

    @Override
    public void removeObject(KeyType key) {
        super.removeObject(key);
        int levelWithNewSpace = keyFrequencyCallMap.get(key).getCacheLevel();
        keyFrequencyCallMap.remove(key);
        reCache(levelWithNewSpace);
    }

    @Override
    public void cacheObject(KeyType key, ValueType value) {
        if (super.findObject(key) == null) {
            if (isEmptySpace()) {
                inputNewRecordStrategy(key, value);
            }
            else {
                ifCacheFullStrategy(key, value);
            }
        } else {
            updateRecordStrategy(key, value);
        }
    }

    private void ifCacheFullStrategy(KeyType key, ValueType value) {
        int latestCacheLevel = cacheLevels.size() - 1;
        removeObject(findTheYoungestInLevel(latestCacheLevel));
        cacheLevels.get(latestCacheLevel).put(key, value);
        KeyDateInfo keyDateInfo = new KeyDateInfo();
        keyDateInfo.setCacheLevel(cacheLevels.indexOf(latestCacheLevel));
        keyDateInfo.setCreationDate(new Date());
        keyFrequencyCallMap.put(key, keyDateInfo);
    }

    private void updateRecordStrategy(KeyType key, ValueType value) {
        for (Cache<KeyType, ValueType> cacheLevel : cacheLevels) {
            ValueType result = cacheLevel.get(key);
            if (result != null) {
                cacheLevel.put(key, value);
                break;
            }
        }
    }

    private void inputNewRecordStrategy(KeyType key, ValueType value) {
        for (Cache<KeyType, ValueType> cacheLevel : cacheLevels) {
            if (cacheLevel.getEmptySpace() > 0) {
                cacheLevel.put(key, value);
                KeyDateInfo keyDateInfo = new KeyDateInfo();
                keyDateInfo.setCacheLevel(cacheLevels.indexOf(cacheLevel));
                keyDateInfo.setCreationDate(new Date());
                keyFrequencyCallMap.put(key, keyDateInfo);
                break;
            }
        }
    }

    @Override
    public void clearCache() {
        super.clearCache();
        keyFrequencyCallMap.clear();
    }

    @Override
    public boolean isEmptySpace() {
        return super.isEmptySpace();
    }

    private void reCache(int levelWithNewSpace) {
        if (cacheLevels.size() > levelWithNewSpace + 1) {
            pushObject(findTheOldestInLevel(levelWithNewSpace + 1));
            reCache(levelWithNewSpace + 1);
        }
    }

    private KeyType findTheOldestInLevel(int level) {
        return keyFrequencyCallMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCacheLevel() == level)
                .min((Comparator.comparing(Map.Entry::getValue)))
                .orElseThrow(NoSuchElementException::new)
                .getKey();
    }

    private KeyType findTheYoungestInLevel(int level) {
        return keyFrequencyCallMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCacheLevel() == level)
                .max((Comparator.comparing(Map.Entry::getValue)))
                .orElseThrow(NoSuchElementException::new)
                .getKey();
    }

    private void pushObject(KeyType key) {
        KeyDateInfo keyDateInfo = keyFrequencyCallMap.get(key);
        Cache<KeyType,ValueType> cacheOld = cacheLevels.get(keyDateInfo.getCacheLevel());
        Cache<KeyType,ValueType> cacheNew = cacheLevels.get(keyDateInfo.getCacheLevel() - 1);
        ValueType value = cacheOld.cut(key);
        cacheNew.put(key, value);
        changeKeyCacheLevel(key, keyDateInfo.getCacheLevel() - 1);
    }

    private void changeKeyCacheLevel(KeyType key, int cacheLevel) {
        if (keyFrequencyCallMap.containsKey(key)) {
            KeyDateInfo keyDateInfo = keyFrequencyCallMap.get(key);
            keyDateInfo.setCacheLevel(cacheLevel);
        }
    }

    private class KeyDateInfo implements Comparable<KeyDateInfo>{
        Date creationDate = new Date();
        int cacheLevel = 0;

        public Date getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
        }

        public int getCacheLevel() {
            return cacheLevel;
        }

        public void setCacheLevel(int cacheLevel) {
            this.cacheLevel = cacheLevel;
        }

        @Override
        public int compareTo(KeyDateInfo o) {
            return creationDate.compareTo(o.getCreationDate());
        }
    }
}
