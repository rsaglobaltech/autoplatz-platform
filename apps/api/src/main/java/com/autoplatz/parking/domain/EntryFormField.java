package com.autoplatz.parking.domain;

import java.util.List;

/** A field of the entry form: whether it must be filled in, and the vehicle types it offers, if it offers any. */
public record EntryFormField(String name, boolean required, List<VehicleType> options) {
  public EntryFormField {
    options = List.copyOf(options);
  }
}
