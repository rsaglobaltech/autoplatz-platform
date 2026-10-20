"use server";

import { ApiError, api } from "@/lib/api";

export interface EntryFormState {
  /** The form fields the API reported missing (REQ-015): each one is highlighted. */
  missing: string[];
  error?: string;
  saved?: boolean;
  /** What was typed, so a refused save does not wipe it. */
  values: Record<string, string>;
}

/** REQ-015: the API decides which required fields are missing; the form only shows it. */
export async function registerEntry(
  _: EntryFormState,
  data: FormData,
): Promise<EntryFormState> {
  const values = {
    licensePlate: String(data.get("licensePlate") ?? ""),
    vehicleType: String(data.get("vehicleType") ?? ""),
    ownerName: String(data.get("ownerName") ?? ""),
    enteredAt: String(data.get("enteredAt") ?? ""),
  };
  try {
    await api.registerEntry({
      licensePlate: values.licensePlate,
      vehicleTypeId: values.vehicleType,
    });
  } catch (e) {
    if (e instanceof ApiError && e.code === "missing_required_fields")
      return { missing: e.fields, values };
    if (e instanceof ApiError && e.status < 500)
      return { missing: [], error: e.message, values };
    throw e;
  }
  return { missing: [], saved: true, values: {} };
}
