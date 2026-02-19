package org.logx.core;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 日志载荷清洗工具：
 * - 去除除换行/制表外的控制字符
 * - 限制最大字节数，超限截断
 * - 返回清洗结果并在需要时可记录告警
 */
public final class LogPayloadSanitizer {

    private static final AtomicLong sanitizedCount = new AtomicLong(0);
    private static final AtomicLong truncatedCount = new AtomicLong(0);

    private LogPayloadSanitizer() {
    }

    public static SanitizedPayload sanitize(String input, int maxBytes) {
        if (input == null) {
            return new SanitizedPayload(new byte[0], false, false, 0);
        }

        if (maxBytes <= 0) {
            return new SanitizedPayload(new byte[0], false, input.length() > 0, 0);
        }

        byte[] source = input.getBytes(StandardCharsets.UTF_8);
        return sanitize(source, maxBytes);
    }

    public static SanitizedPayload sanitize(byte[] input, int maxBytes) {
        if (input == null || input.length == 0) {
            return new SanitizedPayload(new byte[0], false, false, 0);
        }

        if (maxBytes <= 0) {
            return new SanitizedPayload(new byte[0], false, true, input.length);
        }

        byte[] output = new byte[Math.min(maxBytes, input.length)];
        int writeIndex = 0;
        int originalBytes = input.length;
        boolean sanitized = false;
        boolean truncated = false;

        for (int i = 0; i < input.length; i++) {
            int unsigned = input[i] & 0xFF;
            boolean control = unsigned < 0x20
                    && unsigned != '\n'
                    && unsigned != '\r'
                    && unsigned != '\t';
            if (control) {
                sanitized = true;
                continue;
            }

            if (writeIndex >= maxBytes) {
                truncated = true;
                break;
            }
            output[writeIndex] = input[i];
            writeIndex++;
        }

        byte[] result = Arrays.copyOf(output, writeIndex);
        if (sanitized) {
            sanitizedCount.incrementAndGet();
        }
        if (truncated) {
            truncatedCount.incrementAndGet();
        }

        return new SanitizedPayload(result, sanitized, truncated, originalBytes);
    }

    public static final class SanitizedPayload {
        public final byte[] bytes;
        public final boolean sanitized;
        public final boolean truncated;
        public final int originalBytes;

        SanitizedPayload(byte[] bytes, boolean sanitized, boolean truncated, int originalBytes) {
            this.bytes = bytes;
            this.sanitized = sanitized;
            this.truncated = truncated;
            this.originalBytes = originalBytes;
        }
    }
}
