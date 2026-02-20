## 1. 重命名目录（保留 Git 历史）

- [ ] 1.1 使用 `git mv all-in-one distributions` 重命名 all-in-one 目录
- [ ] 1.2 使用 `git mv compatibility-tests integration-tests` 重命名 compatibility-tests 目录
- [ ] 1.3 提交目录重命名操作（第一个 commit）

## 2. 更新 Maven 配置

- [ ] 2.1 更新根目录 `pom.xml` 中的 `<modules>` 部分，将 `all-in-one` 改为 `distributions`，`compatibility-tests` 改为 `integration-tests`
- [ ] 2.2 更新 `distributions/pom.xml` 中的 `<artifactId>` 从 `all-in-one` 改为 `distributions`
- [ ] 2.3 更新 `distributions/pom.xml` 中的 `<name>` 描述文字
- [ ] 2.4 更新 `integration-tests/pom.xml` 中的 `<artifactId>` 从 `integration-compatibility-tests-parent` 改为 `integration-tests-parent`
- [ ] 2.5 更新 `integration-tests/pom.xml` 中的 `<name>` 描述文字
- [ ] 2.6 更新 `integration-tests/pom.xml` 中引用 `all-in-one-test` 的模块路径（如果有）

## 3. 更新文档

- [ ] 3.1 更新 `README.md` 中所有 `all-in-one/` 路径引用为 `distributions/`
- [ ] 3.2 更新 `README.md` 中所有 `compatibility-tests/` 路径引用为 `integration-tests/`
- [ ] 3.3 更新 `README.md` 中所有描述性文字（如"All-in-One集成包"等）
- [ ] 3.4 检查其他 markdown 文档（如 `AGENTS.md`、`docs/` 目录）是否有引用需要更新

## 4. 更新构建脚本

- [ ] 4.1 更新 `scripts/integration-verify.sh` 中的路径引用
- [ ] 4.2 检查 `scripts/` 目录下其他脚本是否有引用需要更新

## 5. 更新 CI/CD 配置

- [ ] 5.1 更新 `.github/workflows/integration-verify-gate.yml` 中的路径引用
- [ ] 5.2 检查其他 GitHub Actions 工作流文件是否有引用需要更新

## 6. 提交配置和文档更新

- [ ] 6.1 提交所有配置和文档更新（第二个 commit）
- [ ] 6.2 在 commit message 中说明开发者需要重新导入 Maven 项目

## 7. 验证构建和测试

- [ ] 7.1 运行 `mvn clean install` 验证所有模块构建成功
- [ ] 7.2 运行 `mvn test` 验证所有测试通过
- [ ] 7.3 检查 `target/` 目录中的 JAR 文件名是否保持不变
- [ ] 7.4 验证子模块的 `pom.xml` 中 `<artifactId>` 未被修改

## 8. 验证 Git 历史

- [ ] 8.1 运行 `git log --follow distributions/` 验证能看到重命名前的历史
- [ ] 8.2 运行 `git log --follow integration-tests/` 验证能看到重命名前的历史

## 9. 最终检查

- [ ] 9.1 使用 `grep -r "all-in-one" .` 搜索是否有遗漏的引用（排除 `.git/` 和 `target/`）
- [ ] 9.2 使用 `grep -r "compatibility-tests" .` 搜索是否有遗漏的引用（排除 `.git/` 和 `target/`）
- [ ] 9.3 在 IDE 中重新导入 Maven 项目，确认模块识别正常
