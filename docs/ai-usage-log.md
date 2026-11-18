# AI usage log (Week 7)

Assistant used: Claude (chat). No GitHub Copilot was used. [Change this if untrue.]

| # | Suggestion | Decision | How checked |
|---|---|---|---|
| 1 | Install PostgreSQL directly instead of Docker | Accepted | Time limit; install verified with `psql --version` (18.6) |
| 2 | Use `psql` as the client instead of DBeaver | Accepted | Output saved to files |
| 3 | Design defaults: NUMERIC(12,2), CHECK on text, text business IDs, 8 tables | Accepted | Reviewed against Java model |
| 4 | `schema.sql` drafted by assistant | Accepted | Ran twice from scratch, no errors, 8 tables listed (`tables-output.txt`) |
| 5 | `seed.sql` drafted by assistant | Accepted | Row counts 7/8/4/9/6/10/5/3 and sum 911000.00 matched hand calculation |
| 6 | Save psql output to files with `>` | Accepted | Fixed truncated-paste problem |
| 7 | `>>` for the sum file | Rejected | It appends and would duplicate lines; replaced with `>` |
| 8 | `crud.sql` (13 queries) drafted by assistant | Accepted | All results compared with hand-calculated values (`crud-results.txt`) |
| 9 | `invalid-ops.sql` (10 tests) drafted by assistant | Accepted | All 10 rejected with the expected constraint names (`constraint-tests.txt`) |
| 10 | ERD, notes and README drafted by assistant | Accepted | Reviewed and edited by me |

## Verification notes
- Re-ran `schema.sql` then `seed.sql` from scratch; counts were identical.
- Hand-calculated order totals (467000, 210000, 74000, 68000, 54000, 38000, 17000) and the overall sum 911000.00 matched query output.
- CRUD cascade deletes were verified by counts returning to 7/6/10/8.
- Every invalid statement produced an `ERROR` naming the expected constraint.
- Honesty note: the SQL was drafted by the assistant under a time limit. I ran it, checked results against hand calculations and read the design notes.