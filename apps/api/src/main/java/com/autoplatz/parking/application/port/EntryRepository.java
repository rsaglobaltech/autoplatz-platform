package com.autoplatz.parking.application.port;

import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.LicensePlate;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.List;

/** Outbound port: where the entries live. Adapters in infrastructure/persistence. */
public interface EntryRepository {
  void save(Entry entry);

  List<Entry> findAll();

  /** The open entries, those still inside: the vehicles currently in the garage (REQ-017). */
  List<Entry> findInside();

  /** Whether at least one existing entry has that vehicle type (REQ-005). */
  boolean anyWithVehicleType(VehicleTypeId type);

  /** Whether that (normalized) plate has an open entry, one still inside (REQ-016). */
  boolean anyOpenWithLicensePlate(LicensePlate plate);
}
