# Delta — parking

## ADDED Requirements

### Requirement: REQ-012 — Entry form fields

<!-- specgate:trace status=Implemented actor=Administrator kind=functional -->

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

### Requirement: REQ-013 — Entry date/time pre-filled

<!-- specgate:trace status=Implemented actor=Administrator kind=functional -->

The system SHALL pre-fill the entry date/time field with the current date and
time when the entry form opens.

> Ticket: "Given the entry form, when it opens, then the entry date/time is pre-filled with the current date and time."

#### Scenario: SCN-023 — Entry date/time defaults to now

- GIVEN the system clock reads 2026-10-20 09:30
- WHEN the administrator opens the entry form
- THEN the entry date/time field is pre-filled with 2026-10-20 09:30

### Requirement: REQ-014 — License plate normalization

<!-- specgate:trace status=Implemented actor=Administrator kind=business-rule -->

The system SHALL store the license plate of a vehicle entry in uppercase with
all spaces removed.

> Ticket: "Given a plate typed with lowercase letters or spaces (e.g. "ab 123 cd"), when I save, then it is stored normalized as uppercase without spaces ("AB123CD")."

#### Scenario: SCN-024 — A lowercase plate with spaces is stored normalized

- GIVEN no entry with status "inside" exists for the license plate "AB123CD"
- AND the administrator types the license plate "ab 123 cd" and selects the vehicle type "Car"
- WHEN the administrator saves the entry
- THEN the entry is stored with the license plate "AB123CD"

### Requirement: REQ-015 — Required fields

<!-- specgate:trace status=Implemented actor=Administrator kind=business-rule -->

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

### Requirement: REQ-016 — One open entry per plate

<!-- specgate:trace status=Implemented actor=Administrator kind=business-rule -->

The system SHALL reject a new entry for a license plate that already has an
open entry, with the message "Vehicle is already in the garage", and SHALL
store no new entry.

> Ticket: "Given a plate that already has an open entry (vehicle still inside), when I try to register a new entry, then it is rejected with the message "Vehicle is already in the garage"."

#### Scenario: SCN-027 — A vehicle still inside cannot enter again

- GIVEN an entry for the license plate "AB123CD" with status "inside" exists
- WHEN the administrator registers a new entry for the license plate "AB123CD" with the vehicle type "Car"
- THEN the entry is rejected with the message "Vehicle is already in the garage"
- AND exactly one entry for the license plate "AB123CD" with status "inside" exists

### Requirement: REQ-017 — Valid entry is stored as inside

<!-- specgate:trace status=Implemented actor=Administrator kind=functional -->

The system SHALL store a valid vehicle entry in the database with status
"inside" and show it in the list of vehicles currently in the garage.

> Ticket: "Given valid input, when I save, then the entry is stored in the database with status "inside" and appears in the list of vehicles currently in the garage."

#### Scenario: SCN-028 — A valid entry is stored and listed

- GIVEN no entry with status "inside" exists for the license plate "AB123CD"
- AND the administrator types the license plate "AB123CD", selects the vehicle type "Car" and leaves the owner name empty
- WHEN the administrator saves the entry
- THEN the entry is stored in the database with status "inside"
- AND the license plate "AB123CD" appears in the list of vehicles currently in the garage
