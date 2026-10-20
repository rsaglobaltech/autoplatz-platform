# KAN-13 — Register vehicle entry

<!-- specgate:ticket key=KAN-13 type=Story parent=KAN-5 url=https://specgatedemo.atlassian.net/browse/KAN-13 read=2026-10-20T16:15:12.600Z -->

Labels: garage-system, spec-2.1

As an administrator, I want to register a vehicle's entry so that I can track which vehicles are in the garage and since when.

Source: Spec §2.1 (Vehicle entry registration)

Acceptance Criteria

1. Given the entry form, when it is displayed, then it contains: license plate (required), vehicle type (required, selected from the catalog), owner name (optional) and entry date/time.
2. Given the entry form, when it opens, then the entry date/time is pre-filled with the current date and time.
3. Given a plate typed with lowercase letters or spaces (e.g. "ab 123 cd"), when I save, then it is stored normalized as uppercase without spaces ("AB123CD").
4. Given a required field is empty, when I save, then the entry is not saved and the missing field is highlighted.
5. Given a plate that already has an open entry (vehicle still inside), when I try to register a new entry, then it is rejected with the message "Vehicle is already in the garage".
6. Given valid input, when I save, then the entry is stored in the database with status "inside" and appears in the list of vehicles currently in the garage.
