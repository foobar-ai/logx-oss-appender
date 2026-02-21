## 1. 修复集成测试日志级别

- [x] 1.1 修改 `integration-tests/spring-boot-test/src/test/java/.../BusinessLogGenerationTest.java`：将"OSS上传测试错误日志"的 error 调用改为 warn
- [x] 1.2 修改 `integration-tests/spring-mvc-test/src/test/java/.../BusinessLogGenerationTest.java`：将"测试错误日志"的 error 调用改为 warn
- [x] 1.3 修改 `integration-tests/multi-framework-test/src/test/java/.../BusinessLogGenerationTest.java`：将"测试错误日志"的 error 调用改为 warn
- [x] 1.4 修改 `integration-tests/jdk21-test/src/test/java/.../Jdk21CompatibilityTest.java`：将"OSS上传测试错误日志"的 error 调用改为 warn
- [x] 1.5 修改 `integration-tests/jsp-servlet-test/src/test/java/.../BusinessLogGenerationTest.java`：将"测试错误日志"的 error 调用改为 warn
- [x] 1.6 修改 `integration-tests/jsp-servlet-test/src/main/java/.../TestExceptionServlet.java`：将"测试错误日志"的 error 调用改为 warn
- [x] 1.7 修改 `integration-tests/distribution-test/` 下各测试文件：将"测试错误日志"的 error 调用改为 warn

## 2. 修复 GitHub Actions 配置

- [x] 2.1 修改 `.github/workflows/integration-verify-gate.yml`：将两处 `if-no-files-found: warn` 改为 `if-no-files-found: error`

## 3. 验证

- [x] 3.1 使用 grep 确认 integration-tests 中不再有包含"测试日志"或"测试错误日志"的 error 级别日志
- [x] 3.2 确认 `.github/workflows/integration-verify-gate.yml` 中无 `if-no-files-found: warn`
