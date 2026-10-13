# Code Review Checklist - Week 3

| Area | Done | Notes |
|---|---|---|
| Requirements met | [x] | Packages, 2 interfaces, 4 implementations, composition, tests, diagrams |
| Logic and correctness | [x] | Totals match Week 1 (276000 and 246000) |
| Input validation | [x] | Moved to `OrderInputValidator`, messages unchanged |
| Java syntax and conventions | [x] | camelCase names, one class per file, packages match folders |
| Readability | [x] | `App.main` split into small methods |
| Method and class organisation | [x] | Each class has one job |
| Edge cases | [x] | Tier boundaries, null and blank location, null promo code |
| Not over-complicated | [x] | Interfaces only where behaviour can vary |
| Duplication removed | [ ] | Pricing duplication removed; model checks not yet |
| Existing behaviour preserved | [x] | Old tests mapped to new tests (see README) |
| All tests pass | [x] | 69 tests, `mvn clean test` gives BUILD SUCCESS |
| No build output committed | [x] | `target` is ignored |

## Known open items

- Repeated blank/null checks in the Week 2 model were not refactored.
- Style slips in `Customer`, `Order` and `Product` remain.
- A NaN price still passes validation (carried over from Week 1).