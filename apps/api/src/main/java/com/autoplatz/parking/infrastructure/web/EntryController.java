package com.autoplatz.parking.infrastructure.web;

import com.autoplatz.parking.application.ListEntryHistory;
import com.autoplatz.parking.application.ListEntryHistory.EntryInHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Inbound REST adapter: translates HTTP ⇄ use cases. */
@RestController
@RequestMapping("/api/entries")
@Tag(name = "entries")
class EntryController {
  record EntryResponse(String id, String vehicleType) {
    static EntryResponse of(EntryInHistory e) {
      return new EntryResponse(e.entry().id().toString(), e.vehicleType().name().value());
    }
  }

  private final ListEntryHistory listEntryHistory;

  EntryController(ListEntryHistory listEntryHistory) {
    this.listEntryHistory = listEntryHistory;
  }

  @GetMapping
  @Operation(summary = "The entry history, each entry with its vehicle type, active or not")
  List<EntryResponse> history() {
    return listEntryHistory.execute().stream().map(EntryResponse::of).toList();
  }
}
