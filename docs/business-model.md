# Business Model: Coke Oven Operations Coordinator

## Executive Summary

This OSS actor provides a free, auditable, open-source alternative to closed SaaS for coke-plant operations coordination. Operators fork, deploy, run, and sell (or give away) their own instances without renting from a vendor.

## Value Proposition

### For Coke-Oven Manufacturers

1. **Reduced Capital:** No per-seat SaaS licensing; deploy once, run freely
2. **Auditability:** All decisions logged to append-only ledger with spec-basis citations; regulators can inspect decision records
3. **Safety by Design:** Hard gates prevent dangerous operations (e.g., emissions threshold exceedances cannot be silently logged)
4. **No Vendor Lock:** Fork, run, modify, and deploy as needed; never depend on a vendor's API uptime or pricing

### For Regulators

1. **Transparency:** All decisions recorded with jurisdiction-specific citations
2. **Accountability:** Decision audit trail links to human approver
3. **Standardization:** Same blueprint across operators; easier to compare/audit industry practices

### For Equipment Vendors

1. **Interoperability:** Actor exposes telemetry API (robotics missions); integrate your coal-supply system, pressure monitors, emissions gauges
2. **Market Expansion:** Sell to many operators at lower integration cost

## Revenue Model (for operators who choose to commercialize)

Operators who deploy and run this actor can:
- Charge for deployment/customization consulting
- Sell specialized versions (e.g., pre-integrated with specific coal-supply vendors)
- Offer managed hosting or cloud deployment services
- Contribute improvements back to this blueprint and build reputation

**This repo does not monetize directly.** It is a public good (AGPL-3.0-or-later).

## Typical Deployment

```
Coke Plant Operations Team
  |
  v
Coke Operations Coordinator (deployed on-prem or cloud)
  |
  +-- langgraph-clj StateGraph (supervises advisor+governor)
  +-- LLM Advisor (suggests scheduling, compliance actions)
  +-- Safety Governor (evaluates against hard gates)
  +-- Audit Ledger (Datomic or event log)
  |
  v
Plant Engineer (approves/rejects proposals)
  |
  v
Real-World Actuation (furnace control, emissions hardware) -- handled separately, NOT by actor
```

The actor is in the middle: it **proposes and logs**, never **commands real hardware**.

## Sustainability

- **Maintenance:** Contributed by coke-plant operators and their engineering teams
- **Feature Requests:** Jurisdictions contribute new requirements to `facts.cljc`
- **Governance:** Per-repo GOVERNANCE.md; no central authority except pull-request review
- **License:** AGPL-3.0-or-later ensures forks contribute improvements back

## Regulation Compliance

Each jurisdiction's requirements are backed by official spec-basis:
- Japan: Gas Utility Regulation Act, Industrial Safety and Health Act, Air Pollution Control Law
- USA: MSHA Regulations, Clean Air Act Title V, OSHA standards
- UK: Environmental Permitting Regulations, Health and Safety at Work Act

Adding a new jurisdiction is a one-line pull request: cite the official source, do not invent.

## Competitive Advantage (vs. SaaS)

| Feature | This Actor | Typical SaaS |
|---------|-----------|-------------|
| Auditability | 100% (open source, append-only ledger) | Proprietary (trust vendor) |
| Customization | Full source (fork) | API limits or consultant engagement |
| Uptime | Your infrastructure | Vendor SLA (may not match your needs) |
| Price | Free (AGPL-3.0-or-later) | Per-seat or per-transaction |
| Onboarding | Deploy + integrate (time) | Quick setup but vendor lock-in |
| Vendor Lock-in | None (fork anytime) | High (data export limitations) |
