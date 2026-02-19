package org.logx.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogPayloadSanitizerTest {

    @Test
    @DisplayName("sanitize(byte[]) 应去除控制字符并保留换行制表")
    void shouldRemoveControlCharactersWhenSanitizeBytes() {
        byte[] input = new byte[] { 'a', 0x00, 'b', '\n', '\t', 0x1F, 'c' };

        LogPayloadSanitizer.SanitizedPayload result = LogPayloadSanitizer.sanitize(input, 32);

        assertThat(result.sanitized).isTrue();
        assertThat(result.truncated).isFalse();
        assertThat(result.originalBytes).isEqualTo(input.length);
        assertThat(result.bytes).containsExactly((byte) 'a', (byte) 'b', (byte) '\n', (byte) '\t', (byte) 'c');
    }

    @Test
    @DisplayName("sanitize(byte[]) 达到字节上限应截断")
    void shouldTruncateWhenExceedMaxBytesInByteSanitization() {
        byte[] input = "abcdef".getBytes(StandardCharsets.UTF_8);

        LogPayloadSanitizer.SanitizedPayload result = LogPayloadSanitizer.sanitize(input, 3);

        assertThat(result.truncated).isTrue();
        assertThat(result.originalBytes).isEqualTo(input.length);
        assertThat(new String(result.bytes, StandardCharsets.UTF_8)).isEqualTo("abc");
    }

    @Test
    @DisplayName("sanitize(String) 应沿用字节级清洗逻辑")
    void shouldApplyByteSanitizationForStringInput() {
        String input = "abc\u0000xyz";

        LogPayloadSanitizer.SanitizedPayload result = LogPayloadSanitizer.sanitize(input, 4);

        assertThat(result.sanitized).isTrue();
        assertThat(result.truncated).isTrue();
        assertThat(new String(result.bytes, StandardCharsets.UTF_8)).isEqualTo("abcx");
    }
}
