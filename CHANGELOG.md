# Changelog

## 1.0.1 — Minecraft 1.20.1

Maintenance release for the original 1.20.1 experience.

- Fixed custom rods switching to their cast-state texture while fishing.
- Tightened the Angler's Odyssey advancement tree layout.
- Added mouse-wheel panning for the advancement tree.
- Preserved existing fish, progression, journal records, worlds, and persistence behavior.

## 1.1.1 — Minecraft 1.21.1

Quality-of-life maintenance release following the 1.21.1 port.

The 1.21.1 release includes the same advancement-navigation and rod-rendering improvements, plus the version-specific data, component, networking, and rendering migrations described below.

## 1.1.0 — Minecraft 1.21.1

Angler's Odyssey now supports Minecraft 1.21.1 with a dedicated, version-qualified release JAR. The original 1.20.1 release remains unchanged.

- Added an explicit Minecraft release and migration matrix covering 1.20.2 through 1.21.1.
- Audited the 1.21.1 vanilla biome registry (64 biome JSONs) and recorded a classification for every biome.
- Ported item settings, food components, tooltips, custom item data, persistence, advancements, typed networking, rendering signatures, and rod durability to 1.21.1.
- Renamed 1.21.1 data-pack paths to the singular forms required by the release.
- Added Gradle verification gates for the 151-fish catalog and biome coverage.
- Added version-qualified JAR naming: `anglers-odyssey-<mod-version>-mc-<minecraft-version>.jar`.
- Restored a clean 1.20.1 release-tag baseline in a separate worktree for behavioral comparison.
- Evaluated three external porting tools. `reqsery/mc-mod-porter` was useful for mechanical migration discovery, but its generated 1.21.1 project selected incompatible loader/mapping metadata and still required manual gameplay/data migration. The Forge-focused and experimental IR tools were not applied to this Fabric project.
- Migrated all 151 1.21.1 shiny-fish loot functions from removed `minecraft:set_nbt` to `minecraft:set_custom_data`.
- Migrated 453 cooking recipe results and advancement icons to the 1.21.1 schemas.
- Added `validateVersionedData` to fail builds when component-era loot, recipe, or advancement formats regress.
- Dedicated-server validation now reaches a clean 1.21.1 `Done` state with zero Angler's Odyssey resource-loading errors in the validation checkout.
- Corrected the vanilla 1.21.1 fishing loot override from the obsolete plural `loot_tables` path to `loot_table`; added a build gate preventing this regression.
- Updated 1.21.1 nested fishing loot-table entries from `name` to `value`, and changed first-catch advancement awarding to occur from the fishing interception rather than inventory possession.
- Added explicit manual-grant criteria for first-catch and journal-completion advancements so code-triggered awards use the correct criterion.

- Fixed the journal recipe and vanilla fishing advancement icon schemas so 1.21.1 resource reloads are clean.
- Fixed journal rendering order so Minecraft's 1.21.1 background blur does not blur the journal itself.

Client and dedicated-server smoke tests passed in the development environment. The 1.21.1 file is approved for CurseForge upload; publication itself is managed from the existing Angler's Odyssey project page.
## 1.0.0 — Minecraft 1.20.1

Initial Angler's Odyssey release with 151 fish, the Angler's Journal, four unlockable rods, shiny variants, trophy catches, and biome-based fishing.
