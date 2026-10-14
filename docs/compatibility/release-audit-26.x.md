# Minecraft 26.x compatibility audit

Status: **port in progress — no 26.x release is supported or publishable yet**.

Minecraft 26.1 is the first unobfuscated Java release. Fabric's migration guidance requires the new `net.fabricmc.fabric-loom` workflow, Java 25, and official Mojang names. Existing 1.20.x and 1.21.x adapters remain the release baseline and are not being rewritten here.

## Stable release scope

| Minecraft | Fabric Loader baseline | Fabric API candidate | Java | Biome audit | Port status |
|---|---:|---:|---:|---|---|
| 26.1 | 0.19.5 | 0.145.1+26.1 | 25 | 64 vanilla keys; no additions relative to 1.21.11 found | blocked on official-mappings source migration |
| 26.1.1 | 0.19.5 | 0.145.4+26.1.1 | 25 | same registry as 26.1 | follows 26.1 adapter after validation |
| 26.1.2 | 0.19.5 | 0.155.3+26.1.2 | 25 | same registry as 26.1 | follows 26.1 adapter after validation |
| 26.2 | 0.19.5 | 0.161.0+26.2 | 25 | registry must be extracted from the 26.2 jar; Vulkan/backend changes audited separately | not started |
| 26.3 | 0.19.5 | 0.163.0+26.3 | 25 | 65 expected; adds `minecraft:dappled_forest` | not started |

There is no stable 26.2.1 in the Fabric game-version metadata checked for this sweep. Snapshot/pre-release builds are excluded.

## Required 26.x migration areas

- Replace Yarn/intermediary names with official names and use Loom's no-remap `jar` flow.
- Migrate item components, `PersistentState`/NBT persistence, fishing-hook/projectile APIs, networking, creative tabs, client rendering/input, and resource/data pack formats.
- Re-audit every vanilla biome and the 151-fish eligibility matrix, including Dappled Forest in 26.3.

## Release gate

No 26.x artifact may be uploaded until the matching release passes registry/biome/fish/persistence checks, a dedicated-server bootstrap, a client bootstrap, and manual fishing/journal smoke tests. A failed matrix row must not publish an artifact.
