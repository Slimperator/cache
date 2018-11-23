package com.wiley.cache.core.cache;

public interface Cache<KeyType,ValueType> {
    void put(KeyType key, ValueType value);
    ValueType get(KeyType key);
    ValueType cut(KeyType key);
    void update(KeyType key, ValueType value);
    void delete(KeyType key);
    void clear();
    void setCacheSize(int maxSize);
    int getCacheSize();
    boolean isContain(KeyType key);
    int getEmptySpace();
}
