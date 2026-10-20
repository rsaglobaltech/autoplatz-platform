package com.autoplatz.parking.infrastructure.persistence;

import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import com.autoplatz.parking.domain.VehicleTypeName;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

/** JDBC adapter over PostgreSQL. Flyway creates the schema (db/migration). */
@Repository
class JdbcVehicleTypeRepository implements VehicleTypeRepository {
  private final JdbcClient jdbc;

  JdbcVehicleTypeRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

  @Override
  public void save(VehicleType t) {
    jdbc.sql("""
        INSERT INTO vehicle_type (id, name, active) VALUES (:id, :name, :active)
        ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, active = EXCLUDED.active
        """)
        .param("id", t.id().value()).param("name", t.name().value()).param("active", t.active())
        .update();
  }

  @Override
  public List<VehicleType> findAll() {
    return jdbc.sql("SELECT * FROM vehicle_type ORDER BY name, id").query(JdbcVehicleTypeRepository::row).list();
  }

  private static VehicleType row(ResultSet rs, int n) throws SQLException {
    return new VehicleType(new VehicleTypeId(rs.getObject("id", UUID.class)), new VehicleTypeName(rs.getString("name")),
        rs.getBoolean("active"));
  }
}
