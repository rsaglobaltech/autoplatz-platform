package com.autoplatz.parking.infrastructure.config;

import com.autoplatz.parking.application.AddVehicleType;
import com.autoplatz.parking.application.DeleteVehicleType;
import com.autoplatz.parking.application.ListAvailableVehicleTypes;
import com.autoplatz.parking.application.ListEntryHistory;
import com.autoplatz.parking.application.ListVehicleTypes;
import com.autoplatz.parking.application.OpenEntryForm;
import com.autoplatz.parking.application.SeedDefaultVehicleTypes;
import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.application.port.VehicleTypeRepository;
import java.time.Clock;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The only place that knows both the use cases and Spring: they are wired here. */
@Configuration(proxyBeanMethods = false)
class ParkingConfiguration {
  @Bean
  SeedDefaultVehicleTypes seedDefaultVehicleTypes(VehicleTypeRepository types) {
    return new SeedDefaultVehicleTypes(types);
  }

  @Bean
  ListVehicleTypes listVehicleTypes(VehicleTypeRepository types) {
    return new ListVehicleTypes(types);
  }

  @Bean
  AddVehicleType addVehicleType(VehicleTypeRepository types) {
    return new AddVehicleType(types);
  }

  @Bean
  ListAvailableVehicleTypes listAvailableVehicleTypes(VehicleTypeRepository types) {
    return new ListAvailableVehicleTypes(types);
  }

  @Bean
  OpenEntryForm openEntryForm(ListAvailableVehicleTypes listAvailableVehicleTypes, Clock clock) {
    return new OpenEntryForm(listAvailableVehicleTypes, clock);
  }

  @Bean
  DeleteVehicleType deleteVehicleType(VehicleTypeRepository types, EntryRepository entries) {
    return new DeleteVehicleType(types, entries);
  }

  @Bean
  ListEntryHistory listEntryHistory(EntryRepository entries, VehicleTypeRepository types) {
    return new ListEntryHistory(entries, types);
  }

  /** Runs the seeding use case when the system starts; what it seeds is the use case's business. */
  @Bean
  ApplicationRunner seedDefaultVehicleTypesOnStart(SeedDefaultVehicleTypes seed) {
    return args -> seed.execute();
  }
}
