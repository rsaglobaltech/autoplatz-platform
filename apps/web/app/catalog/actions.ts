"use server";

import { revalidatePath } from "next/cache";
import { ApiError, api } from "@/lib/api";

export interface FormState {
  error?: string;
}

const MESSAGES: Record<string, string> = {
  invalid_vehicle_type_name:
    "The name is required and must be at most 60 characters.",
  duplicate_vehicle_type_name: "A vehicle type with that name already exists.",
};

export async function addVehicleType(
  _: FormState,
  data: FormData,
): Promise<FormState> {
  try {
    await api.addVehicleType(String(data.get("name") ?? ""));
  } catch (e) {
    if (e instanceof ApiError && e.status < 500)
      return { error: MESSAGES[e.code] ?? e.message };
    throw e;
  }
  revalidatePath("/catalog");
  return {};
}

/** REQ-005: the API decides whether the type is removed or, being in use, deactivated. */
export async function deleteVehicleType(data: FormData): Promise<void> {
  await api.deleteVehicleType(String(data.get("id") ?? ""));
  revalidatePath("/catalog");
}
