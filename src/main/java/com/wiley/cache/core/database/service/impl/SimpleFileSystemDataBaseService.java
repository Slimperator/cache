package com.wiley.cache.core.database.service.impl;

import com.wiley.cache.core.database.repository.DataBaseRepository;
import com.wiley.cache.core.database.service.DataBaseService;

public class SimpleFileSystemDataBaseService<KeyType,ValueType> implements DataBaseService<KeyType,ValueType> {
    private DataBaseRepository<KeyType,ValueType> repository;

    public SimpleFileSystemDataBaseService(DataBaseRepository<KeyType, ValueType> repository) {
        this.repository = repository;
    }

    @Override
    public void put(KeyType key, ValueType value) {
        repository.put(key, value);
    }

    @Override
    public ValueType get(KeyType key) {
        return repository.get(key);
    }

    @Override
    public void update(KeyType key, ValueType value) {
        repository.update(key, value);
    }

    @Override
    public void delete(KeyType key) {
        repository.delete(key);
    }

    @Override
    public void clear() {
        repository.clear();
    }

    @Override
    public boolean isContain(KeyType key) {
        return repository.isContain(key);
    }

    public DataBaseRepository<KeyType, ValueType> getRepository() {
        return repository;
    }

    public void setRepository(DataBaseRepository<KeyType, ValueType> repository) {
        this.repository = repository;
    }

    @Override
    public int getSize() {
        return repository.getSize();
    }
}
