package org.logx.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.logx.fallback.FallbackManager;
import org.logx.storage.ProtocolType;
import org.logx.storage.StorageService;
import org.mockito.Mockito;

class AsyncEngineImplTest {

    @Test
    @DisplayName("当payload大小等于上限时应该允许入队")
    void shouldSubmitWhenPayloadEqualsLimit() throws Exception {
        AsyncEngineConfig config = AsyncEngineConfig.defaultConfig()
                .queueCapacity(1024)
                .batchMaxMessages(1024)
                .maxMessageAgeMs(60_000L)
                .payloadMaxBytes(1024)
                .oversizePayloadPolicy(AsyncEngineConfig.OversizePayloadPolicy.DROP);
        StorageService storageService = mockStorageService();
        FallbackManager fallbackManager = Mockito.mock(FallbackManager.class);

        AsyncEngineImpl engine = new AsyncEngineImpl(config, storageService, null, fallbackManager);
        engine.start();
        try {
            byte[] data = new byte[1024];
            engine.put(data);

            assertThat(readCurrentMemoryUsage(engine)).isGreaterThan(0L);
            verify(fallbackManager, never()).writeFallbackFile(any(byte[].class));
        } finally {
            engine.stop(1, TimeUnit.SECONDS);
        }
    }

    @Test
    @DisplayName("当payload超过上限且策略为DROP时应该拒绝入队")
    void shouldRejectWhenPayloadExceedsLimitWithDropPolicy() throws Exception {
        AsyncEngineConfig config = AsyncEngineConfig.defaultConfig()
                .queueCapacity(1024)
                .batchMaxMessages(1024)
                .maxMessageAgeMs(60_000L)
                .payloadMaxBytes(1024)
                .oversizePayloadPolicy(AsyncEngineConfig.OversizePayloadPolicy.DROP);
        StorageService storageService = mockStorageService();
        FallbackManager fallbackManager = Mockito.mock(FallbackManager.class);

        AsyncEngineImpl engine = new AsyncEngineImpl(config, storageService, null, fallbackManager);
        engine.start();
        try {
            byte[] data = new byte[1025];
            engine.put(data);

            assertThat(readCurrentMemoryUsage(engine)).isZero();
            verify(fallbackManager, never()).writeFallbackFile(any(byte[].class));
        } finally {
            engine.stop(1, TimeUnit.SECONDS);
        }
    }

    @Test
    @DisplayName("当payload极大且超过兜底文件限制时应该拒绝入队")
    void shouldRejectHugePayloadWithoutQueueSubmit() throws Exception {
        AsyncEngineConfig config = AsyncEngineConfig.defaultConfig()
                .queueCapacity(1024)
                .batchMaxMessages(1024)
                .maxMessageAgeMs(60_000L)
                .payloadMaxBytes(1024 * 1024)
                .oversizePayloadPolicy(AsyncEngineConfig.OversizePayloadPolicy.FALLBACK_FILE)
                .oversizeFallbackMaxBytes(5 * 1024 * 1024);
        StorageService storageService = mockStorageService();
        FallbackManager fallbackManager = Mockito.mock(FallbackManager.class);

        AsyncEngineImpl engine = new AsyncEngineImpl(config, storageService, null, fallbackManager);
        engine.start();
        try {
            byte[] data = new byte[11 * 1024 * 1024];
            engine.put(data);

            assertThat(readCurrentMemoryUsage(engine)).isZero();
            verify(fallbackManager, never()).writeFallbackFile(any(byte[].class));
        } finally {
            engine.stop(1, TimeUnit.SECONDS);
        }
    }

    private StorageService mockStorageService() {
        StorageService storageService = Mockito.mock(StorageService.class);
        when(storageService.getKeyPrefix()).thenReturn("test-prefix");
        when(storageService.getProtocolType()).thenReturn(ProtocolType.S3);
        when(storageService.getBucketName()).thenReturn("test-bucket");
        when(storageService.supportsProtocol(ProtocolType.S3)).thenReturn(true);
        when(storageService.putObject(any(String.class), any(byte[].class))).thenReturn(CompletableFuture.completedFuture(null));
        return storageService;
    }

    private long readCurrentMemoryUsage(AsyncEngineImpl engine) throws Exception {
        Field field = AsyncEngineImpl.class.getDeclaredField("currentMemoryUsage");
        field.setAccessible(true);
        AtomicLong memory = (AtomicLong) field.get(engine);
        return memory.get();
    }
}
