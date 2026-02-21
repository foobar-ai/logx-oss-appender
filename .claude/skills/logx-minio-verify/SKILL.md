---
name: logx-minio-verify
description: 代码编写完成后执行全量集成验证（严格全量：MinIO + integration-tests + jdk21-test）
license: MIT
compatibility: Requires Java 8 + Maven + Docker (including docker-compose).
metadata:
  author: logx-project
  version: "1.1"
---

## 目的

统一"编码完成后的验证门禁"。

**硬规则**：只要涉及代码改动，完成实现后必须执行本技能；未通过则视为变更未完成。

## 环境前提

- **Java**：项目默认 Java 8（sdkman `.sdkmanrc: java=8.0.462.fx-zulu`）
- **Maven**：需在 PATH 中可用
- **Docker + docker-compose**：必须可用，脚本依赖其自动拉起 MinIO 和 jdk21-test 容器

注意：脚本通过 `bash -lc` 执行命令，不会自动 source sdkman 初始化脚本，`java_major()` 会正确检测到 Java 8。

## 执行机制

### MinIO

脚本自动检测 MinIO 是否运行（`http://localhost:9000/minio/health/live`）：
- 已运行：直接使用
- 未运行：自动通过 `docker-compose up -d` 拉起（`integration-tests/minio/docker`）

### jdk21-test

脚本检测当前 Java 版本：
- **Java < 21（默认）**：自动使用 `docker run maven:3.9.6-eclipse-temurin-21` 执行，无需手动操作
- **Java >= 21（手动 `sdk use java 21`）**：走本地直接执行（加速选项，非默认路径）

本地和 CI 行为完全一致，均依赖 Docker。

## 输入

- `quick`：快速验证（MinIO 健康 + `MinIOIntegrationTest`）
- `full`：严格全量验证（默认，必须用于最终验收）

## 执行

```bash
bash scripts/integration-verify.sh <quick|full>
```

建议：
- 开发中可先跑 `quick`
- 提交前/合并前必须跑 `full`

## 通过标准

- `quick`：MinIO 可用 + `MinIOIntegrationTest` 通过
- `full`：
  - MinIO 前置检查通过（endpoint、凭据、桶）
  - `integration-tests/test-runner` 通过
  - `jdk21-test` 覆盖并通过（Docker 容器执行）

任一失败即 FAIL。

## GitHub Actions

ubuntu-latest runner 自带 Docker，行为与本地完全一致，脚本自动处理 MinIO 和 jdk21-test：

```yaml
jobs:
  integration-verify:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '8'
          distribution: 'zulu'
      - name: Run integration verify
        run: bash scripts/integration-verify.sh full
```

## 输出产物

脚本会写入：

`integration-tests/target/integration-verify/summary-<timestamp>.md`

并同时生成详细日志：

`integration-tests/target/integration-verify/run-<timestamp>.log`

## 失败处置

优先查看失败模板中的：
1. 失败阶段
2. 失败命令
3. 建议动作

然后根据 summary/report 路径回溯日志定位。
