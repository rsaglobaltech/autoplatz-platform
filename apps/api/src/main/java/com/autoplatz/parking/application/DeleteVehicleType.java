package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import com.autoplatz.parking.domain.VehicleTypeNotFound;

/**
 * Use case (REQ-005): an administrator deletes a type. A type an existing entry uses is deactivated instead,
 * so the entry form no longer offers it and the history keeps showing it; a type no entry uses is removed.
 */
public final class DeleteVehicleType {
  private final VehicleTypeRepository types;
  private final EntryRepository entries;

  public DeleteVehicleType(VehicleTypeRepository types, EntryRepository entries) {
    this.types = types;
    this.entries = entries;
  }

  public void execute(String id) {
    VehicleTypeId typeId = VehicleTypeId.of(id);
    VehicleType type = types.findById(typeId).orElseThrow(() -> VehicleTypeNotFound.withId(id));
    if (entries.anyWithVehicleType(typeId)) {
      types.save(type.deactivate());
    } else {
      types.delete(typeId);
    }
  }
}
