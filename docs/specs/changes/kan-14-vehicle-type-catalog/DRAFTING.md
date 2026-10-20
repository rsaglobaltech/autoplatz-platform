# Drafting KAN-14 — Vehicle type catalog

Turn **only KAN-14** into requirements and scenarios for the
`parking` capability. Nothing is built yet: this is the specification
a person will review. Specgate checks it against the ticket.

- **The ticket:** `docs/specs/changes/kan-14-vehicle-type-catalog/ticket.md` — the only source of business facts. Do not edit it:
  Specgate refuses a draft whose copy of the ticket changed.
- **Write only inside:** `docs/specs/changes/kan-14-vehicle-type-catalog/`.
- **Requirement ids:** `REQ-002` … `REQ-005`, no others.
- **Scenario ids:** from `SCN-002` up, one each.

## 0. Your scope — the acceptance criteria

Each criterion becomes at least one scenario. Criteria about the same rule
share one requirement. Quote the criteria a requirement comes from under
`> Ticket:`.

1. Given a fresh installation, when the system starts, then default types exist: Car, Motorcycle, Van.
2. Given the catalog screen, when I add a type with a unique name, then it becomes available in the entry form.
3. Given a duplicate or empty name, when I save, then it is rejected with a validation message.
4. Given a type already used by existing entries, when I try to delete it, then it is deactivated instead of deleted (hidden from the form, kept in history).

**Why** — the ticket's own words:

> As an administrator, I want to manage a list of vehicle types so that entries use consistent values and rates can depend on the type if needed.

## 1. The requirements — `specs/parking/spec.md`

Replace the placeholder with one block per requirement:

```markdown
## ADDED Requirements

### Requirement: REQ-002 — <a short name>

<!-- specgate:trace actor=<Actor> kind=functional -->

The system SHALL <what it does, observably>.

> Ticket: "<the criterion this comes from>"

#### Scenario: SCN-002 — <what happens>

- GIVEN <the criterion's given, with its values>
- WHEN <its when>
- THEN <its then, with values a test can assert>
```

- `kind` is `functional`, `business-rule` or `non-functional`. A
  non-functional requirement states a number with a unit.
- Keep the criterion's values. Where it says "a plate", choose a concrete
  one and list it in `assumptions.md`.
- A criterion that prescribes tests instead of behaviour becomes one
  scenario per case it lists.

## 2. What the ticket does not say — `assumptions.md`

Every value in a scenario that the ticket does not give gets a row naming
where it is used:

```markdown
| # | Assumption | Used in | Why | Status |
|---|---|---|---|---|
| A1 | HTTP 409 when the plate is inside | REQ-002 | REST convention | to confirm |
```

## 3. What only the analyst can decide — `questions.md`

The ticket has no open questions. A question you find goes in a row naming the
requirements it blocks; each of those carries `status="Needs Clarification"`.

## 4. Check it, until it is ready

```bash
specgate propose --from-ticket KAN-14
```

Fix every finding and run it again, until it says **Ready to review**. A
`D6_unlisted_value` means a value the ticket never gave.

Do not archive. A person reviews the draft; each requirement is then built
test first and delivered with `specgate apply REQ-NNN`, in commits that name KAN-14.
