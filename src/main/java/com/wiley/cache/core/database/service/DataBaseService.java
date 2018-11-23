package com.wiley.cache.core.database.service;

public interface DataBaseService<KeyType,ValueType> {
    void put(KeyType key, ValueType value);
    ValueType get(KeyType key);
    void update(KeyType key, ValueType value);
    void delete(KeyType key);
    void clear();
    boolean isContain(KeyType key);
    int getSize();
}
