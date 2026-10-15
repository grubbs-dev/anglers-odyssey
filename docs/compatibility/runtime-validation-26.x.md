# Minecraft 26.x Runtime Validation

Validation date: 2026-10-14

All checks below were run from the isolated `versions/26.x` Gradle project with Java 25, Fabric Loader 0.19.5, and the version-specific Fabric API listed in the release matrix.

| Minecraft | Dedicated server | Client bootstrap | Registration evidence | Manual gameplay |
|---|---|---|---|---|
| 26.1 | passed; reached `Done` and saved all dimensions | passed; reached graphics, audio, resource reload, and texture-atlas initialization | 151 fish, four rods, journal | pending |
| 26.1.1 | passed; reached `Done` and saved all dimensions | passed; reached graphics, audio, resource reload, and texture-atlas initialization | 151 fish, four rods, journal | pending |
| 26.1.2 | passed; reached `Done` and saved all dimensions | passed; reached graphics, audio, resource reload, and texture-atlas initialization | 151 fish, four rods, journal | pending |
| 26.2 | passed; reached `Done` with the 26.2 fishing loot schema | passed; reached graphics, audio, resource reload, and texture-atlas initialization | 151 fish, four rods, journal | pending |
| 26.3 | passed; reached `Done` and saved all dimensions | passed; reached graphics, audio, resource reload, and texture-atlas initialization | 151 fish, four rods, journal | pending |

The Windows OSHI performance-counter warnings and unauthenticated local Realms messages are environment noise observed during launch; neither prevented the client or server from reaching its validation milestone.

This document records bootstrap evidence only. Public support remains fail-closed until manual in-game fishing, journal, rod rendering, advancement, persistence, and progression checks are completed for each release.
