package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import java.util.List;

/** Use case: the types the entry form offers, which are the active ones. */
public final class ListAvailableVehicleTypes {
  private final VehicleTypeRepository types;

  public ListAvailableVehicleTypes(VehicleTypeRepository types) {
    this.types = types;
  }

  public List<VehicleType> execute() {
    return types.findAll().stream().filter(VehicleType::active).toList();
  }
}
