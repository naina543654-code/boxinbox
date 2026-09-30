#!/usr/bin/env python3
"""Merge the BlackBox engine manifest into the host-runtime manifest.

The engine (Bcore) ships 307 application components (proxy activities/
services/receivers/providers P0..P49, daemon, VPN service, content
providers) plus ~184 uses-permissions. This script:

1. Takes the host manifest as the base (package com.sandboxpoc.hostruntime).
2. Copies every <uses-permission> from the engine manifest that the host
   does not already declare.
3. Copies every child of the engine <application> into the host
   <application>, rewriting relative android:name values
   (".proxy.X" -> "top.niunaijun.blackbox.proxy.X"). Names that are
   already fully qualified are left alone.

This mirrors what AGP manifest-merging does when consuming the AAR, so
the manual APK matches what Jason's Gradle build would produce.
Upstream sources are not modified.
"""
import sys
import xml.etree.ElementTree as ET

ANDROID = "http://schemas.android.com/apk/res/android"
ENGINE_PKG = "top.niunaijun.blackbox"

ET.register_namespace("android", ANDROID)
ET.register_namespace("tools", "http://schemas.android.com/tools")


def a(name):
    return f"{{{ANDROID}}}{name}"


def fix_name(el):
    name = el.get(a("name"))
    if name and name.startswith("."):
        el.set(a("name"), ENGINE_PKG + name)


def fix_application_id(el, app_id):
    """Replace ${applicationId} placeholders (AGP does this at merge time)."""
    for k, v in el.attrib.items():
        if "${applicationId}" in v:
            el.set(k, v.replace("${applicationId}", app_id))
    for child in el:
        fix_application_id(child, app_id)


def main(host_path, engine_path, out_path, app_id):
    host = ET.parse(host_path)
    engine = ET.parse(engine_path)
    hroot, eroot = host.getroot(), engine.getroot()

    # 1. permissions
    existing = {p.get(a("name")) for p in hroot.findall("uses-permission")}
    added_perm = 0
    for p in eroot.findall("uses-permission"):
        if p.get(a("name")) not in existing:
            hroot.append(p)
            existing.add(p.get(a("name")))
            added_perm += 1

    # 2. application components
    happ = hroot.find("application")
    eapp = eroot.find("application")
    added_comp = 0
    for child in eapp:
        fix_name(child)
        fix_application_id(child, app_id)
        happ.append(child)
        added_comp += 1

    host.write(out_path, encoding="utf-8", xml_declaration=True)
    print(f"MERGED: +{added_perm} permissions, +{added_comp} components -> {out_path}")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
