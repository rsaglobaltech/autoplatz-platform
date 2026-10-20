"use server";

import { revalidatePath } from "next/cache";
import { ApiError, api } from "@/lib/api";

export interface FormState {
  error?: string;
}

const MESSAGES: Record<string, string> = {
  invalid_vehicle_type_name: "The name must be between 1 and 60 characters.",
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
