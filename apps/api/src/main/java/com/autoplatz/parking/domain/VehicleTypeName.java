package com.autoplatz.parking.domain;

import com.autoplatz.shared.domain.DomainException;

/** Value object: a name of 1 to 60 characters, with no surrounding whitespace. */
public record VehicleTypeName(String value) {
  public static final int MAX = 60;

  public VehicleTypeName {
    value = value == null ? "" : value.strip();
    if (value.isEmpty() || value.length() > MAX) {
      throw new DomainException("invalid_vehicle_type_name", DomainException.Kind.INVALID,
          "The name must be between 1 and " + MAX + " characters.");
    }
  }
}
