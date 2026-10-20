package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import com.autoplatz.shared.domain.DomainException;

/**
 * Use case (REQ-003): an administrator adds a type to the catalog; it is active, so the entry form offers it.
 * REQ-004: the name must not be empty nor taken by another type, active or not, ignoring case and spaces.
 */
public final class AddVehicleType {
  private final VehicleTypeRepository types;

  public AddVehicleType(VehicleTypeRepository types) {
    this.types = types;
  }

  public VehicleType execute(String name) {
    VehicleTypeName typeName = new VehicleTypeName(name);
    if (types.findAll().stream().anyMatch(t -> t.name().sameAs(typeName))) {
      throw new DomainException("duplicate_vehicle_type_name", DomainException.Kind.CONFLICT,
          "A vehicle type named \"" + typeName.value() + "\" already exists.");
    }
    VehicleType type = VehicleType.create(typeName);
    types.save(type);
    return type;
  }
}
