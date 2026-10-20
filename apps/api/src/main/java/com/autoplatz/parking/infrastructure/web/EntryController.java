package com.autoplatz.parking.infrastructure.web;

import com.autoplatz.parking.application.ListEntryHistory;
import com.autoplatz.parking.application.ListEntryHistory.EntryInHistory;
import com.autoplatz.parking.application.ListVehiclesInGarage;
import com.autoplatz.parking.application.ListVehiclesInGarage.VehicleInGarage;
import com.autoplatz.parking.application.OpenEntryForm;
import com.autoplatz.parking.application.RegisterEntry;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.EntryForm;
import com.autoplatz.parking.domain.EntryFormField;
import com.autoplatz.parking.domain.LicensePlate;
import com.autoplatz.parking.domain.VehicleTypeId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Inbound REST adapter: translates HTTP ⇄ use cases. */
@RestController
@RequestMapping("/api/entries")
@Tag(name = "entries")
class EntryController {
  /** {@code licensePlate}: null for an entry recorded before plates were. */
  record EntryResponse(String id, String vehicleType, String licensePlate) {
    static EntryResponse of(EntryInHistory e) {
      return new EntryResponse(e.entry().id().toString(), e.vehicleType().name().value(),
          plateOf(e.entry().licensePlate()));
    }
  }

  /** Either may be empty or absent: the entry then is not saved, and the response names what is missing (REQ-015). */
  record RegisterEntryRequest(String licensePlate, String vehicleTypeId) {}

  record RegisteredEntryResponse(String id, String licensePlate, String vehicleTypeId) {
    static RegisteredEntryResponse of(Entry e) {
      return new RegisteredEntryResponse(e.id().toString(), plateOf(e.licensePlate()), e.vehicleType().toString());
    }
  }

  /** A vehicle currently in the garage (REQ-017); {@code status} as stored: {@code inside}. */
  record VehicleInGarageResponse(String id, String licensePlate, String vehicleType, String status) {
    static VehicleInGarageResponse of(VehicleInGarage v) {
      return new VehicleInGarageResponse(v.entry().id().toString(), plateOf(v.entry().licensePlate()),
          v.vehicleType().name().value(), v.entry().status().value());
    }
  }

  record EntryFormResponse(List<FieldResponse> fields) {
    /** {@code value}: what the field comes pre-filled with, as a local date-time to the minute; null if nothing. */
    record FieldResponse(String name, boolean required, List<OptionResponse> options, String value) {
      private static final DateTimeFormatter TO_THE_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

      static FieldResponse of(EntryFormField f) {
        return new FieldResponse(f.name(), f.required(),
            f.options().stream().map(t -> new OptionResponse(t.id().toString(), t.name().value())).toList(),
            f.prefilledWith() == null ? null : TO_THE_MINUTE.format(f.prefilledWith()));
      }
    }

    record OptionResponse(String id, String name) {}

    static EntryFormResponse of(EntryForm form) {
      return new EntryFormResponse(form.fields().stream().map(FieldResponse::of).toList());
    }
  }

  private final ListEntryHistory listEntryHistory;
  private final OpenEntryForm openEntryForm;
  private final RegisterEntry registerEntry;
  private final ListVehiclesInGarage listVehiclesInGarage;

  EntryController(ListEntryHistory listEntryHistory, OpenEntryForm openEntryForm, RegisterEntry registerEntry,
      ListVehiclesInGarage listVehiclesInGarage) {
    this.listEntryHistory = listEntryHistory;
    this.openEntryForm = openEntryForm;
    this.registerEntry = registerEntry;
    this.listVehiclesInGarage = listVehiclesInGarage;
  }

  @GetMapping
  @Operation(summary = "The entry history, each entry with its vehicle type, active or not")
  List<EntryResponse> history() {
    return listEntryHistory.execute().stream().map(EntryResponse::of).toList();
  }

  @GetMapping("/inside")
  @Operation(summary = "The vehicles currently in the garage: the entries with status \"inside\"")
  List<VehicleInGarageResponse> inside() {
    return listVehiclesInGarage.execute().stream().map(VehicleInGarageResponse::of).toList();
  }

  @GetMapping("/form")
  @Operation(summary = "The entry form: its fields, which are required, and the vehicle types it offers")
  EntryFormResponse form() {
    return EntryFormResponse.of(openEntryForm.execute());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Save a vehicle entry; its license plate is stored uppercase without spaces")
  RegisteredEntryResponse register(@Valid @RequestBody RegisterEntryRequest request) {
    return RegisteredEntryResponse.of(
        registerEntry.execute(request.licensePlate(), VehicleTypeId.ofPicked(request.vehicleTypeId())));
  }

  private static String plateOf(LicensePlate plate) {
    return plate == null ? null : plate.value();
  }
}
