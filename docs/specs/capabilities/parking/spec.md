# Parking

## Requirements

### Requirement: REQ-002 — Default vehicle types on first start

The system SHALL, on first start of a fresh installation, create the vehicle
types Car, Motorcycle and Van in the catalog, each active.

> Ticket: "Given a fresh installation, when the system starts, then default types exist: Car, Motorcycle, Van."

#### Scenario: SCN-002 — Fresh installation seeds the default types

- GIVEN a fresh installation with an empty vehicle type catalog
- WHEN the system starts
- THEN the catalog contains exactly the vehicle types "Car", "Motorcycle" and "Van"
- AND each of them is active and available in the entry form

<!-- specgate:trace actor=System feature=features/parking/REQ-002.feature kind=functional status=Implemented -->

### Requirement: REQ-003 — Add a vehicle type

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

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-003.feature kind=functional status=Implemented -->

### Requirement: REQ-004 — Vehicle type names are unique and not empty

The system SHALL reject saving a vehicle type whose name is empty or made
only of spaces, or duplicates — ignoring case and surrounding spaces — the
name of a type already in the catalog, active or not, SHALL return a
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

#### Scenario: SCN-020 — A duplicate in other case and spaces is rejected

- GIVEN the catalog contains "Car", "Motorcycle" and "Van"
- WHEN the administrator saves a type named " car "
- THEN the save is rejected with a validation message stating the name already exists
- AND the catalog still contains exactly one type named "Car"

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-004.feature kind=business-rule status=Implemented -->

### Requirement: REQ-005 — A type in use is deactivated, not deleted

The system SHALL, when an administrator deletes a vehicle type that is used
by at least one existing entry, deactivate the type instead of deleting it:
the type SHALL no longer be offered in the entry form, and the existing
entries SHALL keep showing it in history. A type no entry uses SHALL be
removed from the catalog.

> Ticket: "Given a type already used by existing entries, when I try to delete it, then it is deactivated instead of deleted (hidden from the form, kept in history)."

#### Scenario: SCN-006 — Deleting a used type deactivates it

- GIVEN the catalog contains the active type "Van"
- AND one existing entry has the vehicle type "Van"
- WHEN the administrator deletes the type "Van"
- THEN "Van" remains in the catalog marked as inactive
- AND the entry form no longer offers "Van"
- AND the existing entry still shows the vehicle type "Van" in history

#### Scenario: SCN-021 — Deleting an unused type removes it

- GIVEN the catalog contains the active type "Truck"
- AND no entry has the vehicle type "Truck"
- WHEN the administrator deletes the type "Truck"
- THEN the catalog no longer contains "Truck"
- AND the entry form no longer offers "Truck"

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-005.feature kind=business-rule status=Implemented -->

### Requirement: REQ-012 — Entry form fields

The system SHALL display an entry form containing a required license plate
field, a required vehicle type field selected from the vehicle type catalog,
an optional owner name field and an entry date/time field.

> Ticket: "Given the entry form, when it is displayed, then it contains: license plate (required), vehicle type (required, selected from the catalog), owner name (optional) and entry date/time."

#### Scenario: SCN-022 — The entry form shows its fields

- GIVEN the vehicle type catalog contains the active type "Car"
- WHEN the administrator opens the entry form and it is displayed
- THEN it contains a license plate field marked as required
- AND it contains a vehicle type field marked as required that offers the types of the vehicle type catalog, including "Car"
- AND it contains an owner name field marked as optional
- AND it contains an entry date/time field

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-012.feature kind=functional status=Implemented -->

### Requirement: REQ-013 — Entry date/time pre-filled

The system SHALL pre-fill the entry date/time field with the current date and
time when the entry form opens.

> Ticket: "Given the entry form, when it opens, then the entry date/time is pre-filled with the current date and time."

#### Scenario: SCN-023 — Entry date/time defaults to now

- GIVEN the system clock reads 2026-10-20 09:30
- WHEN the administrator opens the entry form
- THEN the entry date/time field is pre-filled with 2026-10-20 09:30

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-013.feature kind=functional status=Implemented -->

### Requirement: REQ-014 — License plate normalization

The system SHALL store the license plate of a vehicle entry in uppercase with
all spaces removed.

> Ticket: "Given a plate typed with lowercase letters or spaces (e.g. "ab 123 cd"), when I save, then it is stored normalized as uppercase without spaces ("AB123CD")."

#### Scenario: SCN-024 — A lowercase plate with spaces is stored normalized

- GIVEN no entry with status "inside" exists for the license plate "AB123CD"
- AND the administrator types the license plate "ab 123 cd" and selects the vehicle type "Car"
- WHEN the administrator saves the entry
- THEN the entry is stored with the license plate "AB123CD"

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-014.feature kind=business-rule status=Implemented -->

### Requirement: REQ-015 — Required fields

The system SHALL NOT save a vehicle entry while a required field (license
plate or vehicle type) is empty, and SHALL highlight each missing required
field.

> Ticket: "Given a required field is empty, when I save, then the entry is not saved and the missing field is highlighted."

#### Scenario: SCN-025 — Missing license plate blocks the save

- GIVEN the administrator leaves the license plate empty and selects the vehicle type "Car"
- WHEN the administrator saves the entry
- THEN no entry is stored
- AND the license plate field is highlighted as missing

#### Scenario: SCN-026 — Missing vehicle type blocks the save

- GIVEN the administrator types the license plate "AB123CD" and selects no vehicle type
- WHEN the administrator saves the entry
- THEN no entry is stored
- AND the vehicle type field is highlighted as missing

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-015.feature kind=business-rule status=Implemented -->

### Requirement: REQ-016 — One open entry per plate

The system SHALL reject a new entry for a license plate that already has an
open entry, with the message "Vehicle is already in the garage", and SHALL
store no new entry.

> Ticket: "Given a plate that already has an open entry (vehicle still inside), when I try to register a new entry, then it is rejected with the message "Vehicle is already in the garage"."

#### Scenario: SCN-027 — A vehicle still inside cannot enter again

- GIVEN an entry for the license plate "AB123CD" with status "inside" exists
- WHEN the administrator registers a new entry for the license plate "AB123CD" with the vehicle type "Car"
- THEN the entry is rejected with the message "Vehicle is already in the garage"
- AND exactly one entry for the license plate "AB123CD" with status "inside" exists

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-016.feature kind=business-rule status=Implemented -->

### Requirement: REQ-017 — Valid entry is stored as inside

The system SHALL store a valid vehicle entry in the database with status
"inside" and show it in the list of vehicles currently in the garage.

> Ticket: "Given valid input, when I save, then the entry is stored in the database with status "inside" and appears in the list of vehicles currently in the garage."

#### Scenario: SCN-028 — A valid entry is stored and listed

- GIVEN no entry with status "inside" exists for the license plate "AB123CD"
- AND the administrator types the license plate "AB123CD", selects the vehicle type "Car" and leaves the owner name empty
- WHEN the administrator saves the entry
- THEN the entry is stored in the database with status "inside"
- AND the license plate "AB123CD" appears in the list of vehicles currently in the garage

<!-- specgate:trace actor=Administrator feature=features/parking/REQ-017.feature kind=functional status=Implemented -->
