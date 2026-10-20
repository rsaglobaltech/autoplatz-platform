package com.autoplatz.parking.application.port;

import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.List;

/** Outbound port: where the entries live. Adapters in infrastructure/persistence. */
public interface EntryRepository {
  void save(Entry entry);

  List<Entry> findAll();

  /** Whether at least one existing entry has that vehicle type (REQ-005). */
  boolean anyWithVehicleType(VehicleTypeId type);
}
