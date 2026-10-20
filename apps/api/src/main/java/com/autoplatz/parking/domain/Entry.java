package com.autoplatz.parking.domain;

/**
 * A vehicle's entry: it refers to its type by id, so the history keeps showing a type that was deactivated.
 * {@code licensePlate} is null only for entries recorded before plates were (REQ-014).
 */
public record Entry(EntryId id, VehicleTypeId vehicleType, LicensePlate licensePlate) {
  public static Entry create(VehicleTypeId vehicleType) {
    return new Entry(EntryId.random(), vehicleType, null);
  }

  public static Entry register(LicensePlate licensePlate, VehicleTypeId vehicleType) {
    return new Entry(EntryId.random(), vehicleType, licensePlate);
  }
}
