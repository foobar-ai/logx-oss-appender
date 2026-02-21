## Context

本次变更需要修改 integration-tests 中的日志级别使用方式，以及 GitHub Actions 工作流的配置。这是一个简单的代码修改任务，不涉及复杂的架构或外部依赖变更。

## Goals / Non-Goals

**Goals:**
1. 将包含"测试日志"或"测试错误日志"字样的 error 级别日志改为 warn 或 info 级别
2. 将 GitHub Actions 中的 `if-no-files-found: warn` 改为 `if-no-files-found: error`

**Non-Goals:**
- 不修改业务逻辑代码，只修改日志级别
- 不添加新功能
- 不修改测试框架或测试结构

## Decisions

1. **日志级别选择**：包含"测试日志"的改为 info 级别，包含"测试错误日志"的改为 warn 级别
2. **GitHub Actions 配置**：将 `if-no-files-found: warn` 改为 `if-no-files-found: error` 以确保测试失败时能正确检测

## Risks / Trade-offs

[Risk] 遗漏某些文件 → [Mitigation] 使用 grep 全面搜索验证
[Risk] 误改业务日志（非测试日志）→ [Mitigation] 只修改明确包含"测试日志"或"测试错误日志"的行
