package com.autoplatz.parking.domain;

import java.util.UUID;

public record VehicleTypeId(UUID value) {
  public static VehicleTypeId random() { return new VehicleTypeId(UUID.randomUUID()); }

  public static VehicleTypeId of(String text) {
    try {
      return new VehicleTypeId(UUID.fromString(text));
    } catch (IllegalArgumentException e) {
      throw VehicleTypeNotFound.withId(text);
    }
  }

  /** The id typed, or null when none was picked (empty or blank): a missing type is the entry's to refuse (REQ-015). */
  public static VehicleTypeId ofPicked(String text) {
    return text == null || text.isBlank() ? null : of(text);
  }

  @Override
  public String toString() { return value.toString(); }
}
