# Signing key for the Sandbox PoC host app

The shared PoC keystore (`poc/poc-debug.keystore`, password in
`poc/keystore-info.txt`) did not exist when this app was built, so a
dedicated debug keystore was generated instead:

- File: `poc/host/poc-host-debug.keystore`
- Alias: `pocdebug`
- Key: RSA 2048, self-signed, 30-year validity
- Store password: `android`
- Key password: `android`

Debug key only — never use for anything release-facing. If the shared
`poc/poc-debug.keystore` appears later, re-signing with it is a one-command
`apksigner sign` swap (see `build-manual.sh`).
