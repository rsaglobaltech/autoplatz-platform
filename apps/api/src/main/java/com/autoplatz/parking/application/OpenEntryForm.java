package com.autoplatz.parking.application;

import com.autoplatz.parking.domain.EntryForm;

/** Use case (REQ-012): the entry form an administrator opens, its vehicle type field offering the available types. */
public final class OpenEntryForm {
  private final ListAvailableVehicleTypes availableTypes;

  public OpenEntryForm(ListAvailableVehicleTypes availableTypes) {
    this.availableTypes = availableTypes;
  }

  public EntryForm execute() {
    return EntryForm.offering(availableTypes.execute());
  }
}
