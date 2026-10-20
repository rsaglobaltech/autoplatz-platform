package com.autoplatz.parking.application;

import com.autoplatz.parking.application.port.EntryRepository;
import com.autoplatz.parking.application.port.VehicleTypeRepository;
import com.autoplatz.parking.domain.Entry;
import com.autoplatz.parking.domain.VehicleType;
import com.autoplatz.parking.domain.VehicleTypeId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Use case: the entries with their vehicle type, active or not (REQ-005: a deactivated type stays in history). */
public final class ListEntryHistory {
  public record EntryInHistory(Entry entry, VehicleType vehicleType) {}

  private final EntryRepository entries;
  private final VehicleTypeRepository types;

  public ListEntryHistory(EntryRepository entries, VehicleTypeRepository types) {
    this.entries = entries;
    this.types = types;
  }

  public List<EntryInHistory> execute() {
    Map<VehicleTypeId, VehicleType> byId =
        types.findAll().stream().collect(Collectors.toMap(VehicleType::id, Function.identity()));
    return entries.findAll().stream().map(e -> new EntryInHistory(e, byId.get(e.vehicleType()))).toList();
  }
}
