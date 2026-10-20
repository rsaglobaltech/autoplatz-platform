package com.autoplatz.parking.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A field of the entry form: whether it must be filled in, the vehicle types it offers, if it offers any,
 * and the date/time it comes pre-filled with, if any (null otherwise).
 */
public record EntryFormField(String name, boolean required, List<VehicleType> options, LocalDateTime prefilledWith) {
  public EntryFormField {
    options = List.copyOf(options);
  }

  public EntryFormField(String name, boolean required, List<VehicleType> options) {
    this(name, required, options, null);
  }
}
