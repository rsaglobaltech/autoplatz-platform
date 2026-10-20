package com.autoplatz.parking.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeName;
import com.autoplatz.shared.domain.MissingRequiredFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequiredEntryFieldsUseCasesTest {
  private final InMemoryEntries entries = new InMemoryEntries();
  private final VehicleType car = VehicleType.create(new VehicleTypeName("Car"));

  @Test
  @DisplayName("REQ-015 SCN-025: an entry with no license plate is not stored and the plate is reported missing")
  void aMissingLicensePlateBlocksTheSave() {
    MissingRequiredFields missing = catchThrowableOfType(MissingRequiredFields.class,
        () -> new RegisterEntry(entries).execute("", car.id()));

    assertThat(missing.fields()).containsExactly("licensePlate");
    assertThat(entries.findAll()).isEmpty();
  }

  @Test
  @DisplayName("REQ-015 SCN-026: an entry with no vehicle type is not stored and the type is reported missing")
  void aMissingVehicleTypeBlocksTheSave() {
    MissingRequiredFields missing = catchThrowableOfType(MissingRequiredFields.class,
        () -> new RegisterEntry(entries).execute("AB123CD", null));

    assertThat(missing.fields()).containsExactly("vehicleType");
    assertThat(entries.findAll()).isEmpty();
  }

  @Test
  @DisplayName("REQ-015: a plate of only spaces is missing too, and every missing field is reported at once")
  void aBlankPlateIsMissingAndEveryMissingFieldIsReported() {
    MissingRequiredFields missing = catchThrowableOfType(MissingRequiredFields.class,
        () -> new RegisterEntry(entries).execute("   ", null));

    assertThat(missing.fields()).containsExactly("licensePlate", "vehicleType");
    assertThat(entries.findAll()).isEmpty();
  }
}
