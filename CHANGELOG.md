# Changelog

## 1.2.x — Minecraft 1.21.0 through 1.21.11

- Added separately named Fabric JARs for every stable 1.21.x release currently available in the project matrix.
- Preserved all 151 fish, five rarity loot pools, biome restrictions, journal records, four-rod progression, advancements, and existing 1.20.1–1.20.6 support.
- Added release-specific adapters for item registry keys, component-era persistence, recipe ingredient formats, fishing-bobber APIs, rod cast models, and late-1.21 client input/render APIs.
- Added explicit dedicated-server smoke validation for each 1.21.x release: 151 fish, four rods, journal registration, recipe loading, advancement loading, and clean world startup.
- Updated CI to build each 1.21.x artifact independently so a failure cannot silently produce a misleading release asset.

## 1.0.6 — Minecraft 1.20.6

- Added the component-era 1.20.5+ item, tooltip, persistence, networking, rendering, and fishing-data adapter.
- Updated custom fish loot to use `minecraft:set_custom_data`, component-era recipe results, and component-era advancement icons.
- Corrected the component-era Angler's Journal recipe result schema and completed isolated dedicated-server smoke validation for 1.20.2 through 1.20.6.
- Updated Fabric metadata and Java requirements for Minecraft 1.20.6.

## 1.0.5 — Minecraft 1.20.5

- Added the component-era adapter introduced by the Armored Paws release.
- Preserved the 151-fish catalog, biome restrictions, journal records, rod progression, and advancement layout.

## 1.0.4 — Minecraft 1.20.4

- Added the 1.20.4-compatible build with version-qualified metadata.

## 1.0.3 — Minecraft 1.20.3

- Added the 1.20.3-compatible build with version-qualified metadata.

## 1.0.2 — Minecraft 1.20.2

- Added the 1.20.2 registry, advancement, persistence, and client-screen compatibility adapter.

## 1.0.1 — Minecraft 1.20.1

Maintenance release for the original 1.20.1 experience.

- Fixed custom rods switching to their cast-state texture while fishing.
- Tightened the Angler's Odyssey advancement tree layout.
- Added mouse-wheel panning for the advancement tree.
- Preserved existing fish, progression, journal records, worlds, and persistence behavior.

## 1.0.0 — Minecraft 1.20.1

Initial Angler's Odyssey release with 151 fish, the Angler's Journal, four unlockable rods, shiny variants, trophy catches, and biome-based fishing.
