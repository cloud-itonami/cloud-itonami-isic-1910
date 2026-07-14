# cloud-itonami-isic-1910

Open Business Blueprint for **ISIC Rev.5 1910**: manufacture of coke oven products.

This repository publishes a community coke-oven operations coordinator -- raw material intake and verification, production-run scheduling proposals, byproduct coordination, and emissions compliance logging -- as an OSS business that any qualified coke-plant operator can fork, deploy, run, improve and sell, so coke-oven manufacturers can manage plant operations safely, with auditable decision records and hard safety gates preventing furnace/process-control operations from being delegated to an LLM, without renting a closed SaaS.

Built on this workspace's [`langgraph-clj`](https://github.com/com-junkawasaki/langgraph-clj) StateGraph runtime (portable `.cljc`, supervised superstep loop, interrupts, Datomic/in-mem checkpoints) -- the same actor pattern as every cloud-itonami actor (production-run scheduling proposals and emissions reporting with threshold exceedances are high-stakes operations requiring hard safety review and human sign-off before any real-world action).

## Scope: what this actor does and does not do

This actor covers raw material intake verification, production-run scheduling proposal, byproduct coordination, and emissions compliance logging. It does **not** operate furnaces, control coking processes, discharge coke, or make process-engineering decisions. Those remain the exclusive authority of licensed plant engineers.

### CRITICAL: What this actor does NOT do

- **NO furnace charging/discharging** — remains engineer exclusive authority
- **NO process-temperature or coking-time control** — remains engineer exclusive authority
- **NO process-parameter decisions** (pressure, composition, combustion control) — remains engineer exclusive authority
- **NO emissions-system operation** — hardware operation remains engineer exclusive; this actor only records monitoring data

Any proposal mentioning furnace control, charging, temperature setpoints, or process parameters is immediately rejected as a hard violation.

### Actuation

**Dispatching a real production-run scheduling or emissions-report logging is never autonomous, at any phase, by construction.** Two independent layers enforce this (`coke.governor`'s high-stakes gates and `coke.phase`'s phase table) -- see `coke.phase`'s docstring and test suite. The actor may draft, check and recommend; a human coke-plant engineer is always the one who actually schedules production or logs emissions reports.

## The core contract

```
coal intake + supplier verification + emissions baseline + worker safety
        |
        v
Coke Operations Advisor -> Coke Operations Governor -> schedule proposal, emissions log, or human approval
        |
        v
robot actions (gated) + audit ledger + escalation to engineer
```

No automated advice can schedule a production run the governor refuses, verify a supplier outside its registered scope, or publish an emissions report with threshold exceedance without governor approval and human sign-off. **A proposal mentioning process-control operations is permanently rejected, even with human approval.**

## Capability layer

Resolves via [`kotoba-lang/industry`](https://github.com/kotoba-lang/industry) (ISIC `1910`). Implemented by:

- [`kotoba-lang/robotics`](https://github.com/kotoba-lang/robotics) — missions, actions, safety-stops, telemetry proofs

See [`docs/business-model.md`](docs/business-model.md) and [`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.
