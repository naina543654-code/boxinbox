#!/bin/bash
# Prints version info for the boxinbox repo, for build stamping.
# Usage: eval "$(./version-stamp.sh)"   -> sets SHA and DATE
# Falls back to "unknown" when git metadata is unavailable.
set -uo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
# FORCE_SHA / FORCE_DATE let the release flow stamp APKs with the commit
# they are about to be committed in (commit sources first, then build).
SHA="${FORCE_SHA:-$(git -C "$ROOT" rev-parse --short HEAD 2>/dev/null || echo unknown)}"
DATE="${FORCE_DATE:-$(git -C "$ROOT" show -s --format=%cs HEAD 2>/dev/null || echo unknown)}"
echo "SHA=$SHA"
echo "DATE=$DATE"
