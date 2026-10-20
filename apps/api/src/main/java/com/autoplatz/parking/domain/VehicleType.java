package com.autoplatz.parking.domain;

import java.util.List;

/** A vehicle type of the catalog. Only active types are offered in the entry form. Pure Java: no framework. */
public record VehicleType(VehicleTypeId id, VehicleTypeName name, boolean active) {

  /** The types a fresh installation starts with (REQ-002). */
  public static final List<String> DEFAULT_NAMES = List.of("Car", "Motorcycle", "Van");

  public static VehicleType create(VehicleTypeName name) {
    return new VehicleType(VehicleTypeId.random(), name, true);
  }

  public static List<VehicleType> defaults() {
    return DEFAULT_NAMES.stream().map(n -> create(new VehicleTypeName(n))).toList();
  }

  public VehicleType deactivate() { return new VehicleType(id, name, false); }
}
