package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Use case (REQ-017): the vehicles currently in the garage, the entries still "inside", with their vehicle type. */
public final class ListVehiclesInGarage {
  public record VehicleInGarage(Entry entry, VehicleType vehicleType) {}

  private final EntryRepository entries;
  private final VehicleTypeRepository types;

  public ListVehiclesInGarage(EntryRepository entries, VehicleTypeRepository types) {
    this.entries = entries;
    this.types = types;
  }

  public List<VehicleInGarage> execute() {
    Map<VehicleTypeId, VehicleType> byId =
        types.findAll().stream().collect(Collectors.toMap(VehicleType::id, Function.identity()));
    return entries.findInside().stream().map(e -> new VehicleInGarage(e, byId.get(e.vehicleType()))).toList();
  }
}
