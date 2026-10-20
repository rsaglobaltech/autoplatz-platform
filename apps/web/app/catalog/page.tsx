import { api } from "@/lib/api";
import { NewVehicleTypeForm } from "./new-vehicle-type-form";

export const dynamic = "force-dynamic";

/** The catalog screen (REQ-003): the vehicle types, and where an administrator adds one. */
export default async function CatalogPage() {
  const types = await api.vehicleTypes();
  return (
    <section className="space-y-6">
      <h1 className="text-xl font-semibold">Vehicle types</h1>
      <NewVehicleTypeForm />
      {types.length === 0 ? (
        <p className="text-sm text-gray-600">No vehicle types yet.</p>
      ) : (
        <ul className="divide-y divide-black/5 rounded-md border border-black/5 bg-surface">
          {types.map((type) => (
            <li
              key={type.id}
              className="flex items-center justify-between gap-4 px-4 py-3"
            >
              <span className={type.active ? "" : "text-gray-400"}>
                {type.name}
              </span>
              <span className="text-xs text-gray-500">
                {type.active ? "Active" : "Inactive"}
              </span>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
