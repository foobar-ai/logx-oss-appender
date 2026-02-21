# Tasks: sync-docs-with-code

## 1. Update source-tree.md

- [x] 1.1 Remove `.bmad-core/` entry from root directory listing
- [x] 1.2 Remove `logx-sf-oss-adapter/` entry from root directory listing
- [x] 1.3 Add `fallback/` and `util/` packages to the `logx-producer` package listing
- [x] 1.4 Replace the `storage/s3/` file list in `logx-producer` section with a note that S3 implementation lives in `logx-s3-adapter`
- [x] 1.5 Update the `logx-s3-adapter` section to list actual files: `S3StorageAdapter.java`, `S3StorageServiceAdapter.java`, `S3StorageServiceProvider.java`
- [x] 1.6 Remove duplicate `S3StorageAdapter.java` entry from the file listing
- [x] 1.7 Remove `logx-sf-oss-adapter-*.jar` from the deployment packages section

## 2. Fix QA gate path references

- [x] 2.1 Replace `compatibility-tests/` with `integration-tests/` in `docs/qa/gates/7-框架兼容性验证.yml`
