import Link from "next/link";
import { api } from "@/lib/api";

export const dynamic = "force-dynamic";

/** The vehicles currently in the garage (REQ-017): every entry still "inside". */
export default async function VehiclesInGaragePage() {
  const vehicles = await api.vehiclesInGarage();
  return (
    <section className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <h1 className="text-xl font-semibold">Vehicles in the garage</h1>
        <Link
          href="/entries/new"
          className="rounded-md bg-brand-600 px-4 py-2 text-sm text-white"
        >
          Register an entry
        </Link>
      </div>
      {vehicles.length === 0 ? (
        <p className="text-sm text-gray-600">No vehicles in the garage.</p>
      ) : (
        <ul className="divide-y divide-black/5 rounded-md border border-black/5 bg-surface">
          {vehicles.map((vehicle) => (
            <li
              key={vehicle.id}
              className="flex items-center justify-between gap-4 px-4 py-3"
            >
              <span className="font-mono">{vehicle.licensePlate ?? "—"}</span>
              <span className="flex items-center gap-3 text-xs text-gray-500">
                <span>{vehicle.vehicleType}</span>
                <span>Inside</span>
              </span>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
