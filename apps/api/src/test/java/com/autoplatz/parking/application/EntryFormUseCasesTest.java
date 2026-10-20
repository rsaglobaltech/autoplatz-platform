package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.parking.domain.EntryForm;
import com.autoplatz.parking.domain.EntryFormField;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntryFormUseCasesTest {
  private final InMemoryVehicleTypes types = new InMemoryVehicleTypes();
  private final Clock clock = Clock.fixed(Instant.parse("2026-10-20T09:30:00Z"), ZoneOffset.UTC);

  @Test
  @DisplayName("REQ-012 SCN-022: the entry form has a required plate, a required type from the catalog, an optional owner and the entry date/time")
  void theEntryFormShowsItsFields() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), clock).execute();

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

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), clock).execute();

    assertThat(form.field("vehicleType").options()).extracting(t -> t.name().value()).containsExactly("Car");
  }

  @Test
  @DisplayName("REQ-012: only the vehicle type field offers options")
  void onlyTheVehicleTypeFieldOffersOptions() {
    types.save(VehicleType.create(new VehicleTypeName("Car")));

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), clock).execute();

    assertThat(form.fields()).filteredOn(f -> !f.name().equals("vehicleType"))
        .allSatisfy(f -> assertThat(f.options()).isEmpty());
  }

  @Test
  @DisplayName("REQ-013 SCN-023: the entry date/time is pre-filled with the current date and time")
  void theEntryDateTimeDefaultsToNow() {
    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), clock).execute();

    assertThat(form.field("enteredAt").prefilledWith()).isEqualTo(LocalDateTime.of(2026, 10, 20, 9, 30));
  }

  @Test
  @DisplayName("REQ-013: the entry date/time is pre-filled to the minute, the way the field shows it")
  void theEntryDateTimeIsPrefilledToTheMinute() {
    Clock midMinute = Clock.fixed(Instant.parse("2026-10-20T09:30:42.517Z"), ZoneOffset.UTC);

    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), midMinute).execute();

    assertThat(form.field("enteredAt").prefilledWith()).isEqualTo(LocalDateTime.of(2026, 10, 20, 9, 30));
  }

  @Test
  @DisplayName("REQ-013: only the entry date/time field is pre-filled")
  void onlyTheEntryDateTimeIsPrefilled() {
    EntryForm form = new OpenEntryForm(new ListAvailableVehicleTypes(types), clock).execute();

    assertThat(form.fields()).filteredOn(f -> !f.name().equals("enteredAt"))
        .allSatisfy(f -> assertThat(f.prefilledWith()).isNull());
  }
}
