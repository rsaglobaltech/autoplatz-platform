# KAN-14 — Vehicle type catalog

<!-- specgate:ticket key=KAN-14 type=Story parent=KAN-5 url=https://specgatedemo.atlassian.net/browse/KAN-14 read=2026-10-20T15:12:31.409Z -->

Labels: derived, garage-system, spec-2.1

As an administrator, I want to manage a list of vehicle types so that entries use consistent values and rates can depend on the type if needed.

Source: Derived from Spec §2.1 ("vehicle type" field) — catalog not defined in the spec.

Acceptance Criteria

1. Given a fresh installation, when the system starts, then default types exist: Car, Motorcycle, Van.
2. Given the catalog screen, when I add a type with a unique name, then it becomes available in the entry form.
3. Given a duplicate or empty name, when I save, then it is rejected with a validation message.
4. Given a type already used by existing entries, when I try to delete it, then it is deactivated instead of deleted (hidden from the form, kept in history).
