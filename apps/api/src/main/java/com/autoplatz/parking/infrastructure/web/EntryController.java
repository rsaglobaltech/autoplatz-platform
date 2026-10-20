package com.autoplatz.parking.infrastructure.web;

import com.autoplatz.parking.application.ListEntryHistory;
import com.autoplatz.parking.application.ListEntryHistory.EntryInHistory;
import com.autoplatz.parking.application.OpenEntryForm;
import com.autoplatz.parking.domain.EntryForm;
import com.autoplatz.parking.domain.EntryFormField;
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

  record EntryFormResponse(List<FieldResponse> fields) {
    record FieldResponse(String name, boolean required, List<OptionResponse> options) {
      static FieldResponse of(EntryFormField f) {
        return new FieldResponse(f.name(), f.required(),
            f.options().stream().map(t -> new OptionResponse(t.id().toString(), t.name().value())).toList());
      }
    }

    record OptionResponse(String id, String name) {}

    static EntryFormResponse of(EntryForm form) {
      return new EntryFormResponse(form.fields().stream().map(FieldResponse::of).toList());
    }
  }

  private final ListEntryHistory listEntryHistory;
  private final OpenEntryForm openEntryForm;

  EntryController(ListEntryHistory listEntryHistory, OpenEntryForm openEntryForm) {
    this.listEntryHistory = listEntryHistory;
    this.openEntryForm = openEntryForm;
  }

  @GetMapping
  @Operation(summary = "The entry history, each entry with its vehicle type, active or not")
  List<EntryResponse> history() {
    return listEntryHistory.execute().stream().map(EntryResponse::of).toList();
  }

  @GetMapping("/form")
  @Operation(summary = "The entry form: its fields, which are required, and the vehicle types it offers")
  EntryFormResponse form() {
    return EntryFormResponse.of(openEntryForm.execute());
  }
}
