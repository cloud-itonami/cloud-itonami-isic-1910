# Contributing to cloud-itonami-isic-1910

Thank you for considering contributing to this open-source coke-oven operations actor!

## How to Contribute

### Reporting Issues

- Use GitHub Issues to report bugs or suggest features
- Include reproduction steps for bugs
- Link to relevant spec-basis citations for new requirements

### Submitting Changes

1. Fork this repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Make your changes
4. Run tests: `clojure -M:test`
5. Run linter: `clojure -M:lint`
6. Commit with a clear message
7. Push and open a pull request

### Adding a Jurisdiction

1. Add entry to `facts.cljc` catalog with **official spec-basis citations**
2. Add tests in `test/coke/facts_test.clj`
3. Update `docs/operator-guide.md` with jurisdiction coverage
4. Run full test suite: `clojure -M:test && clojure -M:lint`

**IMPORTANT:** Never invent jurisdiction requirements. Every citation must link to an official source (law, regulation, standard).

### Code Style

- Follow Clojure conventions (idiomatic expressions)
- Use `:cljc` for all source (portable across JVM/CLJS)
- Document all functions with docstrings
- Avoid mutable state outside of the store atom
- Keep tests in `test/` parallel to source files

### Testing

- All new code must have tests
- Tests use `clojure.test` framework
- Run: `clojure -M:test`
- Aim for 100% coverage of governor contract

### Linting

Run `clojure -M:lint` before submitting. clj-kondo will catch common errors.

## Code of Conduct

See CODE_OF_CONDUCT.md.

## License

By contributing, you agree that your contributions are licensed under AGPL-3.0-or-later.
