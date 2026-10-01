# Dropped / excluded devices — fingerprint research 2026-10-01

Devices that could not be verified to the no-invention bar, or were excluded as out of scope. One line per device with the reason.

## OnePlus/Nothing/Motorola/Asus researcher
- Nothing Phone (2a) A142 @14: no verified SYSTEM fingerprint; wild :13/ Pacman prints are ro.vendor.build.fingerprint (vendor partition stays A13 while system is A14/A15).
- Motorola lyriq @12: pre-release/engineering build (Edge 40 launched with A13).
- Motorola hiphi @12: source labeled "Version: 13" but fingerprint says :12/ — mismatch, rejected.
- OnePlus 11 CPH2449EEA @14 (TP1A.220905.001/T.R4T3.159da5c-493-491): device ran RisingOS custom ROM; identical incremental on CPH2447 — looks spoofed, rejected.
- Asus Zenfone 9/10, ROG Phone 6/7 @13/@14: no full verified fingerprint found in any source.
- OnePlus Nord CE 3 Lite CPH2465 @13: build exists but no real-device fingerprint dump.
- Motorola ThinkPhone (bronco) @13: no verified @13 print (only @15/@16, out of scope).
- Moto G84 (bangkk) @13, Moto G52 (rhode) @13: no verified fingerprints.
- OnePlus 12R CPH2585/CPH2609 @14 (global/US): no verified fingerprint.
- (Out of scope @15/@16, excluded from deliverable: ThinkPhone bronco @15/@16, OnePlus Open CPH2551 @15.)
## Xiaomi/Oppo/Vivo/Realme flagships researcher
- Xiaomi 13 (fuxi) @13: no verified A13 stable fingerprint in real-device sources (damru DB only @14).
- Xiaomi 13 Pro (nuwa) @13: no verified fingerprint (only DEV/A16 builds surfaced).
- Xiaomi 13T (aristotle) @13: only A14 and A12 fingerprints found, no A13 stable.
- Xiaomi 12 (cupid) @13, 12 Pro (zeus) @13: no verified fingerprints (damru DB only @12 for zeus).
- Oppo Reno 10 Pro (CPH2521) @13, Reno 11 (CPH2599) @14, Reno 8 (CPH2451), Find X5 (CPH2307): no verified fingerprints.
- Vivo X90 Pro (V2213) @13, V29 (V2250) @13, X100 (V2308) @14, V30 (V2312) @14: no verified fingerprints.
- Realme 12 Pro+ (RMX3840) @14, 11 Pro+ (RMX3741) @13, GT 3 (RMX3709) @13: no verified fingerprints.
- (Kept with "unknown" patch rather than dropped: Oppo Find X6 Pro @13 and Realme GT 6 @14 — fingerprints verified verbatim from real-device reports, only SPL unverifiable.)
## Samsung flagships researcher
- SM-S911B @14, SM-S921B @14: skip list (already in table).
- No in-scope combos dropped for lack of data; 8 foldable rows + S906B/S911B/S916B product names are "assembled-inferred" (flagged in verification field) — parent can filter to device-log-only (17 rows) if desired.
## Xiaomi/Redmi/POCO + BBK mid-range researcher
- Redmi Note 12S (sea) @13: fingerprint segment is :12/ (Redmi/sea_global/sea:12/...) — version-segment mismatch with A13 profile, likely stale vendor print; excluded.
- Redmi 12C (earth) @13: :12/ segment (Redmi/vnd_earth/earth:12/...) — same reason; excluded.
- Redmi Note 13 5G (gold) @14: :12/ segment (Redmi/vnd_gold/gold:12/...) — excluded.
- Redmi 12 5G (sky) @14: :12/ segment + A12-era build ID on A14 build — excluded.
- POCO M5s (rosemary): dump was actually Redmi Note 10S (wrong device).
- POCO M5 (rock): no dump repo found.
- Redmi 13C (gale), Redmi 14C (tanzanite): only A15/HyperOS2 dumps (out of scope).
- Oppo A78 (CPH2565), A58 (CPH2529): no dump repo or channel post found.
- vivo Y36/V2271/V2246/V2205/V2207: nothing found anywhere.
- realme 10 Pro 5G (RMX3661), narzo 60 5G (RMX3750), 9 Pro 5G (RMX3471): no dump repo or channel post found.
- Pixel Tablet (tangorpro) @13: no verified incremental found (Google researcher).
