package org.logx.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.logx.config.properties.LogxOssProperties;

class LogxOssConfigResolverTest {

    @AfterEach
    void tearDown() {
        System.clearProperty("logx.oss.engine.payloadMaxBytes");
        System.clearProperty("logx.oss.engine.queue.capacity");
        System.clearProperty("logx.oss.engine.batch.bytes");
        System.clearProperty("logx.oss.engine.maxUploadSizeMb");
        System.clearProperty("logx.oss.engine.queue.fullTimeoutMs");
    }

    @Test
    @DisplayName("应该将payloadMaxBytes配置注入到Engine属性")
    void shouldInjectPayloadMaxBytesIntoEngineProperties() {
        System.setProperty("logx.oss.engine.payloadMaxBytes", "4096");

        ConfigManager configManager = new ConfigManager();
        LogxOssProperties properties = configManager.getLogxOssProperties();

        assertThat(properties.getEngine().getPayloadMaxBytes()).isEqualTo(4096);
    }

    @Test
    @DisplayName("应该在关键容量参数越界时快速失败")
    void shouldFailFastWhenEngineLimitsOutOfRange() {
        System.setProperty("logx.oss.engine.queue.capacity", "100");

        assertThatThrownBy(() -> new ConfigManager().getLogxOssProperties())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("logx.oss.engine.queue.capacity out of range");
    }

    @Test
    @DisplayName("应该解析队列满等待超时配置")
    void shouldResolveQueueFullTimeoutMs() {
        System.setProperty("logx.oss.engine.queue.fullTimeoutMs", "1200");

        ConfigManager configManager = new ConfigManager();
        LogxOssProperties properties = configManager.getLogxOssProperties();

        assertThat(properties.getEngine().getQueueFullTimeoutMs()).isEqualTo(1200L);
    }
}
