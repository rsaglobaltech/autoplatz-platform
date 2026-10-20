package com.autoplatz.parking.domain;

import java.util.UUID;

public record EntryId(UUID value) {
  public static EntryId random() { return new EntryId(UUID.randomUUID()); }

  @Override
  public String toString() { return value.toString(); }
}
