package com.wiley.cache.client.app;

import com.wiley.cache.core.cache.Cache;
import com.wiley.cache.core.cache.controller.AbstractCacheController;
import com.wiley.cache.core.cache.controller.LFUCacheController;
import com.wiley.cache.core.database.repository.DataBaseRepository;
import com.wiley.cache.core.database.repository.impl.SimpleFileSystemRepository;
import com.wiley.cache.core.database.service.DataBaseService;
import com.wiley.cache.core.database.service.impl.SimpleFileSystemDataBaseService;
import com.wiley.cache.core.cache.impl.filesystem.FileSystemCacheImpl;
import com.wiley.cache.core.cache.impl.memory.MemoryCacheImpl;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
//       String pathToMFT = "C:/Users/Adminisnt-2/Desktop/Новая папка (6)/MFT.json";
//        String pathToCache = "C:/Users/Adminisnt-2/Desktop/Новая папка (6)/";
//        DataBaseRepository dataBaseRepository = new SimpleFileSystemRepository<String, String>(pathToCache, pathToMFT);
//        DataBaseService dataBaseService = new SimpleFileSystemDataBaseService<String, String>(dataBaseRepository);
//        Cache cache1 = new MemoryCacheImpl(2);
//        Cache cache2 = new FileSystemCacheImpl(dataBaseService,2);
//        AbstractCacheController cacheController = new LFUCacheController<String, String>(Arrays.asList(cache1, cache2));
//
//        cacheController.cacheObject(pathToCache, pathToMFT);
//        cacheController.cacheObject("123", "12312");
//        cacheController.cacheObject("1231231", "123123123");
//
//        String str = (String)cacheController.findObject("1231231");
    }
}
