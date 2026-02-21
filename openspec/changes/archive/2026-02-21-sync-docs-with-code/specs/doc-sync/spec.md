# doc-structure-accuracy Specification

## Purpose

Ensure project documentation accurately reflects the current codebase structure to prevent developer confusion during onboarding and navigation.

## ADDED Requirements

### Requirement: Source tree documentation SHALL reflect actual directory structure

The `docs/architecture/source-tree.md` file SHALL accurately represent the current project directory structure including all Maven modules, package directories, and test directories.

#### Scenario: Root Maven modules
- **WHEN** developers examine the root directory listing
- **THEN** the documentation SHALL list: `logx-producer/`, `logx-s3-adapter/`, `log4j-oss-appender/`, `log4j2-oss-appender/`, `logback-oss-appender/`, `integration-tests/`, `distributions/`

#### Scenario: Removed directories
- **WHEN** a previously documented directory no longer exists in the codebase
- **THEN** the documentation SHALL NOT reference that directory (e.g., `.bmad-core/`, `logx-sf-oss-adapter/`)

#### Scenario: Producer package structure
- **WHEN** developers examine `logx-producer/src/main/java/org/logx/`
- **THEN** the documented packages SHALL include: `adapter/`, `config/`, `core/`, `error/`, `exception/`, `fallback/`, `reliability/`, `retry/`, `storage/`, `util/`

#### Scenario: S3 adapter file structure
- **WHEN** developers examine `logx-s3-adapter/src/main/java/org/logx/storage/s3/`
- **THEN** the documented files SHALL include: `S3StorageAdapter.java`, `S3StorageServiceAdapter.java`, `S3StorageServiceProvider.java`

### Requirement: QA gate file paths SHALL match current directory names

All QA gate YAML files SHALL use the correct directory name `integration-tests/` (not the deprecated `compatibility-tests/`).

#### Scenario: Gate file references
- **WHEN** examining YAML files in `docs/qa/gates/`
- **THEN** all test directory references SHALL use `integration-tests/` path
