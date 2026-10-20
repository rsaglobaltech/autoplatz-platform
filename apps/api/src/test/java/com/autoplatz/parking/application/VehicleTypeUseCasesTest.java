package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleTypeUseCasesTest {
  private final InMemoryVehicleTypes types = new InMemoryVehicleTypes();

  @Test
  @DisplayName("REQ-002 SCN-002: a fresh installation seeds Car, Motorcycle and Van, active and in the entry form")
  void freshInstallationSeedsTheDefaultTypes() {
    assertThat(new ListVehicleTypes(types).execute()).isEmpty();

    new SeedDefaultVehicleTypes(types).execute();

    assertThat(new ListVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactly("Car", "Motorcycle", "Van");
    assertThat(new ListVehicleTypes(types).execute()).allMatch(VehicleType::active);
    assertThat(new ListAvailableVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactly("Car", "Motorcycle", "Van");
  }

  @Test
  @DisplayName("REQ-002: the defaults are created once, a later start does not add them again")
  void aLaterStartDoesNotSeedAgain() {
    new SeedDefaultVehicleTypes(types).execute();
    new SeedDefaultVehicleTypes(types).execute();

    assertThat(new ListVehicleTypes(types).execute()).hasSize(3);
  }

  @Test
  @DisplayName("REQ-002: a catalog that is not empty is not a fresh installation and is left alone")
  void aCatalogThatIsNotEmptyIsNotSeeded() {
    types.save(VehicleType.create(new VehicleTypeName("Truck")));

    new SeedDefaultVehicleTypes(types).execute();

    assertThat(new ListVehicleTypes(types).execute()).extracting(t -> t.name().value()).containsExactly("Truck");
  }

  @Test
  @DisplayName("REQ-002: the entry form offers only active types")
  void theEntryFormOffersOnlyActiveTypes() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));
    types.save(VehicleType.create(new VehicleTypeName("Van")).deactivate());

    assertThat(new ListAvailableVehicleTypes(types).execute()).extracting(t -> t.name().value()).containsExactly("Car");
  }
}
