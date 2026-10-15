# Minecraft 1.21.x compatibility matrix

Each row is an independently compiled artifact. The adapter column identifies the shared/version-specific integration band; it is not a copy of the gameplay design.

| Minecraft | Mod | Adapter | Build/automated invariants | Dedicated server smoke | Artifact |
|---|---:|---|---|---|---|
| 1.21.0 | 1.2.2 | modern | Pass | Pass | `anglers-odyssey-1.2.2-mc-1.21.jar` |
| 1.21.1 | 1.2.3 | modern | Pass | Pass | `anglers-odyssey-1.2.3-mc-1.21.1.jar` |
| 1.21.2 | 1.2.2.1 | modern2 | Pass | Pass | `anglers-odyssey-1.2.2.1-mc-1.21.2.jar` |
| 1.21.3 | 1.2.3.1 | modern2 | Pass | Pass | `anglers-odyssey-1.2.3.1-mc-1.21.3.jar` |
| 1.21.4 | 1.2.4.1 | modern3 | Pass | Pass | `anglers-odyssey-1.2.4.1-mc-1.21.4.jar` |
| 1.21.5 | 1.2.5.1 | modern4 | Pass | Pass | `anglers-odyssey-1.2.5.1-mc-1.21.5.jar` |
| 1.21.6 | 1.2.6 | modern5 | Pass | Pass | `anglers-odyssey-1.2.6-mc-1.21.6.jar` |
| 1.21.7 | 1.2.7 | modern5 | Pass | Pass | `anglers-odyssey-1.2.7-mc-1.21.7.jar` |
| 1.21.8 | 1.2.8 | modern5 | Pass | Pass | `anglers-odyssey-1.2.8-mc-1.21.8.jar` |
| 1.21.9 | 1.2.9 | modern6 | Pass | Pass | `anglers-odyssey-1.2.9-mc-1.21.9.jar` |
| 1.21.10 | 1.2.10 | modern6 | Pass | Pass | `anglers-odyssey-1.2.10-mc-1.21.10.jar` |
| 1.21.11 | 1.2.11 | modern6 | Pass | Pass | `anglers-odyssey-1.2.11-mc-1.21.11.jar` |

Automated invariants cover 151 fish definitions, 69 Angler's Odyssey advancements, 16 biome-region tags, five fishing loot tables, and JSON parsing. Runtime smoke tests launched an isolated dedicated server for every row, confirmed mod registration and recipe/advancement loading, reached `Done`, then shut down cleanly. The client bootstrap harness also launched every row, confirmed mod registration and resource reload, and found no Angler's Odyssey mixin failures. Player-facing fishing and client UI regression testing remains a manual release-owner check.
