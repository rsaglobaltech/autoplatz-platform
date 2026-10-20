import "server-only";
import type { Task, VehicleType } from "@autoplatz-platform/shared";

/**
 * The API's base URL. In development it defaults to the local API; in
 * production it must be set — an unset or empty one would send every request
 * to the web app itself, or nowhere. Read per request, so `next build` (which
 * runs as production) needs no API_URL.
 */
function apiUrl(): string {
  const url = (process.env.API_URL ?? "").trim();
  if (url) return url.replace(/\/$/, "");
  if (process.env.NODE_ENV === "production") {
    throw new Error(
      "API_URL is not set: point it at the API, e.g. https://api.example.com",
    );
  }
  return "http://127.0.0.1:8080";
}

/** An API error (RFC 9457): the web app decides on the `code`, not on the text. */
export class ApiError extends Error {
  constructor(
    readonly status: number,
    readonly code: string,
    message: string,
  ) {
    super(message);
  }
}

/** Server-only API client. */
async function call<T>(path: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers);
  headers.set("Accept", "application/json, application/problem+json");
  if (init.body) headers.set("Content-Type", "application/json");
  const res = await fetch(`${apiUrl()}${path}`, {
    ...init,
    headers,
    cache: "no-store",
  });
  if (!res.ok) {
    const problem = await res.json().catch(() => ({}));
    throw new ApiError(
      res.status,
      problem.code ?? "unexpected",
      problem.detail ?? res.statusText,
    );
  }
  return res.json() as Promise<T>;
}

export const api = {
  myTasks: () => call<Task[]>("/api/tasks"),
  createTask: (title: string) =>
    call<Task>("/api/tasks", {
      method: "POST",
      body: JSON.stringify({ title }),
    }),
  completeTask: (id: string) =>
    call<Task>(`/api/tasks/${encodeURIComponent(id)}/completion`, {
      method: "POST",
    }),
  vehicleTypes: () => call<VehicleType[]>("/api/vehicle-types"),
  availableVehicleTypes: () =>
    call<VehicleType[]>("/api/vehicle-types/available"),
  addVehicleType: (name: string) =>
    call<VehicleType>("/api/vehicle-types", {
      method: "POST",
      body: JSON.stringify({ name }),
    }),
};
