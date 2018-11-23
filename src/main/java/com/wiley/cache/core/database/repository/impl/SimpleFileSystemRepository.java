package com.wiley.cache.core.database.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiley.cache.core.database.repository.DataBaseRepository;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleFileSystemRepository<KeyType,ValueType extends Serializable> implements DataBaseRepository<KeyType,ValueType> {
    private String repositorySystemPath;
    private String repositoryMasterFileTablePath;
    private Map<KeyType, UUID> mft;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SimpleFileSystemRepository(String repositorySystemPath, String repositoryMasterFileTablePath) {
        this.repositoryMasterFileTablePath = repositoryMasterFileTablePath;
        this.repositorySystemPath = repositorySystemPath;
        mft = loadMFT(repositoryMasterFileTablePath);
    }

    @Override
    public void put(KeyType key, ValueType value) {
        UUID uuid = UUID.randomUUID();
        String path = repositorySystemPath + "/" + uuid + ".tmp";
        try (FileOutputStream fileStream = new FileOutputStream(path);
             ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
            objectStream.writeObject(value);
            objectStream.flush();
            fileStream.flush();
            mft.put(key, uuid);
            updateMFT(mft);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ValueType get(KeyType key) {
        ValueType result = null;
        String path = repositorySystemPath + "/" + mft.get(key) + ".tmp";
        if(mft.containsKey(key)) {
            try (FileInputStream fileStream = new FileInputStream(path);
                 ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
                result = (ValueType)objectStream.readObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void update(KeyType key, ValueType value) {
        put(key, value);
    }

    @Override
    public void delete(KeyType key) {
        if (mft.containsKey(key)) {
            try {
                String path = repositorySystemPath + "/" + mft.get(key) + ".tmp";
                Files.deleteIfExists(Paths.get(path));
                mft.remove(key);
                updateMFT(mft);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clear() {
        try {
            FileUtils.cleanDirectory(new File(repositorySystemPath));
            mft.clear();
            updateMFT(mft);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isContain(KeyType key) {
        return mft.containsKey(key);
    }

    @Override
    public int getSize() {
        return mft.size();
    }

    private void updateMFT(Map<KeyType, UUID> mft) {
        File file = new File(repositoryMasterFileTablePath);
        if(file.exists() && !file.isDirectory()) {
            try {
                objectMapper.writeValue(file, mft);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<KeyType, UUID> loadMFT(String repositoryMasterFileTablePath) {
        Map<KeyType, UUID> mft = null;
        File file = new File(repositoryMasterFileTablePath);
        if(file.exists() && !file.isDirectory()) {
            try {
                mft = objectMapper.readValue(file, HashMap.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            mft = new HashMap<>();
            try {
                objectMapper.writeValue(file, mft);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mft;
    }
}
