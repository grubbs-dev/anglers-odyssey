# Minecraft 26.x compatibility audit

Status: **build and registry gates complete — runtime support gates remain open**.

Minecraft 26.1 is the first unobfuscated Java release. Fabric's migration guidance requires the new `net.fabricmc.fabric-loom` workflow, Java 25, and official Mojang names. Existing 1.20.x and 1.21.x adapters remain the release baseline and are not being rewritten here.

## Stable release scope

| Minecraft | Fabric Loader baseline | Fabric API candidate | Java | Biome audit | Port status |
|---|---:|---:|---:|---|---|
| 26.1 | 0.19.5 | 0.145.1+26.1 | 25 | 65 vanilla keys; registry extracted from 26.1 sources | build, registry, and dedicated-server gates passed; client/manual pending |
| 26.1.1 | 0.19.5 | 0.145.4+26.1.1 | 25 | same registry as 26.1 | build, registry, and dedicated-server gates passed; client/manual pending |
| 26.1.2 | 0.19.5 | 0.155.3+26.1.2 | 25 | same registry as 26.1 | build, registry, and dedicated-server gates passed; client/manual pending |
| 26.2 | 0.19.5 | 0.161.0+26.2 | 25 | 65 vanilla keys; registry audit passed; Vulkan/backend and fishing-loot schema audited | build, registry, and dedicated-server gates passed; client/manual pending |
| 26.3 | 0.19.5 | 0.163.0+26.3 | 25 | 66 vanilla keys; adds `minecraft:dappled_forest` | build, registry, and dedicated-server gates passed; client/manual pending |

There is no stable 26.2.1 in the Fabric game-version metadata checked for this sweep. Snapshot/pre-release builds are excluded.

## Required 26.x migration areas

- Replace Yarn/intermediary names with official names and use Loom's no-remap `jar` flow.
- Migrate item components, `PersistentState`/NBT persistence, fishing-hook/projectile APIs, networking, creative tabs, client rendering/input, and resource/data pack formats.
- Re-audit every vanilla biome and the 151-fish eligibility matrix, including Dappled Forest in 26.3.

## Release gate

No 26.x artifact may be uploaded until the matching release passes registry/biome/fish/persistence checks, a dedicated-server bootstrap, a client bootstrap, and manual fishing/journal smoke tests. A failed matrix row must not publish an artifact.
