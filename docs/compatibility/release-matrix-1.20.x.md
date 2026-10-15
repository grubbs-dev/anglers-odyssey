# Minecraft 1.20.x compatibility matrix

Each row is a separate distributable. The original 1.20.1 release remains recoverable in Git; this row is the repaired 1.20.1 build.

| Minecraft | Mod version | Adapter band | Build | Data/resource audit | Runtime smoke | Release status |
|---|---:|---|---|---|---|---|
| 1.20.1 | 1.0.2 | isolated pre-`AdvancementEntry`/pre-`PersistentState.Type` adapter; cast-state and advancement-scroll fixes | passed | passed | client bootstrap passed; manual fishing/UI test pending | candidate |
| 1.20.2 | 1.0.2 | registry, advancement, persistence, client-screen | passed | passed | client and dedicated server passed | supported |
| 1.20.3 | 1.0.3 | shared pre-component adapter | passed | passed | client and dedicated server passed | supported |
| 1.20.4 | 1.0.4 | shared pre-component adapter | passed | passed | client and dedicated server passed | supported |
| 1.20.5 | 1.0.7 | item components, Java 21, fishing enchantment tag, advancement grant fix | passed | passed | automated build passed; manual enchantment/advancement test pending | candidate |
| 1.20.6 | 1.0.8 | same component-era adapter as 1.20.5 | passed | passed | automated build passed; manual enchantment/advancement test pending | candidate |

The 1.20.2–1.20.4 artifacts use the legacy NBT-era fishing data. The 1.20.5–1.20.6 artifacts use `minecraft:custom_data`, component-era recipe results, and component-era advancement icons. Dedicated-server smoke tests reached `Done` for every row with 151 fish, four rods, and the journal registered, and without Angler's Odyssey resource errors. The older 1.20.2–1.20.4 logs report Minecraft's normal seven-recipe startup count; this does not indicate missing mod recipes.

The 1.21 and 1.21.1 maintenance builds use the modern registry-backed enchantment component lookup. Their automated builds pass; manual enchantment, fishing-bonus, and 1.21.1 cast-texture checks remain required before publication.

Official audit references include the [Minecraft 1.20.2 technical notes](https://feedback.minecraft.net/hc/en-us/articles/19703470383757-Minecraft-Java-Edition-1-20-2), [Minecraft 1.20.3 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-3), [Minecraft 1.20.5 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-5), and [Minecraft 1.20.6 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-6).
