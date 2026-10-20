package com.autoplatz.parking.domain;

import java.util.UUID;

public record VehicleTypeId(UUID value) {
  public static VehicleTypeId random() { return new VehicleTypeId(UUID.randomUUID()); }

  @Override
  public String toString() { return value.toString(); }
}
