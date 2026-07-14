# Security Policy: cloud-itonami-isic-1910

## Reporting Security Issues

If you discover a security vulnerability, please **do not** open a public GitHub issue. Instead, email the maintainers privately:

- Jun Kawasaki: jun784@gmail.com

Include:
1. Description of the vulnerability
2. Steps to reproduce
3. Potential impact
4. Suggested fix (if any)

We will respond within 48 hours and work with you on a coordinated disclosure.

## Security Considerations

### Hard Safety Gates

This actor is designed around safety-critical gates (governor contract):
- Furnace control operations are forbidden (hard block)
- Emissions threshold exceedances always escalate (hard block)
- Supplier/material verification is mandatory (hard block)

These gates are implemented in `coke.governor` and tested exhaustively. Changes to the governor must:
1. Pass all existing tests
2. Add new tests for the change
3. Document the safety impact

### Audit Ledger

All decisions are logged to an append-only ledger. In production:
- Use Datomic with read-only history (prevent rewriting decisions)
- Back up the ledger to immutable storage
- Inspect audit logs regularly for anomalies

### Dependency Management

- langgraph-clj and langchain-clj are zero-dependency libraries
- All dependencies are explicit in `deps.edn`
- No runtime JVM reflection (security risk)
- Static analysis with clj-kondo (catches common bugs)

### Deployment Security

When deploying to production:
1. Run on isolated infrastructure (no public internet access)
2. Require authentication for all API endpoints
3. Log all access to audit trail
4. Never store plaintext credentials in code
5. Use environment variables or secure vaults for secrets
6. Enable HTTPS/TLS for all network communication

### Process-Control Boundary

The actor is deliberately forbidden from operating furnaces, changing process parameters, or discharging coke. This boundary is enforced by:
- Hard keyword block in `coke.governor` (recognizes process-control terms)
- Tests that verify the block is working
- Clear documentation in README and operator-guide

### Incident Response

If a security issue occurs in production:
1. Isolate the affected system immediately
2. Inspect the audit ledger to determine what happened
3. Review governor evaluations for anomalies
4. Report to compliance/safety teams
5. Do not attempt to rewrite history (append-only principle)

## Code Review

All pull requests are reviewed for:
- Correctness of logic
- Safety-gate integrity
- Test coverage
- Compliance with AGPL-3.0-or-later license
- No introduction of unsafe dependencies

## Disclaimer

This is open-source software provided as-is. Users are responsible for:
1. Understanding the actor's scope and limitations
2. Deploying securely in their environment
3. Maintaining their own backup and disaster recovery
4. Complying with their jurisdiction's regulations
5. Final safety decisions (the actor advises, humans approve)

No warranty is provided; AGPL-3.0-or-later terms apply.
