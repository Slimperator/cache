package com.wiley.cache.core.cache.impl.filesystem;

import com.wiley.cache.core.cache.Cache;
import com.wiley.cache.core.database.service.DataBaseService;

public class FileSystemCacheImpl <KeyType,ValueType> implements Cache<KeyType,ValueType> {
    private DataBaseService<KeyType,ValueType> dataBaseService;
    private int maxsize;

    public FileSystemCacheImpl(DataBaseService dataBaseService, int maxsize) {
        this.dataBaseService = dataBaseService;
        this.maxsize = maxsize;
    }

    @Override
    public void put(KeyType key, ValueType value) {
        if(maxsize > dataBaseService.getSize()) {
            dataBaseService.put(key, value);
        }
    }

    @Override
    public ValueType get(KeyType key) {
        return dataBaseService.get(key);
    }

    @Override
    public ValueType cut(KeyType key) {
        ValueType result = dataBaseService.get(key);
        dataBaseService.delete(key);
        return result;
    }

    @Override
    public void update(KeyType key, ValueType value) {
        dataBaseService.update(key, value);
    }

    @Override
    public void delete(KeyType key) {
        dataBaseService.delete(key);
    }

    @Override
    public void clear() {
        dataBaseService.clear();
    }

    @Override
    public void setCacheSize(int maxSize) {
        this.maxsize = maxSize;
    }

    @Override
    public int getCacheSize() {
        return this.maxsize;
    }

    @Override
    public boolean isContain(KeyType key) {
        return dataBaseService.isContain(key);
    }

    @Override
    public int getEmptySpace() {
        return maxsize - dataBaseService.getSize();
    }
}
