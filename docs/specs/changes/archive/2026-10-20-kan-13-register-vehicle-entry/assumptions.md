# Assumptions — KAN-13

Values and readings used in scenarios that the ticket does not give.

| # | Assumption | Used in | Why | Status |
|---|---|---|---|---|
| A1 | The plate `AB123CD` is the concrete plate for "a plate" / "valid input" | REQ-014 (SCN-024), REQ-015 (SCN-026), REQ-016 (SCN-027), REQ-017 (SCN-028) | The ticket gives no other plate; reusing its normalized example keeps the values traceable | to confirm |
| A2 | `Car` (a default type of the KAN-14 catalog) is the concrete vehicle type selected in the form | REQ-012 (SCN-022), REQ-014 (SCN-024), REQ-015 (SCN-025), REQ-016 (SCN-027), REQ-017 (SCN-028) | The ticket says "selected from the catalog" without a value | to confirm |
| A3 | A system clock reading of `2026-10-20 09:30` stands for "the current date and time" | REQ-013 (SCN-023) | A test needs a fixed clock; the ticket gives no date/time or format | to confirm |
| A4 | "A required field is empty" is checked separately for license plate and for vehicle type, one scenario each; entry date/time is not one of the required fields | REQ-015 (SCN-025, SCN-026) | Criterion 1 marks exactly these two fields as required and lists entry date/time without "required" | to confirm |
| A5 | An entry is "open" while its status is `inside` | REQ-016 (SCN-027), REQ-014 (SCN-024), REQ-017 (SCN-028) | Criterion 5 equates an open entry with "vehicle still inside"; criterion 6 names the status `inside` | to confirm |
| A6 | The duplicate check compares normalized plates, so `ab 123 cd` matches an open `AB123CD` | REQ-016 | Plates are stored normalized (criterion 3); comparing raw input would let duplicates through | to confirm |
| A7 | A rejected duplicate stores no second entry | REQ-016 (SCN-027) | "Rejected" read as nothing saved; stated so a test can assert it | to confirm |
| A8 | Owner name may be left empty in a valid entry | REQ-017 (SCN-028) | Criterion 1 marks it optional | to confirm |
