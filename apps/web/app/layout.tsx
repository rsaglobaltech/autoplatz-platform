import type { Metadata } from "next";
import Link from "next/link";
import { UserMenu } from "@/components/user-menu";
import "./globals.css";

export const metadata: Metadata = {
  title: "autoplatz-platform",
  description: "autoplatz-platform — generated with forja",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="min-h-screen bg-app text-text antialiased">
        <header className="border-b border-black/5 bg-surface">
          <nav className="mx-auto flex max-w-3xl items-center justify-between px-4 py-3">
            <Link href="/" className="font-semibold">
              autoplatz-platform
            </Link>
            <div className="flex items-center gap-4 text-sm">
              <Link href="/tasks" className="hover:underline">
                Tasks
              </Link>
              <Link href="/catalog" className="hover:underline">
                Catalog
              </Link>
              <UserMenu />
            </div>
          </nav>
        </header>
        <main className="mx-auto max-w-3xl px-4 py-8">{children}</main>
      </body>
    </html>
  );
}
