## Context

当前项目是一个多模块 Maven 项目，包含多个日志框架适配器（logback、log4j、log4j2）和云存储适配器（S3、未来支持阿里云 OSS、腾讯云 COS）。

**当前状态：**
- `all-in-one/` 目录包含 3 个子模块，每个子模块打包一个 Fat JAR（日志框架 + S3 适配器 + 所有依赖）
- `compatibility-tests/` 目录包含多个测试子模块（Spring Boot、Spring MVC、JSP/Servlet、多框架共存、配置一致性等）
- 两个目录都在根 `pom.xml` 中声明为 `<module>`
- `compatibility-tests/pom.xml` 中引用了 `all-in-one-test` 子模块

**约束：**
- 必须保持 Maven 构建的正确性
- 不能破坏现有的 CI/CD 流程
- 不能影响已发布的 artifact（groupId 和核心 artifactId 保持不变）
- 需要保持 Git 历史的连续性

## Goals / Non-Goals

**Goals:**
- 将 `all-in-one` 重命名为 `distributions`，更准确地表达"发行版打包"的概念
- 将 `compatibility-tests` 重命名为 `integration-tests`，符合 Maven 生态标准
- 更新所有引用这两个模块的配置文件和文档
- 确保重命名后 Maven 构建、测试、CI/CD 流程正常工作

**Non-Goals:**
- 不改变模块的功能行为
- 不修改已发布的 artifact 命名（groupId、核心 artifactId）
- 不重构模块内部的代码结构
- 不改变构建产物的输出路径（target/ 目录）

## Decisions

### 1. 使用 Git mv 保留历史

**决策：** 使用 `git mv` 命令重命名目录，而不是删除后重建。

**理由：**
- 保留 Git 历史记录，便于追溯文件变更
- Git 能够自动识别重命名操作（rename detection）
- 避免在 Git blame 中丢失历史信息

**替代方案：**
- 删除旧目录，创建新目录：会丢失 Git 历史
- 手动 mv + git add/rm：需要额外配置 Git 才能识别为重命名

### 2. 分阶段提交

**决策：** 将重命名操作分为两个独立的 commit：
1. 第一个 commit：重命名目录（`git mv`）
2. 第二个 commit：更新所有引用（pom.xml、README.md、脚本、CI/CD 配置）

**理由：**
- 清晰的提交历史，便于 code review
- 如果需要回滚，可以精确定位问题
- 符合"一个 commit 做一件事"的最佳实践

**替代方案：**
- 单个 commit 完成所有操作：提交历史不够清晰，难以追溯

### 3. Maven artifactId 更新策略

**决策：** 更新父 POM 的 `<artifactId>`，但保持子模块的 artifactId 不变（除非子模块名称也需要调整）。

**具体操作：**
- `all-in-one/pom.xml`：`<artifactId>` 从 `all-in-one` 改为 `distributions`
- `compatibility-tests/pom.xml`：`<artifactId>` 从 `integration-compatibility-tests-parent` 改为 `integration-tests-parent`
- 子模块的 artifactId 保持不变（如 `s3-log4j-oss-appender`）

**理由：**
- 父 POM 的 artifactId 通常不会被外部依赖引用
- 子模块的 artifactId 可能已经被外部项目依赖，保持不变避免破坏兼容性
- 符合 Maven 最佳实践（父 POM 主要用于依赖管理和构建配置）

### 4. 文档更新范围

**决策：** 更新以下文档和配置：
- `README.md`：所有路径引用和说明文字
- `pom.xml`（根目录）：`<modules>` 部分
- `scripts/integration-verify.sh`：脚本中的路径引用
- `.github/workflows/integration-verify-gate.yml`：CI/CD 工作流中的路径引用
- 两个模块自己的 `pom.xml`：`<artifactId>` 和 `<name>`

**理由：**
- 这些是所有直接引用这两个目录的地方
- 通过 grep 搜索确认了引用位置

## Risks / Trade-offs

### 风险 1：开发者本地环境需要更新

**风险：** 开发者拉取代码后，IDE 可能无法识别重命名后的模块。

**缓解措施：**
- 在 commit message 中明确说明需要重新导入 Maven 项目
- 在 README.md 中添加迁移说明（如果需要）
- 通知团队成员执行 `mvn clean install` 重新构建

### 风险 2：CI/CD 缓存失效

**风险：** CI/CD 系统可能缓存了旧的目录路径，导致构建失败。

**缓解措施：**
- 在合并 PR 前，清理 CI/CD 缓存
- 监控第一次构建，确保所有步骤正常执行

### 风险 3：文档引用遗漏

**风险：** 可能存在未被 grep 搜索到的引用（如注释、文档中的描述性文字）。

**缓解措施：**
- 在 PR review 时仔细检查所有文档
- 合并后监控 issue tracker，及时修复遗漏的引用

### Trade-off：artifactId 变更的影响

**Trade-off：** 父 POM 的 artifactId 变更后，如果有外部项目通过 `<parent>` 引用了这些父 POM，会导致依赖解析失败。

**评估：**
- 父 POM 通常不会被外部项目直接引用（packaging=pom 且未发布到 Maven 仓库）
- 本项目的父 POM 主要用于内部模块的依赖管理
- 风险较低

**缓解措施：**
- 如果发现有外部引用，可以考虑保留旧的 artifactId，仅更新目录名
