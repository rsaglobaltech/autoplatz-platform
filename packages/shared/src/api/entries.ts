/** What the Java API returns (EntryFormResponse). Keep in sync with apps/api, or generate it from the OpenAPI spec. */
export interface EntryForm {
  fields: EntryFormField[];
}

export interface EntryFormField {
  name: string;
  required: boolean;
  options: { id: string; name: string }[];
  /** What the field comes pre-filled with (entry date/time: `YYYY-MM-DDTHH:mm`), or null. */
  value: string | null;
}

/**
 * What POST /api/entries takes (RegisterEntryRequest). Either may be empty: the API then answers 422
 * `missing_required_fields`, with `fields` naming each missing form field (REQ-015).
 */
export interface RegisterEntryRequest {
  licensePlate: string;
  vehicleTypeId: string;
}

/** What GET /api/entries/inside returns (VehicleInGarageResponse): a vehicle currently in the garage (REQ-017). */
export interface VehicleInGarage {
  id: string;
  /** Null only for an entry recorded before plates were. */
  licensePlate: string | null;
  vehicleType: string;
  status: "inside";
}

/** What POST /api/entries returns (RegisteredEntryResponse): the plate as stored, uppercase without spaces. */
export interface RegisteredEntry {
  id: string;
  licensePlate: string;
  vehicleTypeId: string;
}
