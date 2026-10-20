"use client";

import Link from "next/link";
import { useActionState } from "react";
import type { EntryFormField } from "@autoplatz-platform/shared";
import { registerEntry, type EntryFormState } from "./actions";

/** How each field the API returns is shown; which fields exist and which are required is the API's business. */
const PRESENTATION: Record<string, { label: string; type: string }> = {
  licensePlate: { label: "License plate", type: "text" },
  vehicleType: { label: "Vehicle type", type: "select" },
  ownerName: { label: "Owner name", type: "text" },
  enteredAt: { label: "Entry date/time", type: "datetime-local" },
};

const INPUT = "w-full rounded-md border bg-surface px-3 py-2 text-sm";

function Field({
  field,
  missing,
  value,
}: {
  field: EntryFormField;
  missing: boolean;
  value: string | undefined;
}) {
  const { label, type } = PRESENTATION[field.name] ?? {
    label: field.name,
    type: "text",
  };
  const id = `entry-${field.name}`;
  const className = `${INPUT} ${missing ? "border-red-600 ring-1 ring-red-600" : "border-black/10"}`;
  const common = {
    id,
    name: field.name,
    required: field.required,
    "aria-invalid": missing || undefined,
    "aria-describedby": missing ? `${id}-missing` : undefined,
    className,
  };
  return (
    <div className="space-y-1">
      <label htmlFor={id} className="block text-sm font-medium">
        {label}{" "}
        <span className="text-xs font-normal text-gray-500">
          {field.required ? "(required)" : "(optional)"}
        </span>
      </label>
      {type === "select" ? (
        <select {...common} defaultValue={value ?? ""}>
          <option value="" disabled>
            Select a {label.toLowerCase()}
          </option>
          {field.options.map((option) => (
            <option key={option.id} value={option.id}>
              {option.name}
            </option>
          ))}
        </select>
      ) : (
        <input
          {...common}
          type={type}
          defaultValue={value ?? field.value ?? undefined}
        />
      )}
      {missing ? (
        <p id={`${id}-missing`} className="text-sm text-red-600">
          {label} is required.
        </p>
      ) : null}
    </div>
  );
}

/**
 * The entry form (REQ-012). It does not validate in the browser (`noValidate`): on save the API refuses an entry
 * missing a required field, and each field it names is highlighted (REQ-015).
 */
export function EntryForm({ fields }: { fields: EntryFormField[] }) {
  const [state, action, pending] = useActionState<EntryFormState, FormData>(
    registerEntry,
    { missing: [], values: {} },
  );
  return (
    <form action={action} noValidate className="space-y-4">
      {fields.map((field) => (
        <Field
          key={`${field.name}-${state.values[field.name] ?? ""}`}
          field={field}
          missing={state.missing.includes(field.name)}
          value={state.values[field.name]}
        />
      ))}
      <button
        type="submit"
        disabled={pending}
        className="rounded-md bg-brand-600 px-4 py-2 text-sm text-white disabled:opacity-50"
      >
        Save entry
      </button>
      {state.error ? (
        <p role="alert" className="text-sm text-red-600">
          {state.error}
        </p>
      ) : null}
      {state.saved ? (
        <p role="status" className="text-sm text-green-700">
          Entry saved.{" "}
          <Link href="/entries/inside" className="underline">
            See the vehicles in the garage
          </Link>
        </p>
      ) : null}
    </form>
  );
}
