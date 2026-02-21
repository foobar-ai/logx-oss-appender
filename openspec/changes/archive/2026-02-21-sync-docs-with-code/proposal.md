# Proposal: sync-docs-with-code

## Why

The project documentation contains numerous inaccuracies that misrepresent the current codebase structure. These discrepancies cause confusion during onboarding, make it difficult to navigate the codebase, and erode trust in the documentation. Common issues include: referencing non-existent directories (`.bmad-core/`), listing files that no longer exist (e.g., `AwsS3Config.java`, `S3ConfigValidator.java`), and omitting packages that do exist (e.g., `fallback/`, `util/`).

## What Changes

- Update all architecture documentation to reflect actual directory structure
- Remove references to non-existent directories and files
- Add missing packages to documentation
- Ensure code examples match current implementation
- Verify consistency across all documentation files

## Capabilities

### New Capabilities
None - this is a documentation synchronization task.

### Modified Capabilities
- `module-naming-conventions`: Update to reflect current directory structure and verify naming conventions are followed

## Impact

- **Affected Documentation**: `docs/architecture/source-tree.md`, `docs/stories/1-基础设施和存储接口设计.md`, `docs/qa/test-design-1-基础设施和存储接口设计.md`
- **Code Files**: None - purely documentation updates
- **Configuration**: None
