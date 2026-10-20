package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.EntryStatus;
import com.autoplatz.parking.domain.VehicleAlreadyInGarage;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OneOpenEntryPerPlateUseCasesTest {
  private final InMemoryEntries entries = new InMemoryEntries();
  private final VehicleType car = VehicleType.create(new VehicleTypeName("Car"));

  @Test
  @DisplayName("REQ-016 SCN-027: a vehicle still inside cannot enter again, and no second entry is stored")
  void aVehicleStillInsideCannotEnterAgain() {
    RegisterEntry register = new RegisterEntry(entries);
    Entry inside = register.execute("AB123CD", car.id());
    assertThat(inside.status()).isEqualTo(EntryStatus.INSIDE);

    VehicleAlreadyInGarage rejected = catchThrowableOfType(VehicleAlreadyInGarage.class,
        () -> register.execute("AB123CD", car.id()));

    assertThat(rejected).hasMessage("Vehicle is already in the garage");
    assertThat(entries.findAll())
        .filteredOn(e -> "AB123CD".equals(e.licensePlate().value()) && e.status() == EntryStatus.INSIDE)
        .singleElement().isEqualTo(inside);
  }

  @Test
  @DisplayName("REQ-016: the duplicate check compares normalized plates, so \"ab 123 cd\" matches an open \"AB123CD\"")
  void theDuplicateCheckComparesNormalizedPlates() {
    RegisterEntry register = new RegisterEntry(entries);
    register.execute("AB123CD", car.id());

    catchThrowableOfType(VehicleAlreadyInGarage.class, () -> register.execute("ab 123 cd", car.id()));

    assertThat(entries.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("REQ-016: another plate can still enter while one is inside")
  void anotherPlateCanStillEnter() {
    RegisterEntry register = new RegisterEntry(entries);
    register.execute("AB123CD", car.id());

    register.execute("XY987Z", car.id());

    assertThat(entries.findAll()).hasSize(2);
  }
}
