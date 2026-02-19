package org.logx.core;

public class AsyncEngineConfig {

    public static final int MIN_QUEUE_CAPACITY = 1024;
    public static final int MAX_QUEUE_CAPACITY = 1_048_576;
    public static final int MIN_BATCH_MAX_BYTES = 64 * 1024;
    public static final int MAX_BATCH_MAX_BYTES = 16 * 1024 * 1024;
    public static final int MIN_PAYLOAD_MAX_BYTES = 1024;
    public static final int MAX_PAYLOAD_MAX_BYTES = 1024 * 1024;
    public static final int MIN_MAX_UPLOAD_SIZE_MB = 1;
    public static final int MAX_MAX_UPLOAD_SIZE_MB = 64;
    public static final long DEFAULT_QUEUE_FULL_TIMEOUT_MS = 5000L;

    public enum OversizePayloadPolicy {
        DROP,
        FALLBACK_FILE
    }

    private boolean enabled = true;
    private int queueCapacity = 524288;
    private int batchMaxMessages = 8192;
    private int batchMaxBytes = 10 * 1024 * 1024;
    private long maxMessageAgeMs = 60000L;
    private boolean blockOnFull = false;
    private boolean multiProducer = false;
    private int corePoolSize = 1;
    private int maximumPoolSize = 1;
    private int queueCapacityThreadPool = 500;
    private boolean enableCpuYield = true;
    private boolean enableMemoryProtection = true;
    private long maxShutdownWaitMs = 30000L;
    private String logFilePrefix = "logx/";
    private String logFileName = "applogx";
    private int fallbackRetentionDays = 7;
    private int fallbackScanIntervalSeconds = 60;
    private long fallbackMaxRetryFileBytes = 10L * 1024 * 1024;
    private int fallbackMaxRetryFilesPerRound = 100;
    private long fallbackMaxRetryBytesPerRound = 50L * 1024 * 1024;
    private int emergencyMemoryThresholdMb = 512;
    private int parallelUploadThreads = 2;
    private long uploadTimeoutMs = 30000L;
    private boolean enableDynamicBatching = true;
    private long queuePressureMonitorIntervalMs = 1000;
    private double highPressureThreshold = 0.8;
    private double lowPressureThreshold = 0.3;
    private int payloadMaxBytes = 512 * 1024;
    private OversizePayloadPolicy oversizePayloadPolicy = OversizePayloadPolicy.DROP;
    private int oversizeFallbackMaxBytes = 10 * 1024 * 1024;
    private long queueFullTimeoutMs = DEFAULT_QUEUE_FULL_TIMEOUT_MS;

    public static AsyncEngineConfig defaultConfig() {
        return new AsyncEngineConfig();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public AsyncEngineConfig enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public AsyncEngineConfig queueCapacity(int queueCapacity) {
        this.queueCapacity = requireRange("logx.oss.engine.queue.capacity", queueCapacity,
                MIN_QUEUE_CAPACITY, MAX_QUEUE_CAPACITY);
        return this;
    }

    public int getBatchMaxMessages() {
        return batchMaxMessages;
    }

    public AsyncEngineConfig batchMaxMessages(int batchMaxMessages) {
        this.batchMaxMessages = batchMaxMessages;
        return this;
    }

    public int getBatchMaxBytes() {
        return batchMaxBytes;
    }

    public AsyncEngineConfig batchMaxBytes(int batchMaxBytes) {
        this.batchMaxBytes = requireRange("logx.oss.engine.batch.bytes", batchMaxBytes,
                MIN_BATCH_MAX_BYTES, MAX_BATCH_MAX_BYTES);
        return this;
    }

    public long getMaxMessageAgeMs() {
        return maxMessageAgeMs;
    }

    public AsyncEngineConfig maxMessageAgeMs(long maxMessageAgeMs) {
        this.maxMessageAgeMs = maxMessageAgeMs;
        return this;
    }

    public boolean isBlockOnFull() {
        return blockOnFull;
    }

    public AsyncEngineConfig blockOnFull(boolean blockOnFull) {
        this.blockOnFull = blockOnFull;
        return this;
    }

    public boolean isMultiProducer() {
        return multiProducer;
    }

    public AsyncEngineConfig multiProducer(boolean multiProducer) {
        this.multiProducer = multiProducer;
        return this;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public AsyncEngineConfig corePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public AsyncEngineConfig maximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public int getQueueCapacityThreadPool() {
        return queueCapacityThreadPool;
    }

    public AsyncEngineConfig queueCapacityThreadPool(int queueCapacityThreadPool) {
        this.queueCapacityThreadPool = queueCapacityThreadPool;
        return this;
    }

    public boolean isEnableCpuYield() {
        return enableCpuYield;
    }

    public AsyncEngineConfig enableCpuYield(boolean enableCpuYield) {
        this.enableCpuYield = enableCpuYield;
        return this;
    }

    public boolean isEnableMemoryProtection() {
        return enableMemoryProtection;
    }

    public AsyncEngineConfig enableMemoryProtection(boolean enableMemoryProtection) {
        this.enableMemoryProtection = enableMemoryProtection;
        return this;
    }

    public long getMaxShutdownWaitMs() {
        return maxShutdownWaitMs;
    }

    public AsyncEngineConfig maxShutdownWaitMs(long maxShutdownWaitMs) {
        this.maxShutdownWaitMs = maxShutdownWaitMs;
        return this;
    }

    public String getLogFilePrefix() {
        return logFilePrefix;
    }

    public AsyncEngineConfig logFilePrefix(String logFilePrefix) {
        this.logFilePrefix = logFilePrefix;
        return this;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public AsyncEngineConfig logFileName(String logFileName) {
        this.logFileName = logFileName;
        return this;
    }

    public int getFallbackRetentionDays() {
        return fallbackRetentionDays;
    }

    public AsyncEngineConfig fallbackRetentionDays(int fallbackRetentionDays) {
        this.fallbackRetentionDays = fallbackRetentionDays;
        return this;
    }

    public int getFallbackScanIntervalSeconds() {
        return fallbackScanIntervalSeconds;
    }

    public AsyncEngineConfig fallbackScanIntervalSeconds(int fallbackScanIntervalSeconds) {
        this.fallbackScanIntervalSeconds = fallbackScanIntervalSeconds;
        return this;
    }

    public long getFallbackMaxRetryFileBytes() {
        return fallbackMaxRetryFileBytes;
    }

    public AsyncEngineConfig fallbackMaxRetryFileBytes(long fallbackMaxRetryFileBytes) {
        this.fallbackMaxRetryFileBytes = Math.max(1L, fallbackMaxRetryFileBytes);
        return this;
    }

    public int getFallbackMaxRetryFilesPerRound() {
        return fallbackMaxRetryFilesPerRound;
    }

    public AsyncEngineConfig fallbackMaxRetryFilesPerRound(int fallbackMaxRetryFilesPerRound) {
        this.fallbackMaxRetryFilesPerRound = Math.max(1, fallbackMaxRetryFilesPerRound);
        return this;
    }

    public long getFallbackMaxRetryBytesPerRound() {
        return fallbackMaxRetryBytesPerRound;
    }

    public AsyncEngineConfig fallbackMaxRetryBytesPerRound(long fallbackMaxRetryBytesPerRound) {
        this.fallbackMaxRetryBytesPerRound = Math.max(1L, fallbackMaxRetryBytesPerRound);
        return this;
    }

    public int getEmergencyMemoryThresholdMb() {
        return emergencyMemoryThresholdMb;
    }

    public AsyncEngineConfig emergencyMemoryThresholdMb(int emergencyMemoryThresholdMb) {
        this.emergencyMemoryThresholdMb = emergencyMemoryThresholdMb;
        return this;
    }

    public int getParallelUploadThreads() {
        return parallelUploadThreads;
    }

    public AsyncEngineConfig parallelUploadThreads(int parallelUploadThreads) {
        this.parallelUploadThreads = Math.max(1, parallelUploadThreads);
        return this;
    }

    public long getUploadTimeoutMs() {
        return uploadTimeoutMs;
    }

    public AsyncEngineConfig uploadTimeoutMs(long uploadTimeoutMs) {
        this.uploadTimeoutMs = uploadTimeoutMs;
        return this;
    }

    public boolean isEnableDynamicBatching() {
        return enableDynamicBatching;
    }

    public AsyncEngineConfig enableDynamicBatching(boolean enableDynamicBatching) {
        this.enableDynamicBatching = enableDynamicBatching;
        return this;
    }

    public long getQueuePressureMonitorIntervalMs() {
        return queuePressureMonitorIntervalMs;
    }

    public AsyncEngineConfig queuePressureMonitorIntervalMs(long queuePressureMonitorIntervalMs) {
        this.queuePressureMonitorIntervalMs = Math.max(100, queuePressureMonitorIntervalMs);
        return this;
    }

    public double getHighPressureThreshold() {
        return highPressureThreshold;
    }

    public AsyncEngineConfig highPressureThreshold(double highPressureThreshold) {
        this.highPressureThreshold = Math.max(0.1, Math.min(0.99, highPressureThreshold));
        return this;
    }

    public double getLowPressureThreshold() {
        return lowPressureThreshold;
    }

    public AsyncEngineConfig lowPressureThreshold(double lowPressureThreshold) {
        this.lowPressureThreshold = Math.max(0.01, Math.min(0.9, lowPressureThreshold));
        return this;
    }

    public int getPayloadMaxBytes() {
        return payloadMaxBytes;
    }

    public AsyncEngineConfig payloadMaxBytes(int payloadMaxBytes) {
        this.payloadMaxBytes = requireRange("logx.oss.engine.payloadMaxBytes", payloadMaxBytes,
                MIN_PAYLOAD_MAX_BYTES, MAX_PAYLOAD_MAX_BYTES);
        return this;
    }

    public OversizePayloadPolicy getOversizePayloadPolicy() {
        return oversizePayloadPolicy;
    }

    public AsyncEngineConfig oversizePayloadPolicy(OversizePayloadPolicy oversizePayloadPolicy) {
        if (oversizePayloadPolicy == null) {
            this.oversizePayloadPolicy = OversizePayloadPolicy.DROP;
        } else {
            this.oversizePayloadPolicy = oversizePayloadPolicy;
        }
        return this;
    }

    public int getOversizeFallbackMaxBytes() {
        return oversizeFallbackMaxBytes;
    }

    public AsyncEngineConfig oversizeFallbackMaxBytes(int oversizeFallbackMaxBytes) {
        this.oversizeFallbackMaxBytes = Math.max(1, oversizeFallbackMaxBytes);
        return this;
    }

    public long getQueueFullTimeoutMs() {
        return queueFullTimeoutMs;
    }

    public AsyncEngineConfig queueFullTimeoutMs(long queueFullTimeoutMs) {
        this.queueFullTimeoutMs = Math.max(1L, queueFullTimeoutMs);
        return this;
    }

    private org.logx.storage.StorageConfig storageConfig;

    public org.logx.storage.StorageConfig getStorageConfig() {
        return storageConfig;
    }

    public void setStorageConfig(org.logx.storage.StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    private static int requireRange(String key, int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    key + " out of range: " + value + ", expected [" + min + ", " + max + "]");
        }
        return value;
    }
}
