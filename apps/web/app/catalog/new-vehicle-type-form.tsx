"use client";

import { useActionState } from "react";
import { addVehicleType, type FormState } from "./actions";

export function NewVehicleTypeForm() {
  const [state, action, pending] = useActionState<FormState, FormData>(
    addVehicleType,
    {},
  );
  return (
    <form action={action} className="space-y-2">
      <div className="flex gap-2">
        <input
          name="name"
          aria-label="New vehicle type"
          placeholder="New vehicle type"
          maxLength={60}
          className="flex-1 rounded-md border border-black/10 bg-surface px-3 py-2 text-sm"
        />
        <button
          type="submit"
          disabled={pending}
          className="rounded-md bg-brand-600 px-4 py-2 text-sm text-white disabled:opacity-50"
        >
          Add
        </button>
      </div>
      {state.error ? (
        <p role="alert" className="text-sm text-red-600">
          {state.error}
        </p>
      ) : null}
    </form>
  );
}
