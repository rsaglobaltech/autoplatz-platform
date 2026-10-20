package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;

/** Use case (REQ-003): an administrator adds a type to the catalog; it is active, so the entry form offers it. */
public final class AddVehicleType {
  private final VehicleTypeRepository types;

  public AddVehicleType(VehicleTypeRepository types) {
    this.types = types;
  }

  public VehicleType execute(String name) {
    VehicleType type = VehicleType.create(new VehicleTypeName(name));
    types.save(type);
    return type;
  }
}
