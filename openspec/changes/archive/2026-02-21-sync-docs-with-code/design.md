# Design: sync-docs-with-code

## Context

The project has evolved significantly since early documentation was written. Several architecture docs describe a codebase that no longer exists:

- `docs/architecture/source-tree.md` references `.bmad-core/` (removed), lists non-existent S3 classes (`AwsS3Config.java`, `S3ConfigValidator.java`, `S3StorageFactory.java`, `S3StorageInterface.java`), and omits real packages (`fallback/`, `util/`). The `storage/s3/` section in `logx-producer` is wrong — those files now live in `logx-s3-adapter`.
- `docs/qa/test-design-1-基础设施和存储接口设计.md` and `docs/stories/1-基础设施和存储接口设计.md` reference `.bmad-core/` as a required directory.
- `docs/qa/gates/7-框架兼容性验证.yml` still uses `compatibility-tests/` path (renamed to `integration-tests/`).
- `docs/S3StorageInterface-API.md` and `docs/architecture/coding-standards.md` reference `AwsS3Config` and `S3StorageInterface` which no longer exist as standalone classes.

The actual current structure:
- `logx-producer/src/main/java/org/logx/` contains: `adapter/`, `config/`, `core/`, `error/`, `exception/`, `fallback/`, `reliability/`, `retry/`, `storage/`, `util/`
- `logx-s3-adapter/src/main/java/org/logx/storage/s3/` contains: `S3StorageAdapter.java`, `S3StorageServiceAdapter.java`, `S3StorageServiceProvider.java`
- `logx-sf-oss-adapter/` directory does not exist in the root (was removed or never materialized)
- `.bmad-core/` does not exist
- Integration tests live under `integration-tests/` (not `compatibility-tests/`)

## Goals / Non-Goals

**Goals:**
- Update `docs/architecture/source-tree.md` to reflect the actual directory and file structure
- Remove all references to `.bmad-core/` from active documentation
- Correct S3 class listings to match what actually exists in `logx-s3-adapter`
- Add missing packages (`fallback/`, `util/`) to the source tree doc
- Fix `compatibility-tests/` → `integration-tests/` in QA gate files

**Non-Goals:**
- Updating historical QA reports, story summaries, or assessment files (these are historical records, not living docs)
- Changing any code
- Updating `docs/S3StorageInterface-API.md` — this is a deeper API doc that needs separate investigation
- Updating `docs/architecture/coding-standards.md` code examples — these are illustrative, not structural

## Decisions

**Decision: Scope to structural/navigational docs only**

Only update docs that developers use to navigate the codebase (`source-tree.md`, active QA gates). Historical story files and QA assessments are records of past work — correcting them would be misleading. Rationale: changing historical records creates confusion about what was actually built when.

**Decision: Update `docs/qa/gates/7-框架兼容性验证.yml` path references**

This is an active gate file used in CI/QA workflows, not a historical record. The `compatibility-tests/` path references will break if used. Update to `integration-tests/`.

**Decision: Do not create a new spec for this change**

The proposal identified `module-naming-conventions` as a potentially modified capability, but reviewing the existing spec, its requirements are already satisfied by the code. This change is purely about bringing docs into alignment — no spec-level behavior is changing.

## Risks / Trade-offs

- **[Risk] Docs may have additional stale references not caught in this pass** → Mitigation: Focus on `source-tree.md` as the primary structural reference; other docs are lower priority
- **[Risk] `logx-sf-oss-adapter` references in story files** → Mitigation: Leave story files untouched (historical); only note in source-tree that the module is not present in the current codebase

## Migration Plan

1. Update `docs/architecture/source-tree.md` — correct root structure, `logx-producer` package list, `logx-s3-adapter` file list
2. Update `docs/qa/gates/7-框架兼容性验证.yml` — replace `compatibility-tests/` with `integration-tests/`
3. No rollback needed — documentation-only changes
