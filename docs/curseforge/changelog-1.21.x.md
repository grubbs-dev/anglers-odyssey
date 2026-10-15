# Angler's Odyssey 1.21.x release notes

The 1.21.9–1.21.11 adapter removes an obsolete renderer redirect that no longer matches the vanilla client call site. Custom rods inherit `FishingRodItem`, so vanilla's late-1.21 renderer recognizes them without a redirect while preserving cast item-model behavior.

Use the entry matching the Minecraft version of the uploaded file.

## `anglers-odyssey-1.2.2.1-mc-1.21.2.jar`

- Restored vanilla fishing-rod enchantment parity for all four Angler's Odyssey rods: Lure, Luck of the Sea, Unbreaking, Mending, and Curse of Vanishing.
- Preserved the 1.21.2 fishing, loot, journal, advancement, and progression behavior.

## `anglers-odyssey-1.2.3.1-mc-1.21.3.jar`

- Restored vanilla fishing-rod enchantment parity for all four Angler's Odyssey rods: Lure, Luck of the Sea, Unbreaking, Mending, and Curse of Vanishing.
- Preserved the shared 1.21.2 adapter and all 151 fish and habitat rules.

## `anglers-odyssey-1.2.4.1-mc-1.21.4.jar`

- Restored vanilla fishing-rod enchantment parity for all four Angler's Odyssey rods.
- Removed the obsolete renderer redirect that prevented the client from completing resource reload and could produce a black screen.
- Preserved component-based cast-model behavior and all gameplay data.

## `anglers-odyssey-1.2.5.1-mc-1.21.5.jar`

- Restored vanilla fishing-rod enchantment parity for all four Angler's Odyssey rods.
- Removed the obsolete renderer redirect that could fail during client startup/resource reload.
- Preserved component-based cast-model behavior, persistence, fishing loot, and progression.

## `anglers-odyssey-1.2.0-mc-1.21.jar`

- Added the Minecraft 1.21 Fabric build of Angler's Odyssey.
- Preserved the complete 151-fish collection, habitat rules, journal, rods, advancements, and fishing progression.
- Validated item registration, recipe loading, advancement loading, and dedicated-server startup.

## `anglers-odyssey-1.2.1-mc-1.21.1.jar`

- Added the Minecraft 1.21.1 Fabric build.
- Preserved all fish, journal records, biome coverage, loot pools, and rod progression.
- Validated dedicated-server startup and mod registration.

## `anglers-odyssey-1.2.2-mc-1.21.2.jar`

- Added the 1.21.2 fishing-bobber, loot-context, registry, and recipe compatibility adapter.
- Updated item registration for the new registry-key requirement.
- Preserved all 151 fish and validated server startup.

## `anglers-odyssey-1.2.3-mc-1.21.3.jar`

- Added the 1.21.3-compatible build using the shared 1.21.2 adapter.
- Preserved fishing behavior, journal data, recipes, advancements, and rod progression.
- Validated server startup and resource loading.

## `anglers-odyssey-1.2.4-mc-1.21.4.jar`

- Updated the rod cast model integration for Minecraft's component-based item model system.
- Preserved the fishing and journal experience while adapting the 1.21.4 client API.
- Validated server startup and resource loading.

## `anglers-odyssey-1.2.5-mc-1.21.5.jar`

- Updated persistent journal/records state to the 1.21.5 persistent-state codec API.
- Updated tooltip and inventory tick integrations and recipe ingredients.
- Preserved all fish, progression, and fishing loot behavior.

## `anglers-odyssey-1.2.6-mc-1.21.6.jar`

- Updated item registry keys and 1.21.6 recipe ingredient encoding.
- Preserved the full collection and fishing progression.
- Dedicated-server startup validated with 151 fish, four rods, the journal, 1,861 loaded recipes, and no Angler's Odyssey data errors.

## `anglers-odyssey-1.2.7-mc-1.21.7.jar`

- Added the 1.21.7-compatible build on the shared late-1.21 adapter.
- Preserved all fish, habitats, loot tables, journal records, recipes, and advancements.
- Dedicated-server startup validated.

## `anglers-odyssey-1.2.8-mc-1.21.8.jar`

- Added the 1.21.8-compatible build.
- Preserved the complete Angler's Odyssey gameplay loop and resource data.
- Dedicated-server startup validated.

## `anglers-odyssey-1.2.9-mc-1.21.9.jar`

- Added the 1.21.9-compatible client/server adapter.
- Updated the late-1.21 screen input and drawing APIs while preserving the journal UI behavior.
- Preserved all fish, habitats, loot tables, recipes, advancements, and progression.

## `anglers-odyssey-1.2.10-mc-1.21.10.jar`

- Added the 1.21.10-compatible build using the 1.21.9+ adapter.
- Preserved the complete collection, fishing behavior, journal, rods, and advancements.
- Dedicated-server startup validated.

## `anglers-odyssey-1.2.11-mc-1.21.11.jar`

- Added the 1.21.11-compatible build.
- Preserved all 151 fish, biome rules, loot pools, records, progression, and client integrations.
- Dedicated-server startup validated.
