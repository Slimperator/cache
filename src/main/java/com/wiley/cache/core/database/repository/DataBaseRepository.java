package com.wiley.cache.core.database.repository;

import java.io.Serializable;

public interface DataBaseRepository <KeyType,ValueType> {
    void put(KeyType key, ValueType value);
    ValueType get(KeyType key);
    void update(KeyType key, ValueType value);
    void delete(KeyType key);
    void clear();
    boolean isContain(KeyType key);
    int getSize();
}
