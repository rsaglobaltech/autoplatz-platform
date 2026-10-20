package com.autoplatz.parking.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The form an administrator fills in to register a vehicle's entry (REQ-012): a required license plate,
 * a required vehicle type picked from the types it offers, an optional owner name and the entry date/time,
 * pre-filled with the moment the form opens (REQ-013).
 */
public record EntryForm(List<EntryFormField> fields) {
  public EntryForm {
    fields = List.copyOf(fields);
  }

  /** The form offering those vehicle types, opened at {@code now}: its entry date/time reads now, to the minute. */
  public static EntryForm offering(List<VehicleType> vehicleTypes, LocalDateTime now) {
    return new EntryForm(List.of(
        new EntryFormField("licensePlate", true, List.of()),
        new EntryFormField("vehicleType", true, vehicleTypes),
        new EntryFormField("ownerName", false, List.of()),
        new EntryFormField("enteredAt", false, List.of(), now.truncatedTo(ChronoUnit.MINUTES))));
  }

  public EntryFormField field(String name) {
    return fields.stream().filter(f -> f.name().equals(name)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("The entry form has no field " + name));
  }
}
