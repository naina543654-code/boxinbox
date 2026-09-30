#!/usr/bin/env python3
"""Generate top.niunaijun.blackbox.R.java from a merged aapt2 R.txt.

The manual APK links ALL resources (host + engine + appcompat) in one
aapt2 invocation, so every resource gets a final ID in the app's package
namespace. The prebuilt engine classes reference
top.niunaijun.blackbox.R fields (non-final -> getstatic), so the dex must
contain that R class with the merged IDs.

This script joins the engine's own R.txt (the set of resources it uses)
against the merged R.txt (final IDs) and emits the engine R.java with
non-final fields — exactly what AGP generates for a library at app-build
time.
"""
import sys
from collections import defaultdict


def load_rtxt(path):
    table = {}
    with open(path) as f:
        for line in f:
            parts = line.split()
            if len(parts) == 4 and parts[0] == "int":
                _, typ, name, rid = parts
                table[(typ, name)] = rid
    return table


def main(merged_rtxt, engine_rtxt, out_java):
    merged = load_rtxt(merged_rtxt)
    engine = load_rtxt(engine_rtxt)
    by_type = defaultdict(list)
    missing = []
    for (typ, name) in engine:
        rid = merged.get((typ, name))
        if rid is None:
            missing.append(f"{typ}/{name}")
        else:
            by_type[typ].append((name, rid))
    if missing:
        print(f"WARNING: {len(missing)} engine resources absent from merged R.txt: {missing[:5]}")

    with open(out_java, "w") as f:
        f.write("package top.niunaijun.blackbox;\n\n")
        f.write("public final class R {\n")
        for typ in sorted(by_type):
            f.write(f"  public static final class {typ} {{\n")
            for name, rid in sorted(by_type[typ]):
                f.write(f"    public static int {name}={rid};\n")
            f.write("  }\n")
        f.write("}\n")
    total = sum(len(v) for v in by_type.values())
    print(f"R_JAVA: {total} fields, {len(by_type)} types -> {out_java}")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3])
