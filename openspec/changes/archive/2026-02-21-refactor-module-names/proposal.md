## Why

当前项目使用 `all-in-one` 和 `compatibility-tests` 作为模块名称，存在以下问题：
1. `all-in-one` 命名不够专业，且未来会包含多个云厂商的 OSS 适配器（S3、阿里云 OSS、腾讯云 COS 等），需要更准确地表达"发行版/分发包"的概念
2. `compatibility-tests` 实际上包含了集成测试和兼容性测试，使用 Maven 标准命名 `integration-tests` 更符合生态习惯

采用更专业和规范的命名可以提升项目的可维护性和可理解性。

## What Changes

- 将 `all-in-one` 目录重命名为 `distributions`，更准确地表达"发行版打包"的用途
- 将 `compatibility-tests` 目录重命名为 `integration-tests`，符合 Maven 标准命名约定
- 更新所有引用这两个模块的配置文件和文档
- 更新 Maven 模块的 artifactId 以保持一致性

## Capabilities

### New Capabilities

无新增能力。

### Modified Capabilities

无现有能力的需求变更。此变更仅涉及模块重命名，不改变功能行为。

## Impact

**受影响的文件和系统：**

- **Maven 配置**
  - 根目录 `pom.xml` 的 `<modules>` 部分
  - `all-in-one/pom.xml` 的 `<artifactId>` 和 `<name>`
  - `compatibility-tests/pom.xml` 的 `<artifactId>` 和 `<name>`
  - `compatibility-tests/pom.xml` 中引用 `all-in-one-test` 的模块路径

- **文档**
  - `README.md` 中所有引用这两个目录的路径和说明
  - 可能的其他 markdown 文档

- **CI/CD**
  - GitHub Actions 工作流配置（如果有引用这些目录）
  - 构建脚本（`scripts/` 目录下的脚本）

- **开发工具配置**
  - IDE 项目配置可能需要刷新

**影响范围：**
- 不影响运行时行为
- 不影响已发布的 artifact（groupId 和核心 artifactId 保持不变）
- 仅影响项目结构和构建配置
- 开发者需要重新导入 Maven 项目
