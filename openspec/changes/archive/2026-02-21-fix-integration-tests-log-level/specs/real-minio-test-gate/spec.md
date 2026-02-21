# real-minio-test-gate Specification

## MODIFIED Requirements

### Requirement: 门禁必须覆盖项目要求的全量集成链路
门禁流程 MUST 执行项目定义的完整集成测试范围（包括兼容性测试集合及项目要求的扩展链路），不得以部分模块通过替代全量通过结论。

**Updated Description:**
门禁流程 MUST 执行项目定义的完整集成测试范围（包括兼容性测试集合及项目要求的扩展链路），不得以部分模块通过替代全量通过结论。GitHub Actions 配置中的 `if-no-files-found` 必须设置为 `error`，确保在测试结果文件缺失时能正确检测失败。

#### Scenario: 仅执行了部分测试模块
- **WHEN** 验证流程检测到仅执行子集测试而非全量链路
- **THEN** 系统 MUST 判定门禁失败
- **AND** 系统 MUST 标识缺失的测试范围

#### Scenario: 测试结果文件缺失
- **WHEN** GitHub Actions 上传测试结果时发现文件不存在
- **THEN** 系统 MUST 判定门禁失败
- **AND** 系统 MUST 输出文件缺失的诊断信息
