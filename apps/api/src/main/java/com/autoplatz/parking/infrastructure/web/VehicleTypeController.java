package com.autoplatz.parking.infrastructure.web;

import com.autoplatz.parking.application.AddVehicleType;
import com.autoplatz.parking.application.DeleteVehicleType;
import com.autoplatz.parking.application.ListAvailableVehicleTypes;
import com.autoplatz.parking.application.ListVehicleTypes;
import com.autoplatz.parking.domain.VehicleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Inbound REST adapter: translates HTTP ⇄ use cases. */
@RestController
@RequestMapping("/api/vehicle-types")
@Tag(name = "vehicle-types")
class VehicleTypeController {
  record AddVehicleTypeRequest(@NotNull String name) {}

  record VehicleTypeResponse(String id, String name, boolean active) {
    static VehicleTypeResponse of(VehicleType t) {
      return new VehicleTypeResponse(t.id().toString(), t.name().value(), t.active());
    }
  }

  private final ListVehicleTypes listVehicleTypes;
  private final ListAvailableVehicleTypes listAvailableVehicleTypes;
  private final AddVehicleType addVehicleType;
  private final DeleteVehicleType deleteVehicleType;

  VehicleTypeController(ListVehicleTypes listVehicleTypes, ListAvailableVehicleTypes listAvailableVehicleTypes,
      AddVehicleType addVehicleType, DeleteVehicleType deleteVehicleType) {
    this.listVehicleTypes = listVehicleTypes;
    this.listAvailableVehicleTypes = listAvailableVehicleTypes;
    this.addVehicleType = addVehicleType;
    this.deleteVehicleType = deleteVehicleType;
  }

  @GetMapping
  @Operation(summary = "The vehicle type catalog, active and inactive, by name")
  List<VehicleTypeResponse> catalog() {
    return listVehicleTypes.execute().stream().map(VehicleTypeResponse::of).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Add an active vehicle type to the catalog")
  VehicleTypeResponse add(@Valid @RequestBody AddVehicleTypeRequest request) {
    return VehicleTypeResponse.of(addVehicleType.execute(request.name()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a vehicle type; one an existing entry uses is deactivated instead")
  void delete(@PathVariable String id) {
    deleteVehicleType.execute(id);
  }

  @GetMapping("/available")
  @Operation(summary = "The vehicle types offered in the entry form, by name")
  List<VehicleTypeResponse> available() {
    return listAvailableVehicleTypes.execute().stream().map(VehicleTypeResponse::of).toList();
  }
}
