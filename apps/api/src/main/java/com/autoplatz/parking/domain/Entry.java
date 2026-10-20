package com.autoplatz.parking.domain;

/** A vehicle's entry: it refers to its type by id, so the history keeps showing a type that was deactivated. */
public record Entry(EntryId id, VehicleTypeId vehicleType) {
  public static Entry create(VehicleTypeId vehicleType) {
    return new Entry(EntryId.random(), vehicleType);
  }
}
