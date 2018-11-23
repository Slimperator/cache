package com.wiley.cache.core.database;

import com.wiley.cache.core.database.repository.DataBaseRepository;
import com.wiley.cache.core.database.repository.impl.SimpleFileSystemRepository;
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
public class SimpleFileSystemDataBaseServiceTest {
    final private int cacheSize = 10;
    final private String key = "Key";
    final private String value = "Value";
    final private String testPath = "/test";
    final private String testPathMFT = "/testMFT.json";

    @Mock
    final private DataBaseRepository<String, String> dataBaseRepository = new SimpleFileSystemRepository<>(testPath, testPathMFT);
    @InjectMocks
    final private SimpleFileSystemDataBaseService<String, String> dataBaseService = new SimpleFileSystemDataBaseService<>(dataBaseRepository);

    @Test
    public void putTest() {
        dataBaseService.put(key, value);
        verify(dataBaseRepository, times(1)).put(key, value);
    }

    @Test
    public void getTest() {
        dataBaseService.get(key);
        verify(dataBaseRepository, times(1)).get(key);
    }

    @Test
    public void updateTest() {
        dataBaseService.update(key, value);
        verify(dataBaseRepository, times(1)).update(key, value);
    }

    @Test
    public void deleteTest() {
        dataBaseService.delete(key);
        verify(dataBaseRepository, times(1)).delete(key);
    }

    @Test
    public void clearTest() {
        dataBaseService.clear();
        verify(dataBaseRepository, times(1)).clear();
    }

    @Test
    public void isContainTest() {
        dataBaseService.isContain(key);
        verify(dataBaseRepository, times(1)).isContain(key);
    }

    @Test
    public void getSizeTest() {
        dataBaseService.getSize();
        verify(dataBaseRepository, times(1)).getSize();
    }
}
