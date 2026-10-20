package com.autoplatz.parking.infrastructure.persistence;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.EntryId;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

/** JDBC adapter over PostgreSQL. Flyway creates the schema (db/migration). */
@Repository
class JdbcEntryRepository implements EntryRepository {
  private final JdbcClient jdbc;

  JdbcEntryRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

  @Override
  public void save(Entry e) {
    jdbc.sql("""
        INSERT INTO parking_entry (id, vehicle_type_id) VALUES (:id, :type)
        ON CONFLICT (id) DO UPDATE SET vehicle_type_id = EXCLUDED.vehicle_type_id
        """)
        .param("id", e.id().value()).param("type", e.vehicleType().value())
        .update();
  }

  @Override
  public List<Entry> findAll() {
    return jdbc.sql("SELECT * FROM parking_entry ORDER BY id").query(JdbcEntryRepository::row).list();
  }

  @Override
  public boolean anyWithVehicleType(VehicleTypeId type) {
    return jdbc.sql("SELECT EXISTS (SELECT 1 FROM parking_entry WHERE vehicle_type_id = :type)")
        .param("type", type.value()).query(Boolean.class).single();
  }

  private static Entry row(ResultSet rs, int n) throws SQLException {
    return new Entry(new EntryId(rs.getObject("id", UUID.class)),
        new VehicleTypeId(rs.getObject("vehicle_type_id", UUID.class)));
  }
}
