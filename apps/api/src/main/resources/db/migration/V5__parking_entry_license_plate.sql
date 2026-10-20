-- REQ-014: an entry records its license plate, normalized (uppercase, no spaces). Entries from before have none.
ALTER TABLE parking_entry ADD COLUMN license_plate TEXT;
