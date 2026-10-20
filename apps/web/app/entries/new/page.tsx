import type { EntryFormField } from "@autoplatz-platform/shared";
import { api } from "@/lib/api";

export const dynamic = "force-dynamic";

/** How each field the API returns is shown; which fields exist and which are required is the API's business. */
const PRESENTATION: Record<string, { label: string; type: string }> = {
  licensePlate: { label: "License plate", type: "text" },
  vehicleType: { label: "Vehicle type", type: "select" },
  ownerName: { label: "Owner name", type: "text" },
  enteredAt: { label: "Entry date/time", type: "datetime-local" },
};

const INPUT =
  "w-full rounded-md border border-black/10 bg-surface px-3 py-2 text-sm";

function Field({ field }: { field: EntryFormField }) {
  const { label, type } = PRESENTATION[field.name] ?? {
    label: field.name,
    type: "text",
  };
  const id = `entry-${field.name}`;
  return (
    <div className="space-y-1">
      <label htmlFor={id} className="block text-sm font-medium">
        {label}{" "}
        <span className="text-xs font-normal text-gray-500">
          {field.required ? "(required)" : "(optional)"}
        </span>
      </label>
      {type === "select" ? (
        <select
          id={id}
          name={field.name}
          required={field.required}
          defaultValue=""
          className={INPUT}
        >
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
          id={id}
          name={field.name}
          type={type}
          required={field.required}
          className={INPUT}
        />
      )}
    </div>
  );
}

/** The entry form (REQ-012): the fields an administrator fills in to register a vehicle's entry. */
export default async function NewEntryPage() {
  const form = await api.entryForm();
  return (
    <section className="space-y-6">
      <h1 className="text-xl font-semibold">Register a vehicle entry</h1>
      <form className="space-y-4">
        {form.fields.map((field) => (
          <Field key={field.name} field={field} />
        ))}
      </form>
    </section>
  );
}
