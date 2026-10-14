# Minecraft compatibility audit

This matrix is the source of truth for port work. A version is not marked supported until its build, registry/data checks, dedicated-server smoke test, and client smoke test pass. Patch releases are tracked explicitly because mappings, loader/API versions, data-pack paths, and registries can change independently of gameplay additions.

| Release | Vanilla biome delta from 1.20.1 | Port status | Validation state |
|---|---|---|---|
| 1.20.1 | Baseline: 64 registry biome JSONs in the released implementation | released | existing release baseline |
| 1.20.2 | No new vanilla biome JSON identified; networking, reporting, and data-pack behavior changed | audit complete | baseline comparison recorded |
| 1.20.3 | No new vanilla biome JSON identified | planned | pending adapter |
| 1.20.4 | No new vanilla biome JSON identified | planned | pending adapter |
| 1.20.5 | No new vanilla biome JSON identified; item components and custom payload networking introduced | planned | pending adapter |
| 1.20.6 | No new vanilla biome JSON identified | planned | pending adapter |
| 1.21 | No new vanilla biome JSON identified; enchantments/data components and singular data paths changed | planned | pending adapter |
| 1.21.1 | Registry baseline audited from the game JAR (64 biome JSONs); no new biome versus 1.21 | supported in 1.1.0 | build, registry/data checks, dedicated-server smoke, and client smoke passed |

## 1.21.1 migration matrix

| Surface | 1.20.1 implementation | 1.21.1 adapter/change | Gate |
|---|---|---|---|
| Items/food | `FabricItemSettings`, `FoodComponent.hunger` | `Item.Settings`, component `FoodComponent.nutrition` | compile + item registration |
| Fish flags/measurements | `ItemStack` root NBT helpers | `minecraft:custom_data` (`NbtComponent`), preserving keys | persistence migration test |
| Tooltips | `(stack, world, list, context)` | `(stack, Item.TooltipContext, list, TooltipType)` | client compile/smoke |
| Journal persistence | functional serializer callbacks | `PersistentState.Type` + lookup-aware `writeNbt` | save/reload test |
| Journal networking | identifier + `PacketByteBuf` | registered `CustomPayload` + `PacketCodec` | client/server packet test |
| Advancements | `Advancement` | `AdvancementEntry` | advancement grant test |
| Fishing rod durability | break callback | `EquipmentSlot` overload | dedicated-server cast/retrieve |
| Data-pack paths | plural paths | singular `advancement`, `recipe`, `loot_table`, `tags/item` | resource load check |
| Rendering | old screen background/mouse scroll signatures | 1.21.1 signatures | client smoke |

Official references: [Minecraft 1.20.2 notes](https://feedback.minecraft.net/hc/en-us/articles/19703470383757-Minecraft-Java-Edition-1-20-2), [Fabric 1.21/1.21.1 migration notes](https://www.fabricmc.net/2024/05/31/121.html), and [Fabric networking documentation](https://docs.fabricmc.net/develop/networking).
