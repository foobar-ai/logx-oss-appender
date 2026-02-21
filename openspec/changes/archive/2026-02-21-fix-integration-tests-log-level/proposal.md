## Why

integration-tests 中存在大量使用 error 级别日志输出"测试日志"和"测试错误日志"的代码，这种做法不符合日志规范。测试日志应该是 info 或 warn 级别，error 级别应该保留给真正的错误。此外，GitHub Actions 配置中 `if-no-files-found: warn` 应改为 `error` 以确保测试失败时能正确检测。

## What Changes

1. 将 integration-tests 中所有包含"测试日志"、"测试错误日志"的 error 级别日志调用改为 warn 或 info 级别
2. 修改 `.github/workflows/integration-verify-gate.yml` 中的 `if-no-files-found: warn` 为 `if-no-files-found: error`

## Capabilities

### Modified Capabilities
- `real-minio-test-gate`: 修改 GitHub Actions 配置以严格检查测试结果

## Impact

- 修改文件范围：
  - `integration-tests/jsp-servlet-test/`
  - `integration-tests/multi-framework-test/`
  - `integration-tests/spring-boot-test/`
  - `integration-tests/spring-mvc-test/`
  - `integration-tests/jdk21-test/`
  - `integration-tests/distribution-test/`
  - `.github/workflows/integration-verify-gate.yml`
