# Governance: cloud-itonami-isic-1910

## Decision-Making

This is a community-driven open-source project. Major decisions are made transparently via GitHub pull requests and issues.

### Pull Request Review

- Every pull request must pass tests (`clojure -M:test`)
- Every pull request must pass linting (`clojure -M:lint`)
- At least one maintainer review required before merge
- New jurisdictions require verification of official spec-basis citations

### Major Changes

Changes that affect the governor contract or safety gates require:
1. GitHub issue discussion (why the change?)
2. Pull request with tests
3. Documentation update (docs/adr/)
4. Maintainer consensus

### Dispute Resolution

If contributors disagree on a feature or design decision:
1. Discuss in the GitHub issue with cite context
2. Propose a compromise pull request
3. If no consensus, escalate to maintainers

## Roles

### Maintainers

Currently: The cloud-itonami team at com-junkawasaki organization.

Responsibilities:
- Review pull requests
- Merge approved changes
- Maintain test suite
- Manage releases
- Enforce Code of Conduct

### Contributors

Anyone who submits code, documentation, tests, or feedback.

## Licensing

- Source code: AGPL-3.0-or-later
- Documentation: CC-BY-4.0
- All contributions must be compatible with AGPL-3.0-or-later

## Releases

- Semantic versioning (MAJOR.MINOR.PATCH)
- Published to GitHub Releases
- Announcement in project README

## Feedback and Communication

- GitHub Issues for bugs/features
- GitHub Discussions for design questions
- Pull requests for code changes
- Transparency: all decisions are documented and public

## No Funding or Sponsorship

This is a gift to the industrial-operations open-source community. There is no commercial funding, no corporate backing, and no expectation of profit. Operators who deploy and run this actor may commercialize their own services, but the blueprint itself is free (AGPL-3.0-or-later).
