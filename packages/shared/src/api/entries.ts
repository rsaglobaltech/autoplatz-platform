/** What the Java API returns (EntryFormResponse). Keep in sync with apps/api, or generate it from the OpenAPI spec. */
export interface EntryForm {
  fields: EntryFormField[];
}

export interface EntryFormField {
  name: string;
  required: boolean;
  options: { id: string; name: string }[];
}
