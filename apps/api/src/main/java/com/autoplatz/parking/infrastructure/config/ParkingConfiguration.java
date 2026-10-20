package com.autoplatz.parking.infrastructure.config;

import com.autoplatz.parking.application.ListAvailableVehicleTypes;
import com.autoplatz.parking.application.ListVehicleTypes;
import com.autoplatz.parking.application.SeedDefaultVehicleTypes;
import com.autoplatz.parking.application.port.VehicleTypeRepository;
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
  ListAvailableVehicleTypes listAvailableVehicleTypes(VehicleTypeRepository types) {
    return new ListAvailableVehicleTypes(types);
  }

  /** Runs the seeding use case when the system starts; what it seeds is the use case's business. */
  @Bean
  ApplicationRunner seedDefaultVehicleTypesOnStart(SeedDefaultVehicleTypes seed) {
    return args -> seed.execute();
  }
}
