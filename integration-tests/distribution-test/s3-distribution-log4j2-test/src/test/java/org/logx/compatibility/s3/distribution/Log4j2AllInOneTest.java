package org.logx.compatibility.s3.distribution;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log4j2 All-in-One JAR兼容性测试
 *
 * 测试前请按照 compatibility-tests/minio/README-MINIO.md 指南启动MinIO服务
 *
 * 快速启动：
 * cd compatibility-tests/minio
 * ./start-minio-local.sh
 *
 * 标准配置：
 * - 端点: http://localhost:9000
 * - 控制台: http://localhost:9001
 * - 用户名/密码: minioadmin/minioadmin
 * - 测试桶: logx-test-bucket
 */
@SpringBootTest
public class Log4j2AllInOneTest {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2AllInOneTest.class);

    @Test
    public void testOSSConnectionAndLogging() throws Exception {
        logger.info("开始Log4j2 All-in-One JAR兼容性测试...");

        // 生成测试日志
        logger.info("=== 生成测试日志 ===");
        for (int i = 1; i <= 20; i++) {
            logger.info("All-in-One测试日志 #{} - 测试时间: {}, 内容: 这是一条用于验证All-in-One JAR功能的测试日志",
                       i, System.currentTimeMillis());

            if (i % 5 == 0) {
                logger.warn("All-in-One警告日志 #{} - 这是一条WARN级别的测试日志", i);
            }

            if (i % 10 == 0) {
                logger.error("All-in-One错误日志 #{} - 这是一条ERROR级别的测试日志", i);
            }

            // 短暂延迟
            Thread.sleep(100);
        }

        // 等待日志处理
        logger.info("=== 等待日志处理和上传 ===");
        logger.info("等待10秒让日志系统处理和上传日志...");
        Thread.sleep(10000);

        // 生成更多日志来触发批处理
        logger.info("=== 触发批处理上传 ===");
        for (int i = 1; i <= 100; i++) {
            logger.info("批处理触发日志 #{} - 时间戳: {}", i, System.currentTimeMillis());
            if (i % 10 == 0) {
                Thread.sleep(50); // 短暂暂停
            }
        }

        logger.info("=== 最终等待和验证 ===");
        logger.info("等待15秒确保所有日志被处理和上传...");
        Thread.sleep(15000);

        logger.info("Log4j2 All-in-One JAR兼容性测试完成！");
        logger.info("请检查MinIO控制台: http://localhost:9001");
        logger.info("查看桶: logx-test-bucket");
        logger.info("查看路径: logx/");
    }

    @Test
    public void testPerformanceStressLogsGeneration() throws Exception {
        logger.info("🚀 开始Log4j2 All-in-One性能压力测试");

        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        logger.info("=== 系统初始状态 ===");
        logger.info("初始内存使用: {} MB", String.format("%.2f", initialMemory / 1024.0 / 1024.0));
        logger.info("目标配置: maxBatchCount=8192, maxBatchBytes=10MB, maxMessageAgeMs=60s, emergencyThreshold=512MB");

        // 等待系统初始化
        Thread.sleep(2000);

        // 1. 测试消息数量触发条件 (8192条消息)
        logger.info("\n=== 测试1: 消息数量触发条件 (8192条) ===");
        long test1Start = System.currentTimeMillis();

        logger.info("生成8500条日志测试消息数量触发...");
        for (int i = 0; i < 8500; i++) {
            logger.info("数量触发测试 #{} - 消息内容: 订单ID-{}, 时间: {}",
                       i + 1, String.format("ORD%06d", i), System.currentTimeMillis());

            // 控制生成速度，避免过快触发其他条件
            if (i % 1000 == 0) {
                Thread.sleep(50);
            }
        }

        long test1Duration = System.currentTimeMillis() - test1Start;
        logger.info("✅ 消息数量触发测试完成 - 耗时: {}ms", test1Duration);

        // 等待上传
        Thread.sleep(3000);

        // 2. 测试字节数触发条件 (10MB)
        logger.info("\n=== 测试2: 字节数触发条件 (10MB) ===");
        long test2Start = System.currentTimeMillis();

        // 生成大消息以快速达到10MB
        StringBuilder sb = new StringBuilder(100 * 1024);
        for (int j = 0; j < 100 * 1024; j++) {
            sb.append("X");
        }
        String largeMessage = sb.toString(); // 100KB消息
        int messagesFor10MB = 105; // 略超过10MB

        logger.info("单条消息大小约 {} KB，生成 {} 条大消息测试字节数触发...",
                    100, messagesFor10MB);

        for (int i = 0; i < messagesFor10MB; i++) {
            logger.info("字节数触发测试 #{} - 大消息: {}", i + 1, largeMessage);

            if (i % 20 == 0) {
                Thread.sleep(50);
            }
        }

        long test2Duration = System.currentTimeMillis() - test2Start;
        logger.info("✅ 字节数触发测试完成 - 耗时: {}ms", test2Duration);

        // 等待上传
        Thread.sleep(3000);

        // 3. 测试消息年龄触发条件 (60秒)
        logger.info("\n=== 测试3: 消息年龄触发条件 (60秒) ===");
        long test3Start = System.currentTimeMillis();

        // 生成少量消息，然后等待60秒触发年龄条件
        logger.info("生成100条消息，然后等待60秒测试年龄触发...");
        for (int i = 0; i < 100; i++) {
            logger.info("年龄触发测试 #{} - 消息: 等待时间触发, 时间: {}",
                       i + 1, System.currentTimeMillis());
        }

        logger.info("开始等待60秒让消息年龄触发条件生效...");
        Thread.sleep(65000); // 等待65秒确保触发

        long test3Duration = System.currentTimeMillis() - test3Start;
        logger.info("✅ 消息年龄触发测试完成 - 耗时: {}ms", test3Duration);

        // 最终内存检查
        System.gc();
        Thread.sleep(1000);
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        double finalMemoryMB = (finalMemory - initialMemory) / 1024.0 / 1024.0;

        // 测试总结
        logger.info("\n=== 三个触发条件验证总结 ===");
        logger.info("✅ 测试1 - 消息数量触发 (8192条): 完成");
        logger.info("✅ 测试2 - 字节数触发 (10MB): 完成");
        logger.info("✅ 测试3 - 消息年龄触发 (60秒): 完成");
        logger.info("📊 最终队列内存使用: {} MB (限制: 512MB) - {}",
                   String.format("%.2f", finalMemoryMB), finalMemoryMB < 512 ? "✓ 合格" : "⚠ 超标");

        // 等待所有日志上传完成
        logger.info("\n等待所有日志批次上传到MinIO完成...");
        Thread.sleep(30000);

        logger.info("\n🎉 Log4j2 All-in-One触发条件验证测试完成！");
        logger.info("请检查MinIO控制台 (http://localhost:9001) 确认各批次文件上传情况");
    }
}