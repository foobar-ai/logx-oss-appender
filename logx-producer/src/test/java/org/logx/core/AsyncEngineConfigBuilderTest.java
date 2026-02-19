package org.logx.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AsyncEngineConfigBuilderTest {

    @AfterEach
    void tearDown() {
        System.clearProperty("logx.oss.engine.queue.fullTimeoutMs");
    }

    @Test
    @DisplayName("应该从配置读取队列满等待超时")
    void shouldLoadQueueFullTimeoutMsFromConfig() {
        System.setProperty("logx.oss.engine.queue.fullTimeoutMs", "1500");

        AsyncEngineConfig config = AsyncEngineConfigBuilder.buildConfig();

        assertThat(config.getQueueFullTimeoutMs()).isEqualTo(1500L);
    }
}
