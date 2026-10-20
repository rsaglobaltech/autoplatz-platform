package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import java.util.List;

/** Use case: the whole catalog, active and inactive. */
public final class ListVehicleTypes {
  private final VehicleTypeRepository types;

  public ListVehicleTypes(VehicleTypeRepository types) {
    this.types = types;
  }

  public List<VehicleType> execute() {
    return types.findAll();
  }
}
