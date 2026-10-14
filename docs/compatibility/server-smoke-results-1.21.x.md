# Dedicated-server smoke results: Minecraft 1.21.x

All rows were launched from isolated `run-server-1.21.x` directories with Java 21, Fabric Loader 0.19.5, the matching Fabric API, and an accepted EULA. Each server was stopped through the console after reaching `Done`.

| Version | Registration | Data loading | World startup | Shutdown |
|---|---|---|---|---|
| 1.21.0–1.21.1 | 151 fish, four rods, journal | recipes and advancements loaded without Angler's Odyssey errors | Pass | Pass |
| 1.21.2–1.21.4 | 151 fish, four rods, journal | recipes and advancements loaded without Angler's Odyssey errors | Pass | Pass |
| 1.21.5–1.21.8 | 151 fish, four rods, journal | recipes and advancements loaded without Angler's Odyssey errors | Pass | Pass |
| 1.21.9–1.21.11 | 151 fish, four rods, journal | recipes and advancements loaded without Angler's Odyssey errors | Pass | Pass |

The smoke test is a startup/registration gate, not a replacement for client fishing and journal interaction tests. Those remain part of the manual CurseForge release checklist.

## Client bootstrap follow-up

The committed `scripts/client-bootstrap-smoke.ps1` harness launched every 1.21.x client. All twelve versions registered 151 fish, four rods, and the journal, completed resource reload without Angler's Odyssey mixin errors, and reached the title screen. The unauthenticated development profile produced only expected Mojang/Realms authentication warnings. In-game fishing, journal interaction, and cast-rod visual confirmation remain manual checks.
