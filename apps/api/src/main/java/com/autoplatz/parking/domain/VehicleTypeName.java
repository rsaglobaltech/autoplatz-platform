package com.autoplatz.parking.domain;

import com.autoplatz.shared.domain.DomainException;
import java.util.Locale;

/** Value object: a name of 1 to 60 characters, with no surrounding whitespace. */
public record VehicleTypeName(String value) {
  public static final int MAX = 60;

  public VehicleTypeName {
    value = value == null ? "" : value.strip();
    if (value.isEmpty()) {
      throw new DomainException("invalid_vehicle_type_name", DomainException.Kind.INVALID, "The name is required.");
    }
    if (value.length() > MAX) {
      throw new DomainException("invalid_vehicle_type_name", DomainException.Kind.INVALID,
          "The name must be at most " + MAX + " characters.");
    }
  }

  /** Two names are the same type when they differ only in case (REQ-004); surrounding spaces are already gone. */
  public boolean sameAs(VehicleTypeName other) {
    return value.toLowerCase(Locale.ROOT).equals(other.value.toLowerCase(Locale.ROOT));
  }
}
