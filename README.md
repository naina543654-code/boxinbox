# boxinbox — PoC v1 (runtime-feasibility baseline)

This branch is the v1 baseline: BlackBox engine integrated behind a
SandboxRuntime seam, host app proving sandbox create / guest install /
launch / internet / reset, plus a basic probe APK. No device-profile
spoofing (the source audit proved the engine had none).

See `main` (tagged `poc-v2`) for the spoofing implementation.
Build docs: `poc/README.md`. Test sheet: `poc/TEST-INSTRUCTIONS.md`.
