package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.EntryId;
import com.autoplatz.parking.domain.LicensePlate;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Test double for the port: use cases are tested without Spring or a database. */
class InMemoryEntries implements EntryRepository {
  private final Map<EntryId, Entry> entries = new LinkedHashMap<>();

  @Override
  public void save(Entry entry) { entries.put(entry.id(), entry); }

  @Override
  public List<Entry> findAll() { return List.copyOf(entries.values()); }

  @Override
  public boolean anyWithVehicleType(VehicleTypeId type) {
    return entries.values().stream().anyMatch(e -> e.vehicleType().equals(type));
  }

  @Override
  public boolean anyOpenWithLicensePlate(LicensePlate plate) {
    return entries.values().stream().anyMatch(e -> e.isOpen() && plate.equals(e.licensePlate()));
  }
}
