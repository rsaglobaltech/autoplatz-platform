package com.autoplatz.parking.domain;

import java.util.Locale;

/** Where an entry's vehicle is. An entry is "open" while it is {@link #INSIDE} (REQ-016). */
public enum EntryStatus {
  INSIDE;

  /** As stored and as the scenarios name it: {@code inside}. */
  public String value() { return name().toLowerCase(Locale.ROOT); }

  public static EntryStatus of(String value) { return valueOf(value.toUpperCase(Locale.ROOT)); }
}
