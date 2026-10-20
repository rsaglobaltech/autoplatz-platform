import { api } from "@/lib/api";
import { deleteVehicleType } from "./actions";
import { NewVehicleTypeForm } from "./new-vehicle-type-form";

export const dynamic = "force-dynamic";

/**
 * The catalog screen (REQ-003): the vehicle types, and where an administrator adds one.
 * REQ-005: and deletes one; a type in use stays listed as inactive.
 */
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
              <span className="flex items-center gap-3">
                <span className="text-xs text-gray-500">
                  {type.active ? "Active" : "Inactive"}
                </span>
                {type.active ? (
                  <form action={deleteVehicleType}>
                    <input type="hidden" name="id" value={type.id} />
                    <button
                      type="submit"
                      aria-label={`Delete ${type.name}`}
                      className="text-xs text-red-600 hover:underline"
                    >
                      Delete
                    </button>
                  </form>
                ) : null}
              </span>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
