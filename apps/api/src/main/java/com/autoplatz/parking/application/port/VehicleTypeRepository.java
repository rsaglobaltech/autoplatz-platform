package com.autoplatz.parking.application.port;

import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.List;
import java.util.Optional;

/** Outbound port: where the vehicle type catalog lives. Adapters in infrastructure/persistence. */
public interface VehicleTypeRepository {
  void save(VehicleType type);

  /** The whole catalog, active and inactive, by name. */
  List<VehicleType> findAll();

  Optional<VehicleType> findById(VehicleTypeId id);

  void delete(VehicleTypeId id);
}
