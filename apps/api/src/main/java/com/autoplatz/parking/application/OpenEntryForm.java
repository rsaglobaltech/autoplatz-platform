package com.autoplatz.parking.application;

import com.autoplatz.parking.domain.EntryForm;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Use case (REQ-012, REQ-013): the entry form an administrator opens, its vehicle type field offering the
 * available types and its entry date/time pre-filled with the clock's current reading.
 */
public final class OpenEntryForm {
  private final ListAvailableVehicleTypes availableTypes;
  private final Clock clock;

  public OpenEntryForm(ListAvailableVehicleTypes availableTypes, Clock clock) {
    this.availableTypes = availableTypes;
    this.clock = clock;
  }

  public EntryForm execute() {
    return EntryForm.offering(availableTypes.execute(), LocalDateTime.now(clock));
  }
}
