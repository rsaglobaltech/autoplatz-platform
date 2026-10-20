package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.parking.domain.EntryForm;
import com.autoplatz.parking.domain.EntryFormField;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntryFormUseCasesTest {
  private final InMemoryVehicleTypes types = new InMemoryVehicleTypes();

  @Test
  @DisplayName("REQ-012 SCN-022: the entry form has a required plate, a required type from the catalog, an optional owner and the entry date/time")
  void theEntryFormShowsItsFields() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types)).execute();

    assertThat(form.fields()).extracting(EntryFormField::name)
        .containsExactly("licensePlate", "vehicleType", "ownerName", "enteredAt");
    assertThat(form.field("licensePlate").required()).isTrue();
    assertThat(form.field("vehicleType").required()).isTrue();
    assertThat(form.field("vehicleType").options()).extracting(t -> t.name().value()).containsExactly("Car");
    assertThat(form.field("ownerName").required()).isFalse();
    assertThat(form.field("enteredAt")).isNotNull();
  }

  @Test
  @DisplayName("REQ-012: the vehicle type field offers only the active types of the catalog")
  void theVehicleTypeFieldOffersOnlyActiveTypes() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));
    types.save(VehicleType.create(new VehicleTypeName("Van")).deactivate());

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types)).execute();

    assertThat(form.field("vehicleType").options()).extracting(t -> t.name().value()).containsExactly("Car");
  }

  @Test
  @DisplayName("REQ-012: only the vehicle type field offers options")
  void onlyTheVehicleTypeFieldOffersOptions() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types)).execute();

    assertThat(form.fields()).filteredOn(f -> !f.name().equals("vehicleType"))
        .allSatisfy(f -> assertThat(f.options()).isEmpty());
  }
}
