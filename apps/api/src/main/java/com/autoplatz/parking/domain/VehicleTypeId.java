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

  @Override
  public String toString() { return value.toString(); }
}
