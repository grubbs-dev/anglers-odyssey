# Angler's Odyssey

Angler's Odyssey is a cozy, collection-focused fishing adventure for Fabric. Cast into rivers, lakes, oceans, and other habitats to discover 151 fish, chase shiny variants, land trophy catches, and work your way through four custom rod tiers.

[![Minecraft 1.20.1](https://img.shields.io/badge/Minecraft-1.20.1-5b8c3a?style=flat-square)](https://fabricmc.net/)
[![Minecraft 1.21.1](https://img.shields.io/badge/Minecraft-1.21.1-3f7f5f?style=flat-square)](https://fabricmc.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-dbd0b4?style=flat-square)](https://fabricmc.net/)
[![Release](https://img.shields.io/github/v/release/grubbs-dev/anglers-odyssey?style=flat-square&color=cf8f35)](https://github.com/grubbs-dev/anglers-odyssey/releases)

## Screenshots

<p align="center">
  <img src="docs/screenshots/rod-collection-in-game.png" alt="Angler's Odyssey rods in-game" width="49%" />
  <img src="docs/screenshots/trophy-catch.png" alt="A trophy catch in Angler's Odyssey" width="49%" />
</p>

<p align="center">
  <img src="docs/screenshots/fish-discovery.png" alt="Exploring a fish habitat in Angler's Odyssey" width="80%" />
</p>

## What makes it fun

- Explore a large fish catalogue with rarity tiers, habitat restrictions, shiny variants, weights, lengths, and personal records.
- Build a collection in the Angler's Journal and follow the Angler's Odyssey advancement path.
- Unlock stronger rods as you discover species, each with a custom fishing bonus alongside vanilla rod enchantments.
- Find trophy catches and celebrate new species with in-game announcements.

## Supported versions

- Minecraft 1.20.1 — released baseline (`1.0.0`)
- Minecraft 1.21.1 — current release (`1.1.0`)

Each Minecraft version has its own JAR. Install only the file matching your Minecraft version.

See the [compatibility audit](docs/compatibility/release-matrix.md) for supported versions and port status.

## Installation

1. Install a supported Minecraft version with Fabric Loader.
2. Install the matching Fabric API version.
3. Place the matching `anglers-odyssey-<version>-mc-<minecraft-version>.jar` in the instance's `mods` folder.
4. Launch the Fabric profile and fish with a vanilla or Angler's Odyssey rod.

The mod is designed for both client and dedicated-server play. Install the same mod and Fabric API versions on the server and every client.

## Controls

- Right-click with a fishing rod to cast.
- Right-click again to reel in.
- Hold the Angler's Journal and right-click to open it.
- Use the journal's Previous and Next buttons to browse fish records, statistics, and rod progression.

## Rod progression

Discover new fish species to earn stronger rods. Rod abilities are custom to Angler's Odyssey and do not replace normal vanilla enchantments; all four rods remain compatible with vanilla fishing-rod enchantments.

| Rod | Unlock | Durability | Custom ability |
| --- | ---: | ---: | --- |
| Reed Rod | 20 species | 64 | Steady Grip |
| Oak Rod | 50 species | 128 | Quick Set — faster bites |
| Ironwood Rod | 100 species | 192 | Treasure Sense — better treasure odds |
| Angler's Rod | 150 species | 256 | Master Angler — faster bites and better treasure |

The Angler's Journal is crafted shapelessly from one vanilla book and one fish from the mod's fish tag.

## Development setup

The project targets Java 17 bytecode. The current Fabric Loom toolchain requires Java 21 or newer to run; Java 25 is also supported. A local development runtime is stored under `.tools/` and is ignored by Git.

From PowerShell:

```powershell
.\dev.ps1 build
.\dev.ps1 runClient
```

The first build downloads the Gradle and Fabric dependencies. The dev client uses the `run/` directory for its local Minecraft instance.

## Release build

Run `./gradlew.bat clean build` from PowerShell. The 1.21.1 distributable JAR is written to `build/libs/anglers-odyssey-1.1.0-mc-1.21.1.jar`; the sources JAR is not required for player installation.

## Project structure

- `src/main/java` — common mod logic, fish data, persistence, loot replacement, and mixins
- `src/client/java` — journal UI and client initialization
- `src/main/resources/data` — loot tables, advancements, recipes, and biome region tags
- `docs/fish_tables` — fish reference tables by rarity

## Asset note

The rod art follows the vanilla Minecraft fishing-rod pixel silhouette and uses Angler's Odyssey recolors, including separate held and cast-state textures. Review Mojang/Microsoft's current asset and distribution terms before publishing a public release.

## License

This project is licensed under the terms in [LICENSE](LICENSE).
