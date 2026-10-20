import type { NextConfig } from "next";

const config: NextConfig = {
  // Minimal Docker image (apps/web/Dockerfile).
  output: "standalone",
  transpilePackages: ["@autoplatz-platform/shared"],
};

export default config;
