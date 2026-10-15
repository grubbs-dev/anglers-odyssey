# Changelog

## Unreleased — 26.x compatibility sweep

- Audited the stable 26.1, 26.1.1, 26.1.2, 26.2, and 26.3 release scope.
- Added an isolated official-Mojang-mappings adapter using Java 25 and the no-remap Fabric Loom workflow.
- Migrated custom networking, saved data, advancement lookup, item components, creative tabs, fishing behavior, and the journal UI to the 26.x APIs.
- Added 26.x biome registry coverage for every stable target, including Dappled Forest in 26.3.
- Build, registry, dedicated-server, and client-bootstrap gates pass for all five targets; manual fishing/journal validation remains required before publication.
- Added a runtime validation report documenting dedicated-server and client bootstrap evidence for every 26.x target.

## 1.2.13 through 1.2.13.4 — Minecraft 26.x resource and journal fixes

- Added the required 26.x `assets/anglers_odyssey/items` definitions for every custom fish, journal, cooked fish, and rod item.
- Added 26.x fishing-rod cast conditions so custom rods select their cast-state models correctly.
- Fixed the extraction render layering used by the Angler's Journal so its panel, headings, statistics, and fish pages render above the inherited screen layer.
- Preserved the official-Mojang-mappings adapter, 151-fish catalog, loot tables, persistence, advancements, and progression.
- Automated build and resource validation pass; manual item-texture and journal interaction testing remains required.

## 1.0.2 — Minecraft 1.20.1 maintenance build

- Restored a dedicated 1.20.1 adapter for the pre-`AdvancementEntry` and pre-`PersistentState.Type` APIs without changing the shared 1.20.2–1.20.4 adapter.
- Fixed custom rod cast-state predicates so first-person and hotbar models can switch to their cast textures while a line is out.
- Restored mouse-wheel scrolling in the 1.20.1 advancement screen.
- Rebuilt and client-bootstrapped the 1.20.1 artifact with Fabric Loader 0.16.10 and Fabric API 0.92.9+1.20.1; manual gameplay verification remains required before release.

## 1.2.x — Minecraft 1.21.0 through 1.21.11

- Added separately named Fabric JARs for every stable 1.21.x release currently available in the project matrix.
- Preserved all 151 fish, five rarity loot pools, biome restrictions, journal records, four-rod progression, advancements, and existing 1.20.1–1.20.6 support.
- Added release-specific adapters for item registry keys, component-era persistence, recipe ingredient formats, fishing-bobber APIs, rod cast models, and late-1.21 client input/render APIs.
- Added explicit dedicated-server smoke validation for each 1.21.x release: 151 fish, four rods, journal registration, recipe loading, advancement loading, and clean world startup.
- Updated CI to build each 1.21.x artifact independently so a failure cannot silently produce a misleading release asset.

## 1.2.6.1 through 1.2.8.1 — Minecraft 1.21.6 through 1.21.8 startup fixes

- Removed the obsolete fishing-bobber renderer redirect from the shared 1.21.6–1.21.8 client adapter.
- Fixed the resource-reload mixin failure that left the client on a black screen while menu audio continued in the background.
- Preserved the shared late-1.21 fishing, cast-model, journal, advancement, and progression behavior.

## 1.2.2.1 through 1.2.5.1 — Minecraft 1.21.2 through 1.21.5 maintenance fixes

- Added custom rods to the 1.21.2–1.21.5 fishing and durability enchantment tags, restoring vanilla Lure, Luck of the Sea, Unbreaking, Mending, and Curse of Vanishing support.
- Removed the obsolete 1.21.4/1.21.5 fishing-bobber renderer redirect that caused a critical mixin failure during client resource reload.
- Preserved the component-based cast item-model behavior and all existing fish, loot, journal, advancement, and progression data.
- Client bootstrap validation now reaches resource reload completion for 1.21.4 and 1.21.5; manual in-game enchantment and fishing checks remain required.

## 1.0.9 / 1.0.10 — Minecraft 1.20.5 and 1.20.6 rod enchantment parity

- Prevented the Angler's Odyssey root and first-catch advancement from unlocking from a tick or inventory-change event; they are now granted only after a regional Angler's Odyssey fish is actually selected by the fishing result.
- Added all four Angler's Odyssey rods to Minecraft's fishing and durability enchantment tags so vanilla Lure, Luck of the Sea, Unbreaking, Mending, and Curse of Vanishing can be applied.
- Applied vanilla Lure and Luck of the Sea levels to custom rods in addition to each rod's built-in progression bonuses.
- Preserved the component-era item data, 151-fish catalog, biome restrictions, journal records, and rod progression.

## 1.2.2 / 1.2.3 — Minecraft 1.21 and 1.21.1 rod enchantment parity

- Added all four Angler's Odyssey rods to Minecraft's 1.21 fishing and durability enchantment tags, matching the vanilla rod's supported Lure, Luck of the Sea, Unbreaking, Mending, and Curse of Vanishing behavior.
- Applied vanilla Lure and Luck of the Sea levels to custom rods in addition to their built-in progression bonuses.
- Fixed 1.21.1 cast-model state so first-person and hotbar rod textures switch immediately when a custom rod is cast.
- Preserved the modern adapter's 151-fish catalog, biome restrictions, journal data, and progression.

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
