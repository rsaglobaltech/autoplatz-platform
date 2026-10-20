# Delta — parking

## ADDED Requirements

### Requirement: REQ-002 — Default vehicle types on first start

<!-- specgate:trace status=Implemented actor=System kind=functional -->

The system SHALL, on first start of a fresh installation, create the vehicle
types Car, Motorcycle and Van in the catalog, each active.

> Ticket: "Given a fresh installation, when the system starts, then default types exist: Car, Motorcycle, Van."

#### Scenario: SCN-002 — Fresh installation seeds the default types

- GIVEN a fresh installation with an empty vehicle type catalog
- WHEN the system starts
- THEN the catalog contains exactly the vehicle types "Car", "Motorcycle" and "Van"
- AND each of them is active and available in the entry form

### Requirement: REQ-003 — Add a vehicle type

<!-- specgate:trace status=Implemented actor=Administrator kind=functional -->

The system SHALL let an administrator add a vehicle type with a unique,
non-empty name from the catalog screen, and SHALL make the new type
available for selection in the entry form.

> Ticket: "Given the catalog screen, when I add a type with a unique name, then it becomes available in the entry form."

#### Scenario: SCN-003 — A type with a unique name becomes available in the entry form

- GIVEN the catalog contains "Car", "Motorcycle" and "Van"
- AND the administrator is on the catalog screen
- WHEN the administrator adds a type named "Truck"
- THEN the catalog contains "Truck" as an active type
- AND the entry form offers "Car", "Motorcycle", "Van" and "Truck"

### Requirement: REQ-004 — Vehicle type names are unique and not empty

<!-- specgate:trace actor=Administrator kind=business-rule status="Needs Clarification" -->

The system SHALL reject saving a vehicle type whose name is empty or
duplicates the name of a type already in the catalog, SHALL return a
validation message, and SHALL leave the catalog unchanged.

> Ticket: "Given a duplicate or empty name, when I save, then it is rejected with a validation message."

#### Scenario: SCN-004 — A duplicate name is rejected

- GIVEN the catalog contains "Car", "Motorcycle" and "Van"
- WHEN the administrator saves a type named "Car"
- THEN the save is rejected with a validation message stating the name already exists
- AND the catalog still contains exactly one type named "Car"

#### Scenario: SCN-005 — An empty name is rejected

- GIVEN the catalog contains "Car", "Motorcycle" and "Van"
- WHEN the administrator saves a type with the name ""
- THEN the save is rejected with a validation message stating the name is required
- AND the catalog still contains exactly "Car", "Motorcycle" and "Van"

### Requirement: REQ-005 — A type in use is deactivated, not deleted

<!-- specgate:trace actor=Administrator kind=business-rule status="Needs Clarification" -->

The system SHALL, when an administrator deletes a vehicle type that is used
by at least one existing entry, deactivate the type instead of deleting it:
the type SHALL no longer be offered in the entry form, and the existing
entries SHALL keep showing it in history.

> Ticket: "Given a type already used by existing entries, when I try to delete it, then it is deactivated instead of deleted (hidden from the form, kept in history)."

#### Scenario: SCN-006 — Deleting a used type deactivates it

- GIVEN the catalog contains the active type "Van"
- AND one existing entry has the vehicle type "Van"
- WHEN the administrator deletes the type "Van"
- THEN "Van" remains in the catalog marked as inactive
- AND the entry form no longer offers "Van"
- AND the existing entry still shows the vehicle type "Van" in history
