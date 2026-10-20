-- REQ-005: an entry refers to its vehicle type; a type an entry uses can be deactivated but not deleted.
CREATE TABLE parking_entry (
  id              UUID PRIMARY KEY,
  vehicle_type_id UUID NOT NULL REFERENCES vehicle_type (id)
);
CREATE INDEX parking_entry_vehicle_type_idx ON parking_entry (vehicle_type_id);
