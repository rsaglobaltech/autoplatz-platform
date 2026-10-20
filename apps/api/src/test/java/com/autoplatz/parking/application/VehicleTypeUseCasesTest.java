package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import com.autoplatz.shared.domain.DomainException;
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
  @DisplayName("REQ-003 SCN-003: a type with a unique name is added active and the entry form offers it")
  void anAddedTypeBecomesAvailableInTheEntryForm() {
    new SeedDefaultVehicleTypes(types).execute();

    VehicleType truck = new AddVehicleType(types).execute("Truck");

    assertThat(truck.name().value()).isEqualTo("Truck");
    assertThat(truck.active()).isTrue();
    assertThat(new ListVehicleTypes(types).execute())
        .filteredOn(t -> t.name().value().equals("Truck")).singleElement().matches(VehicleType::active);
    assertThat(new ListAvailableVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactlyInAnyOrder("Car", "Motorcycle", "Van", "Truck");
  }

  @Test
  @DisplayName("REQ-003: a type needs a non-empty name")
  void anEmptyNameIsNotAdded() {
    assertThatThrownBy(() -> new AddVehicleType(types).execute("  "))
        .isInstanceOf(DomainException.class).hasFieldOrPropertyWithValue("code", "invalid_vehicle_type_name");
    assertThat(new ListVehicleTypes(types).execute()).isEmpty();
  }

  @Test
  @DisplayName("REQ-004 SCN-004: a duplicate name is rejected and the catalog keeps one Car")
  void aDuplicateNameIsRejected() {
    new SeedDefaultVehicleTypes(types).execute();

    assertThatThrownBy(() -> new AddVehicleType(types).execute("Car"))
        .isInstanceOf(DomainException.class).hasMessageContaining("already exists")
        .hasFieldOrPropertyWithValue("code", "duplicate_vehicle_type_name");
    assertThat(new ListVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactly("Car", "Motorcycle", "Van");
  }

  @Test
  @DisplayName("REQ-004 SCN-005: an empty name is rejected as required and the catalog is unchanged")
  void anEmptyNameIsRejectedAsRequired() {
    new SeedDefaultVehicleTypes(types).execute();

    assertThatThrownBy(() -> new AddVehicleType(types).execute(""))
        .isInstanceOf(DomainException.class).hasMessageContaining("required")
        .hasFieldOrPropertyWithValue("code", "invalid_vehicle_type_name");
    assertThat(new ListVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactly("Car", "Motorcycle", "Van");
  }

  @Test
  @DisplayName("REQ-004 SCN-020: a duplicate in other case and surrounding spaces is rejected")
  void aDuplicateInOtherCaseAndSpacesIsRejected() {
    new SeedDefaultVehicleTypes(types).execute();

    assertThatThrownBy(() -> new AddVehicleType(types).execute(" car "))
        .isInstanceOf(DomainException.class).hasMessageContaining("already exists")
        .hasFieldOrPropertyWithValue("code", "duplicate_vehicle_type_name");
    assertThat(new ListVehicleTypes(types).execute())
        .extracting(t -> t.name().value()).containsExactly("Car", "Motorcycle", "Van");
  }

  @Test
  @DisplayName("REQ-004: the name of a deactivated type stays taken")
  void theNameOfAnInactiveTypeStaysTaken() {
    types.save(VehicleType.create(new VehicleTypeName("Van")).deactivate());

    assertThatThrownBy(() -> new AddVehicleType(types).execute("VAN"))
        .isInstanceOf(DomainException.class).hasFieldOrPropertyWithValue("code", "duplicate_vehicle_type_name");
    assertThat(new ListVehicleTypes(types).execute()).hasSize(1);
  }

  @Test
  @DisplayName("REQ-002: the entry form offers only active types")
  void theEntryFormOffersOnlyActiveTypes() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));
    types.save(VehicleType.create(new VehicleTypeName("Van")).deactivate());

    assertThat(new ListAvailableVehicleTypes(types).execute()).extracting(t -> t.name().value()).containsExactly("Car");
  }
}
