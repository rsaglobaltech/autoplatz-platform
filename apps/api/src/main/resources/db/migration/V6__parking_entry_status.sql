-- REQ-016: an entry has a status; it is open while "inside", and a plate may have only one open entry.
ALTER TABLE parking_entry ADD COLUMN status TEXT NOT NULL DEFAULT 'inside';
CREATE INDEX parking_entry_open_plate_idx ON parking_entry (license_plate) WHERE status = 'inside';
