package com.wiley.cache.core.cache;

import com.wiley.cache.core.cache.impl.memory.MemoryCacheImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class MemoryCacheTest {
    final private int memoryCacheSize = 10;
    final private String key = "Key";
    final private String value = "Value";
    @InjectMocks
    final private MemoryCacheImpl<String, String> memoryCache = new MemoryCacheImpl<>(memoryCacheSize);

    @Test
    public void putTest() {
        memoryCache.put(key, value);
        assertThat(memoryCache.get(key), is(value));
    }

    @Test
    public void getTest() {
        memoryCache.put(key, value);
        assertThat(memoryCache.get(key), is(value));
    }

    @Test
    public void cutTest() {
        memoryCache.put(key, value);
        assertThat(memoryCache.cut(key), is(value));
        assertThat(memoryCache.cut(key), is(nullValue()));
    }

    @Test
    public void updateTest() {
        String newValue = "new";
        memoryCache.put(key, value);
        memoryCache.update(key, newValue);
        assertThat(memoryCache.get(key), is(newValue));
    }

    @Test
    public void deleteTest() {
        memoryCache.put(key, value);
        memoryCache.delete(key);
        assertThat(memoryCache.get(key), is(nullValue()));
    }

    @Test
    public void clearTest() {
        memoryCache.put(key, value);
        memoryCache.clear();
        assertThat(memoryCache.get(key), is(nullValue()));
    }

    @Test
    public void isContainTest() {
        memoryCache.put(key, value);
        assertThat(memoryCache.isContain(key), is(true));
    }

    @Test
    public void getEmptySpaceTest() {
        memoryCache.put(key, value);
        assertThat(memoryCache.getEmptySpace(), is(memoryCacheSize - 1));
    }
}
