package com.autoplatz.parking.domain;

import com.autoplatz.shared.domain.DomainException;

public final class VehicleTypeNotFound extends DomainException {
  private VehicleTypeNotFound(String id) {
    super("vehicle_type_not_found", Kind.NOT_FOUND, "Vehicle type " + id + " does not exist.");
  }

  public static VehicleTypeNotFound withId(Object id) { return new VehicleTypeNotFound(String.valueOf(id)); }
}
