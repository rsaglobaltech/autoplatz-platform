package com.autoplatz.parking.domain;

import java.util.List;

/**
 * The form an administrator fills in to register a vehicle's entry (REQ-012): a required license plate,
 * a required vehicle type picked from the types it offers, an optional owner name and the entry date/time.
 */
public record EntryForm(List<EntryFormField> fields) {
  public EntryForm {
    fields = List.copyOf(fields);
  }

  /** The form offering those vehicle types: the ones the catalog makes available. */
  public static EntryForm offering(List<VehicleType> vehicleTypes) {
    return new EntryForm(List.of(
        new EntryFormField("licensePlate", true, List.of()),
        new EntryFormField("vehicleType", true, vehicleTypes),
        new EntryFormField("ownerName", false, List.of()),
        new EntryFormField("enteredAt", false, List.of())));
  }

  public EntryFormField field(String name) {
    return fields.stream().filter(f -> f.name().equals(name)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("The entry form has no field " + name));
  }
}
