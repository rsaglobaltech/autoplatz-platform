package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Test double for the port: use cases are tested without Spring or a database. */
class InMemoryVehicleTypes implements VehicleTypeRepository {
  private final Map<VehicleTypeId, VehicleType> types = new LinkedHashMap<>();

  @Override
  public void save(VehicleType type) { types.put(type.id(), type); }

  @Override
  public List<VehicleType> findAll() {
    return types.values().stream().sorted(Comparator.comparing(t -> t.name().value())).toList();
  }

  @Override
  public Optional<VehicleType> findById(VehicleTypeId id) { return Optional.ofNullable(types.get(id)); }

  @Override
  public void delete(VehicleTypeId id) { types.remove(id); }
}
