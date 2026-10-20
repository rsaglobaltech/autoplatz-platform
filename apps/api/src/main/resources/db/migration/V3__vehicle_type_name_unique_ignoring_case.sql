-- REQ-004: a name is taken whatever its case (the application strips surrounding spaces).
CREATE UNIQUE INDEX vehicle_type_name_lower_key ON vehicle_type (lower(name));
