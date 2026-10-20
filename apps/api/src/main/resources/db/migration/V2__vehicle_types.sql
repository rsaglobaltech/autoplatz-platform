CREATE TABLE vehicle_type (
  id     UUID PRIMARY KEY,
  name   VARCHAR(60) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL
);
