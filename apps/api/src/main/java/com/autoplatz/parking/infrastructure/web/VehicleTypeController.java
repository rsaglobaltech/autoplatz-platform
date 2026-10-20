package com.autoplatz.parking.infrastructure.web;

import com.autoplatz.parking.application.ListAvailableVehicleTypes;
import com.autoplatz.parking.application.ListVehicleTypes;
import com.autoplatz.parking.domain.VehicleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Inbound REST adapter: translates HTTP ⇄ use cases. */
@RestController
@RequestMapping("/api/vehicle-types")
@Tag(name = "vehicle-types")
class VehicleTypeController {
  record VehicleTypeResponse(String id, String name, boolean active) {
    static VehicleTypeResponse of(VehicleType t) {
      return new VehicleTypeResponse(t.id().toString(), t.name().value(), t.active());
    }
  }

  private final ListVehicleTypes listVehicleTypes;
  private final ListAvailableVehicleTypes listAvailableVehicleTypes;

  VehicleTypeController(ListVehicleTypes listVehicleTypes, ListAvailableVehicleTypes listAvailableVehicleTypes) {
    this.listVehicleTypes = listVehicleTypes;
    this.listAvailableVehicleTypes = listAvailableVehicleTypes;
  }

  @GetMapping
  @Operation(summary = "The vehicle type catalog, active and inactive, by name")
  List<VehicleTypeResponse> catalog() {
    return listVehicleTypes.execute().stream().map(VehicleTypeResponse::of).toList();
  }

  @GetMapping("/available")
  @Operation(summary = "The vehicle types offered in the entry form, by name")
  List<VehicleTypeResponse> available() {
    return listAvailableVehicleTypes.execute().stream().map(VehicleTypeResponse::of).toList();
  }
}
