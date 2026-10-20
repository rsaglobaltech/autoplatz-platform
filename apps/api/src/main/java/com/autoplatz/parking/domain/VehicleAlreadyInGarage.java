package com.autoplatz.parking.domain;

import com.autoplatz.shared.domain.DomainException;

/** A plate already has an open entry, so a new one is refused (REQ-016). */
public final class VehicleAlreadyInGarage extends DomainException {
  public VehicleAlreadyInGarage() {
    super("vehicle_already_in_garage", Kind.CONFLICT, "Vehicle is already in the garage");
  }
}
