package com.autoplatz.shared.domain;

import java.util.List;

/**
 * Required fields were left empty, so nothing is saved. {@code fields} names each of them, as the form names
 * them, so the client can highlight every missing one (REQ-015).
 */
public class MissingRequiredFields extends DomainException {
  private final List<String> fields;

  public MissingRequiredFields(List<String> fields) {
    super("missing_required_fields", Kind.INVALID, "Required fields are missing: " + String.join(", ", fields) + ".");
    this.fields = List.copyOf(fields);
  }

  public List<String> fields() { return fields; }
}
