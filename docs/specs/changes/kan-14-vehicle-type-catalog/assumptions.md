# Assumptions — KAN-14

Values used in scenarios that the ticket does not give.

| # | Assumption | Used in | Why | Status |
|---|---|---|---|---|
| A1 | "Fresh installation" means the vehicle type catalog is empty before the first start; defaults are created once, active | REQ-002 (SCN-002) | Ticket gives the names only, not the starting state | to confirm |
| A2 | "Truck" as the example of a unique new type name | REQ-003 (SCN-003), REQ-005 (SCN-021) | Ticket says "a type with a unique name" without a value | to confirm |
| A3 | "Available in the entry form" means listed among the selectable vehicle types of the entry form, and only active types are listed | REQ-002, REQ-003, REQ-005 (SCN-002, SCN-003, SCN-006) | Ticket names the form but not how a type appears in it | to confirm |
| A4 | "Car" (a default type) as the example of a duplicate name | REQ-004 (SCN-004) | Ticket says "a duplicate name" without a value | to confirm |
| A5 | The empty string "" as the example of an empty name | REQ-004 (SCN-005) | Ticket says "an empty name" without a value | to confirm |
| A6 | Validation message content: "name already exists" for a duplicate, "name is required" for an empty name; exact wording is not asserted | REQ-004 (SCN-004, SCN-005) | Ticket requires "a validation message" but gives no text | to confirm |
| A7 | A rejected save leaves the catalog unchanged | REQ-004 (SCN-004, SCN-005) | Follows from "rejected"; stated so a test can assert it | to confirm |
| A8 | "Van" used by one existing entry as the example of a type in use | REQ-005 (SCN-006) | Ticket says "a type already used by existing entries" without a value | to confirm |
| A9 | Deactivated types stay in the catalog marked inactive; "kept in history" means existing entries still show their type | REQ-005 (SCN-006) | Ticket says "deactivated … kept in history" without defining what is shown | to confirm |
