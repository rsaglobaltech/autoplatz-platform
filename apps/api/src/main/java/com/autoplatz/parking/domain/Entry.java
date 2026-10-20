package com.autoplatz.parking.domain;

import com.autoplatz.shared.domain.MissingRequiredFields;
import java.util.ArrayList;
import java.util.List;

/**
 * A vehicle's entry: it refers to its type by id, so the history keeps showing a type that was deactivated.
 * {@code licensePlate} is null only for entries recorded before plates were (REQ-014).
 */
public record Entry(EntryId id, VehicleTypeId vehicleType, LicensePlate licensePlate) {
  public static Entry create(VehicleTypeId vehicleType) {
    return new Entry(EntryId.random(), vehicleType, null);
  }

  /** A new entry; refused, naming each one, while the license plate or the vehicle type is missing (REQ-015). */
  public static Entry register(LicensePlate licensePlate, VehicleTypeId vehicleType) {
    List<String> missing = new ArrayList<>();
    if (licensePlate == null || licensePlate.isEmpty()) missing.add("licensePlate");
    if (vehicleType == null) missing.add("vehicleType");
    if (!missing.isEmpty()) throw new MissingRequiredFields(missing);
    return new Entry(EntryId.random(), vehicleType, licensePlate);
  }
}
