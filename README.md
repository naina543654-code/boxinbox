# boxinbox — Privacy Sandbox (Android, no-root)

No-root Android privacy sandbox, app-cloner style. PoC stage.

## Branches

- `main` — **PoC v2**: coherent device-profile spoofing (Build fields, Android ID,
  location + movement, sensors, network identity, telephony), probe with
  EXPECTED vs OBSERVED PASS/FAIL/UNKNOWN, per-feature capability matrix.
- `v1` — **PoC v1**: runtime-feasibility baseline (sandbox create / install /
  launch / internet / reset, no spoofing). Tagged `poc-v1`.

## Layout

Everything lives under `poc/`:

- `poc/README.md` — build documentation
- `poc/TEST-INSTRUCTIONS.md` — on-device test sheet (v2)
- `poc/SPOOFING_MATRIX.md` — per-feature capability states (v2)
- `poc/engine/` — manual AAR build pipeline for the engine
- `poc/engines/` — engine source checkouts (zitanioi/blackbox @ c994edf, alex5402/newblackbox @ 89b5983)
- `poc/host-runtime/` — host app source (SpoofProfile, ProfileStore, SandboxRuntime seam)
- `poc/probe/` — probe APK source
- `poc/apks/` — signed release APKs

## Build notes

- Gradle wrapper is included but needs one distribution download on first run.
- Manual build scripts (`build-manual.sh`) are included for offline builds.
- Debug keystores (`poc-debug.keystore` etc.) are throwaway keys for reproducible
  signed PoC builds. `keystore-info.txt` holds their passwords.
