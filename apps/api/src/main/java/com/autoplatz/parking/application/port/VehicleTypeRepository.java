package com.autoplatz.parking.application.port;

import com.autoplatz.parking.domain.VehicleType;
import java.util.List;

/** Outbound port: where the vehicle type catalog lives. Adapters in infrastructure/persistence. */
public interface VehicleTypeRepository {
  void save(VehicleType type);

  /** The whole catalog, active and inactive, by name. */
  List<VehicleType> findAll();
}
