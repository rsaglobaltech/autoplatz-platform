import Link from "next/link";

export default async function HomePage() {
  return (
    <section className="space-y-4">
      <h1 className="text-2xl font-semibold">autoplatz-platform</h1>
      <p className="text-sm text-gray-600">
        A Next.js web app on top of a Spring Boot API with hexagonal
        architecture. Generated with forja.
      </p>
      <Link
        href="/tasks"
        className="inline-block rounded-md bg-brand-600 px-4 py-2 text-sm text-white"
      >
        Go to my tasks
      </Link>
    </section>
  );
}
