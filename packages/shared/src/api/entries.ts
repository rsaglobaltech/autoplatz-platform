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

/** What POST /api/entries takes (RegisterEntryRequest). */
export interface RegisterEntryRequest {
  licensePlate: string;
  vehicleTypeId: string;
}

/** What POST /api/entries returns (RegisteredEntryResponse): the plate as stored, uppercase without spaces. */
export interface RegisteredEntry {
  id: string;
  licensePlate: string;
  vehicleTypeId: string;
}
