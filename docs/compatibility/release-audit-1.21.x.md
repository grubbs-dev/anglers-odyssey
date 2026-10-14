# Minecraft 1.21.x release and biome audit

The 1.21.x compatibility sweep was checked against the official Minecraft release notes, Fabric's version/mapping metadata, and the mapped vanilla `BiomeKeys` registry for each release. The committed row-level evidence is in [`biome-coverage-1.21.x.csv`](biome-coverage-1.21.x.csv).

## Registry changes observed

| Releases | Vanilla biome registry | Change relevant to Angler's Odyssey |
|---|---:|---|
| 1.21–1.21.3 | 64 entries | No new vanilla biome affecting the existing habitat model. |
| 1.21.4–1.21.9 | 65 entries | `minecraft:pale_garden` added; intentionally excluded because it has no Angler's Odyssey habitat assignment. |
| 1.21.10–1.21.11 | 65 entries | No additional vanilla biome registry change affecting the habitat model. |

Every registry entry is classified in the CSV as `fishable`, `intentionally_excluded`, or `otherwise_handled`. Fishable entries are covered either by an exact habitat name or by an Angler's Odyssey region tag. The 16 effective habitat regions and all 151 fish definitions remain shared across the adapters.

## Gameplay audit

- Every fish definition has a non-empty habitat, rarity, and corresponding fishing eligibility path.
- Every effective habitat has at least one fish definition and one region/exact-biome resolution path.
- Vanilla fishing loot is intercepted only for fish entries; non-fish vanilla loot remains unchanged.
- If a rarity has no valid regional fish at a location, the original vanilla catch is preserved as cod rather than producing an empty result.
- Time, weather, rarity, shiny, trophy, journal, and rod-progression behavior remains in shared gameplay code; only Minecraft API/resource integration is version-adapted.
