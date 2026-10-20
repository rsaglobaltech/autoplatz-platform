import { api } from "@/lib/api";
import { EntryForm } from "./entry-form";

export const dynamic = "force-dynamic";

/** The entry form (REQ-012): the fields an administrator fills in to register a vehicle's entry. */
export default async function NewEntryPage() {
  const form = await api.entryForm();
  return (
    <section className="space-y-6">
      <h1 className="text-xl font-semibold">Register a vehicle entry</h1>
      <EntryForm fields={form.fields} />
    </section>
  );
}
