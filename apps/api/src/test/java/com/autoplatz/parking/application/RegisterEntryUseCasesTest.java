package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterEntryUseCasesTest {
  private final InMemoryEntries entries = new InMemoryEntries();
  private final VehicleType car = VehicleType.create(new VehicleTypeName("Car"));

  @Test
  @DisplayName("REQ-014 SCN-024: a lowercase plate with spaces is stored uppercase without spaces")
  void aLowercasePlateWithSpacesIsStoredNormalized() {
    assertThat(entries.findAll()).noneMatch(e -> "AB123CD".equals(e.licensePlate().value()));

    Entry entry = new RegisterEntry(entries).execute("ab 123 cd", car.id());

    assertThat(entries.findAll()).singleElement().satisfies(stored -> {
      assertThat(stored.id()).isEqualTo(entry.id());
      assertThat(stored.licensePlate().value()).isEqualTo("AB123CD");
      assertThat(stored.vehicleType()).isEqualTo(car.id());
    });
  }

  @Test
  @DisplayName("REQ-014: every kind of whitespace is removed, and a plate already normalized is kept as it is")
  void anyWhitespaceIsRemovedAndANormalizedPlateIsKept() {
    RegisterEntry register = new RegisterEntry(entries);

    assertThat(register.execute(" ab\t12 3 cd ", car.id()).licensePlate().value()).isEqualTo("AB123CD");
    assertThat(register.execute("XY987Z", car.id()).licensePlate().value()).isEqualTo("XY987Z");
  }
}
