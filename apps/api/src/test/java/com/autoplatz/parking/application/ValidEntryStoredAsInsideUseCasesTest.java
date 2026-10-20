package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.parking.application.ListVehiclesInGarage.VehicleInGarage;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.EntryStatus;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidEntryStoredAsInsideUseCasesTest {
  private final InMemoryEntries entries = new InMemoryEntries();
  private final InMemoryVehicleTypes types = new InMemoryVehicleTypes();
  private final VehicleType car = VehicleType.create(new VehicleTypeName("Car"));

  @Test
  @DisplayName("REQ-017 SCN-028: a valid entry is stored as \"inside\" and listed among the vehicles in the garage")
  void aValidEntryIsStoredAndListed() {
    types.save(car);
    assertThat(new ListVehiclesInGarage(entries, types).execute()).isEmpty();

    Entry saved = new RegisterEntry(entries).execute("AB123CD", car.id());

    assertThat(entries.findAll()).singleElement().satisfies(e -> {
      assertThat(e.licensePlate().value()).isEqualTo("AB123CD");
      assertThat(e.status()).isEqualTo(EntryStatus.INSIDE);
    });
    assertThat(new ListVehiclesInGarage(entries, types).execute())
        .singleElement().isEqualTo(new VehicleInGarage(saved, car));
  }
}
