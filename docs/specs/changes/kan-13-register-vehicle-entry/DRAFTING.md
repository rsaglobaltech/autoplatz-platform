# Drafting KAN-13 — Register vehicle entry

Turn **only KAN-13** into requirements and scenarios for the
`parking` capability. Nothing is built yet: this is the specification
a person will review. Specgate checks it against the ticket.

- **The ticket:** `docs/specs/changes/kan-13-register-vehicle-entry/ticket.md` — the only source of business facts. Do not edit it:
  Specgate refuses a draft whose copy of the ticket changed.
- **Write only inside:** `docs/specs/changes/kan-13-register-vehicle-entry/`.
- **Requirement ids:** `REQ-006` … `REQ-011`, no others.
- **Scenario ids:** from `SCN-007` up, one each.

## 0. Your scope — the acceptance criteria

Each criterion becomes at least one scenario. Criteria about the same rule
share one requirement. Quote the criteria a requirement comes from under
`> Ticket:`.

1. Given the entry form, when it is displayed, then it contains: license plate (required), vehicle type (required, selected from the catalog), owner name (optional) and entry date/time.
2. Given the entry form, when it opens, then the entry date/time is pre-filled with the current date and time.
3. Given a plate typed with lowercase letters or spaces (e.g. "ab 123 cd"), when I save, then it is stored normalized as uppercase without spaces ("AB123CD").
4. Given a required field is empty, when I save, then the entry is not saved and the missing field is highlighted.
5. Given a plate that already has an open entry (vehicle still inside), when I try to register a new entry, then it is rejected with the message "Vehicle is already in the garage".
6. Given valid input, when I save, then the entry is stored in the database with status "inside" and appears in the list of vehicles currently in the garage.

**The values you may use** — write them exactly so; any other value goes
in `assumptions.md` or becomes a question:

`ab 123 cd` · `AB123CD` · `Vehicle is already in the garage` · `inside`

**Why** — the ticket's own words:

> As an administrator, I want to register a vehicle's entry so that I can track which vehicles are in the garage and since when.

## 1. The requirements — `specs/parking/spec.md`

Replace the placeholder with one block per requirement:

```markdown
## ADDED Requirements

### Requirement: REQ-006 — <a short name>

<!-- specgate:trace actor=<Actor> kind=functional -->

The system SHALL <what it does, observably>.

> Ticket: "<the criterion this comes from>"

#### Scenario: SCN-007 — <what happens>

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
| A1 | HTTP 409 when the plate is inside | REQ-006 | REST convention | to confirm |
```

## 3. What only the analyst can decide — `questions.md`

The ticket has no open questions. A question you find goes in a row naming the
requirements it blocks; each of those carries `status="Needs Clarification"`.

## 4. Check it, until it is ready

```bash
specgate propose --from-ticket KAN-13
```

Fix every finding and run it again, until it says **Ready to review**. A
`D6_unlisted_value` means a value the ticket never gave.

Do not archive. A person reviews the draft; each requirement is then built
test first and delivered with `specgate apply REQ-NNN`, in commits that name KAN-13.
