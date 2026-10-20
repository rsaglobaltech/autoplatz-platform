package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.LicensePlate;
import com.autoplatz.parking.domain.VehicleTypeId;

/** Use case (REQ-014): an administrator saves an entry; its plate is stored uppercase without spaces. */
public final class RegisterEntry {
  private final EntryRepository entries;

  public RegisterEntry(EntryRepository entries) {
    this.entries = entries;
  }

  public Entry execute(String licensePlate, VehicleTypeId vehicleType) {
    Entry entry = Entry.register(new LicensePlate(licensePlate), vehicleType);
    entries.save(entry);
    return entry;
  }
}
