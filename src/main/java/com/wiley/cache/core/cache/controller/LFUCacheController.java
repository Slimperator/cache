package com.wiley.cache.core.cache.controller;

import com.wiley.cache.core.cache.Cache;

import java.util.*;

public class LFUCacheController<KeyType,ValueType> extends AbstractCacheController<KeyType,ValueType> {
    private Map<KeyType, KeyFrequency> keyFrequencyCallMap = new HashMap<>();

    public LFUCacheController(List<Cache<KeyType, ValueType>> cacheLevels) {
        super(cacheLevels);
    }

    @Override
    public ValueType findObject(KeyType key) {
        ValueType result = super.findObject(key);
        if (result != null) {
            incrementKeyFrequency(key);
            pickSpace(key);
        }
        return result;
    }

    @Override
    public void removeObject(KeyType key) {
        super.removeObject(key);
        keyFrequencyCallMap.remove(key);
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
        }
        else {
            updateRecordStrategy(key, value);
        }
    }

    private void ifCacheFullStrategy(KeyType key, ValueType value) {
        int latestCacheLevel = cacheLevels.size() - 1;
        removeObject(findTheLessUsedInLevel(latestCacheLevel));
        cacheLevels.get(latestCacheLevel).put(key, value);
        KeyFrequency keyFrequency = new KeyFrequency();
        keyFrequency.setCacheLevel(latestCacheLevel);
        keyFrequencyCallMap.put(key, keyFrequency);
    }

    private void updateRecordStrategy(KeyType key, ValueType value) {
        for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
            ValueType result = cacheLevel.get(key);
            if (result != null) {
                cacheLevel.put(key, value);
                keyFrequencyCallMap.get(key).incrementKeyFrequency();
                break;
            }
        }
    }

    private void inputNewRecordStrategy(KeyType key, ValueType value) {
        for (Cache<KeyType,ValueType> cacheLevel: cacheLevels) {
            if(cacheLevel.getEmptySpace() > 0) {
                cacheLevel.put(key, value);
                KeyFrequency keyFrequency = new KeyFrequency();
                keyFrequency.setCacheLevel(cacheLevels.indexOf(cacheLevel));
                keyFrequencyCallMap.put(key, keyFrequency);
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

    private void pickSpace(KeyType key) {
        KeyFrequency keyFrequency = keyFrequencyCallMap.get(key);
        if (isKeyTheBiggestInLevel(key, keyFrequency)) {
            int newCacheLevel = keyFrequency.getCacheLevel() - 1;
            if (cacheLevels.get(newCacheLevel).getEmptySpace() > 0) {
                cacheLevels.get(newCacheLevel)
                        .put(key, cacheLevels.get(keyFrequency.getCacheLevel()).cut(key));
                changeKeyCacheLevel(key, newCacheLevel);
            }
            else {
                Optional<Map.Entry<KeyType, KeyFrequency>> keyToSwitch = keyFrequencyCallMap
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue()
                                .getCacheLevel() ==
                                newCacheLevel)
                        .min(Comparator.comparing(Map.Entry::getValue));
                if (keyToSwitch.get().getValue().getFrequency() < keyFrequency.getFrequency()) {
                    switchObjects(key, keyToSwitch.get().getKey());
                }
            }
        }
    }

    private boolean isKeyTheBiggestInLevel(KeyType key, KeyFrequency keyFrequency) {
        boolean result = false;
        if (keyFrequency.getCacheLevel() != 0) {
            result = keyFrequencyCallMap
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != key &&
                            entry.getValue().getCacheLevel() ==
                                    keyFrequency.getCacheLevel())
                    .noneMatch(entry -> entry.getValue().getFrequency() >
                            keyFrequency.getFrequency());
        }
        return result;
    }

    private KeyType findTheLessUsedInLevel(int level) {
        return keyFrequencyCallMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCacheLevel() == level)
                .min((Comparator.comparing(Map.Entry::getValue)))
                .orElseThrow(NoSuchElementException::new)
                .getKey();
    }

    private void switchObjects(KeyType key1, KeyType key2) {
        KeyFrequency keyFrequency1 = keyFrequencyCallMap.get(key1);
        KeyFrequency keyFrequency2 = keyFrequencyCallMap.get(key2);
        Cache<KeyType,ValueType> cache1 = cacheLevels.get(keyFrequency1.getCacheLevel());
        Cache<KeyType,ValueType> cache2 = cacheLevels.get(keyFrequency2.getCacheLevel());
        ValueType value1 = cache1.cut(key1);
        ValueType value2 = cache2.cut(key2);
        cache1.put(key2, value2);
        cache2.put(key1, value1);
        changeKeyCacheLevel(key1, keyFrequency2.getCacheLevel());
        changeKeyCacheLevel(key2, keyFrequency1.getCacheLevel());
    }

    private void incrementKeyFrequency(KeyType key) {
        if (keyFrequencyCallMap.containsKey(key)) {
            KeyFrequency keyFrequency = keyFrequencyCallMap.get(key);
            keyFrequency.incrementKeyFrequency();
        }
    }

    private void changeKeyCacheLevel(KeyType key, int cacheLevel) {
        if (keyFrequencyCallMap.containsKey(key)) {
            KeyFrequency keyFrequency = keyFrequencyCallMap.get(key);
            keyFrequency.setCacheLevel(cacheLevel);
        }
    }

    private class KeyFrequency implements Comparable<KeyFrequency>{
        int frequency = 0;
        int cacheLevel = 0;

        public void incrementKeyFrequency() {
            frequency++;
        }

        public void decrementKeyFrequency() {
            frequency--;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getCacheLevel() {
            return cacheLevel;
        }

        public void setCacheLevel(int cacheLevel) {
            this.cacheLevel = cacheLevel;
        }

        @Override
        public int compareTo(KeyFrequency o) {
            return frequency - o.frequency;
        }
    }
}
