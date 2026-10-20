package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;

/**
 * Use case (REQ-002): on the first start of a fresh installation, the catalog gets the default types, active.
 * A catalog that already has types is not a fresh installation and is left as it is.
 */
public final class SeedDefaultVehicleTypes {
  private final VehicleTypeRepository types;

  public SeedDefaultVehicleTypes(VehicleTypeRepository types) {
    this.types = types;
  }

  public void execute() {
    if (!types.findAll().isEmpty()) return;
    VehicleType.defaults().forEach(types::save);
  }
}
