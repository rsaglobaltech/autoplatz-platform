# Open questions — KAN-14

Answered by the analyst on 2026-10-20.

| # | Question | Blocks | Answer |
|---|---|---|---|
| Q1 | Is the duplicate check case- and whitespace-insensitive ("car", " Car " duplicate "Car")? Is a name of only spaces "empty"? Does the name of a deactivated type count as taken? | REQ-004 | Yes to all three: compared ignoring case and surrounding spaces; only spaces is empty; a deactivated type's name stays taken. |
| Q2 | What happens when deleting a type that no existing entry uses — is it removed from the catalog? | REQ-005 | It is removed from the catalog. |
| Q3 | Can a deactivated type be reactivated, and by whom? | REQ-005 | Out of scope for KAN-14; a later ticket if needed. |
