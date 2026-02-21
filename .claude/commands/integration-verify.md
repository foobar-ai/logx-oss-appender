---
description: 一键验证全量集成测试链路（严格全量，含 MinIO 与 jdk21-test）
---

执行仓库的全量集成验证（推荐入口）。

> 与 `/minio-verify` 等价，作为语义化主入口。

## 输入

`/integration-verify [quick|full]`

- 默认 `full`
- `quick`：MinIO 可用性 + `MinIOIntegrationTest`
- `full`：在 `quick` 基础上追加 `integration-tests/test-runner` 与 `jdk21-test`

## 环境说明

- 项目默认 Java 8（sdkman `.sdkmanrc`），Docker 必须可用
- MinIO：脚本自动检测，未运行时自动 `docker-compose up -d` 拉起
- jdk21-test：默认走 `docker run maven:3.9.6-eclipse-temurin-21`；本地手动 `sdk use java 21` 后可跳过 Docker
- CI（GitHub Actions ubuntu-latest）与本地行为一致

## 执行命令

```bash
bash scripts/integration-verify.sh <mode>
```

## 成功判定

- `quick`：MinIO 可用且 `MinIOIntegrationTest` 通过
- `full`：MinIO 可用且 `test-runner`、`jdk21-test` 全通过
- 任一必选步骤失败或缺失：FAIL

## 输出要求

脚本会输出标准化摘要并写入：

`integration-tests/target/integration-verify/summary-<timestamp>.md`
