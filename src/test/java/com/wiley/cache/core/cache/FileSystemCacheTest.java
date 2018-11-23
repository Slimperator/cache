package com.wiley.cache.core.cache;

import com.wiley.cache.core.cache.impl.filesystem.FileSystemCacheImpl;
import com.wiley.cache.core.database.repository.DataBaseRepository;
import com.wiley.cache.core.database.repository.impl.SimpleFileSystemRepository;
import com.wiley.cache.core.database.service.DataBaseService;
import com.wiley.cache.core.database.service.impl.SimpleFileSystemDataBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FileSystemCacheTest {
    final private int cacheSize = 10;
    final private String key = "Key";
    final private String value = "Value";
    final private String testPath = "/test";
    final private String testPathMFT = "/testMFT.json";

    @Mock
    final private DataBaseRepository<String, String> dataBaseRepository = new SimpleFileSystemRepository<>(testPath, testPathMFT);
    @Mock
    final private DataBaseService<String, String> dataBaseService = new SimpleFileSystemDataBaseService<>(dataBaseRepository);
    @InjectMocks
    final private Cache<String, String> fileSystemCacheImpl = new FileSystemCacheImpl<>(dataBaseService, cacheSize);

    @Test
    public void putTest() {
        fileSystemCacheImpl.put(key, value);
        verify(dataBaseService, times(1)).put(key, value);
    }

    @Test
    public void getTest() {
        fileSystemCacheImpl.get(key);
        verify(dataBaseService, times(1)).get(key);
    }

    @Test
    public void cutTest() {
        fileSystemCacheImpl.cut(key);
        verify(dataBaseService, times(1)).get(key);
        verify(dataBaseService, times(1)).delete(key);
    }

    @Test
    public void updateTest() {
        fileSystemCacheImpl.update(key, value);
        verify(dataBaseService, times(1)).update(key, value);
    }

    @Test
    public void deleteTest() {
        fileSystemCacheImpl.delete(key);
        verify(dataBaseService, times(1)).delete(key);
    }

    @Test
    public void clearTest() {
        fileSystemCacheImpl.clear();
        verify(dataBaseService, times(1)).clear();
    }

    @Test
    public void isContainTest() {
        fileSystemCacheImpl.isContain(key);
        verify(dataBaseService, times(1)).isContain(key);
    }

    @Test
    public void getEmptySpaceTest() {
        fileSystemCacheImpl.getEmptySpace();
        verify(dataBaseService, times(1)).getSize();
    }
}
