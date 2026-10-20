package com.autoplatz.parking.domain;

import java.util.Locale;

/** Value object (REQ-014): a license plate is kept uppercase with all spaces removed, however it was typed. */
public record LicensePlate(String value) {
  public LicensePlate {
    value = value == null ? "" : value.replaceAll("[\\s\\p{Z}]", "").toUpperCase(Locale.ROOT);
  }
}
