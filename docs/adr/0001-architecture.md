# ADR-0001: Coke Oven Operations Coordinator Architecture

**Status:** Implemented

**Date:** 2026-07-14

## Summary

This document defines the architecture of the Coke Oven Operations Coordinator actor for ISIC 1910 (Manufacture of coke oven products). The actor coordinates plant operations logistics and compliance, NOT process engineering or furnace control.

## Context

Coke-oven manufacturing requires:
1. Raw coal supplier verification and batch tracking
2. Production-run scheduling (proposals only)
3. Byproduct (tar, gas, chemicals) handling and shipment coordination
4. Emissions monitoring and compliance reporting
5. Strict separation of concerns: an LLM advisor cannot make process-engineering decisions

The actor follows the cloud-itonami pattern: LLM advisor proposes, independent governor evaluates against hard safety gates, human reviews/approves for high-stakes operations.

## Design Decisions

### 1. **No Process Control Authority**

The actor is forbidden from:
- Controlling furnace charge/discharge
- Setting process temperatures or coking times
- Making pressure/composition/combustion decisions
- Operating emissions-system hardware

**Implementation:** `coke.governor` has a hard block (`process-control-block-violations`) that rejects any proposal mentioning furnace control, charging, temperature setpoints, or process parameters, regardless of confidence or evidence quality.

### 2. **Jurisdiction-Specific Regulations (Honest Catalog)**

Each jurisdiction (Japan, USA, UK, etc.) has its own coal-supply and emissions requirements, all backed by official citations. The catalog is deliberately small (3 jurisdictions) to honestly represent coverage: we claim only what we verify with official spec-basis.

**File:** `src/coke/facts.cljc`

### 3. **Governor Contract with Hard and Soft Gates**

**Hard violations (no override):**
- No spec-basis citation
- Missing required evidence
- Process-control keywords present (furnace, charge, discharge, temperature-setpoint, etc.)
- Emissions threshold exceedance flagged
- Supplier/material not verified

**Soft violations (human can approve):**
- Low confidence (< 0.6)
- High-stakes actuation (production-run scheduling, emissions reporting)

**File:** `src/coke/governor.cljc`

### 4. **Emissions Threshold Exceedance: Always Escalate**

If an emissions report shows any monitored value (SO2, NOx, particulates) exceeding threshold, it MUST escalate to human. Never silently log a threshold exceedance.

### 5. **Proposal-Only Production Scheduling**

The actor proposes production-run schedules but never executes them. A human plant engineer reviews and approves (or rejects) the proposal before real scheduling happens.

## Implementation

- **Language:** Portable Clojure (`.cljc`)
- **Runtime:** langgraph-clj StateGraph (JVM/Chicory/ClojureScript/browser WASM)
- **State Store:** In-memory atom (reference), Datomic for production
- **Audit Ledger:** Append-only (via langgraph-clj checkpoint system)
- **Tests:** JVM-based (clojure.test), 100% coverage of governor contract

## Scope Boundaries

### IN SCOPE
- Coal supplier registration/verification
- Coal-batch quality tracking
- Production-run scheduling **proposals** (human executes)
- Byproduct inventory and shipment coordination
- Emissions monitoring data recording
- Compliance report generation
- Escalation logic for violations

### OUT OF SCOPE
- Furnace operation/control
- Process-parameter decisions
- Emissions-system hardware operation
- Worker safety policies (logged/tracked but not decided by actor)
- Incident response (logged, human responds)

## Portability

All source code is `.cljc` (Clojure + ClojureScript). Reader-conditional splits:
- `:clj` for Datomic/persistent storage integration (production fallback)
- `:cljs` for browser WASM and Node.js execution

No `.kt` (Kotlin), `.go` (Go), or language-specific libraries. langgraph-clj brings langchain-clj transitively; both are zero-dependency and portable.

## Next Steps

1. Mirror this repo for other ISIC classes (3520 Manufacture of gas already exists as template)
2. Extend `facts.cljc` catalog as new jurisdictions are added
3. Integrate real Datomic persistence for audit ledger
4. Deploy via kototama actor-host (Chicory on JVM, browser WASM)
