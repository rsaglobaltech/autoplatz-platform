---
product: autoplatz-platform
surfaces: [api, web]
actors:
  # Who uses the product, and where. Each actor gets a scenario on each of
  # their surfaces (rule D3), so no screen is forgotten.
  - { name: Customer, surfaces: [api, web] }
# The pieces to specify and build, in the order you will build them,
# e.g. [catalogue, orders]. Each becomes `specgate propose --from-brief <module>`.
modules: []
---

# Brief

Paste the client's brief here **as it is**, without rewriting it: every value
in a scenario must come from this text or from `assumptions.md` (rule D6).

Then give each module a section with its cards:

<!--
## Module: orders
Rules
- An order holds at most 20 items.
Examples
- The one where Ana orders 21 items and is refused.
Questions
- Can an order be changed after it is paid?
-->

`npx specgate propose --from-brief --check` checks the brief, and
`npx specgate propose --from-brief --agent "claude -p"` lets your agent propose
the fields and the sections from the text above.

Working from Jira tickets instead? You do not need this file: see
`docs/specgate.md`.
