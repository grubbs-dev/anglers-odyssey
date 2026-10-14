# Minecraft 1.20.x compatibility matrix

Each row is a separate distributable. The 1.20.1 release baseline remains unchanged in its own branch.

| Minecraft | Mod version | Adapter band | Build | Data/resource audit | Runtime smoke | Release status |
|---|---:|---|---|---|---|---|
| 1.20.1 | 1.0.1 | released baseline | passed | passed | previously tested | supported |
| 1.20.2 | 1.0.2 | registry, advancement, persistence, client-screen | passed | passed | outstanding manual client/server smoke | build candidate |
| 1.20.3 | 1.0.3 | shared pre-component adapter | passed | passed | outstanding manual client/server smoke | build candidate |
| 1.20.4 | 1.0.4 | shared pre-component adapter | passed | passed | outstanding manual client/server smoke | build candidate |
| 1.20.5 | 1.0.5 | item components, Java 21, networking, persistence, loot/recipe/advancement schemas | passed | passed | outstanding manual client/server smoke | build candidate |
| 1.20.6 | 1.0.6 | same component-era adapter as 1.20.5 | passed | passed | outstanding manual client/server smoke | build candidate |

The 1.20.2–1.20.4 artifacts use the legacy NBT-era fishing data. The 1.20.5–1.20.6 artifacts use `minecraft:custom_data`, component-era recipe results, and component-era advancement icons. A green Gradle build alone does not mark a row supported; the remaining runtime smoke gate must pass first.

Official audit references include the [Minecraft 1.20.2 technical notes](https://feedback.minecraft.net/hc/en-us/articles/19703470383757-Minecraft-Java-Edition-1-20-2), [Minecraft 1.20.3 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-3), [Minecraft 1.20.5 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-5), and [Minecraft 1.20.6 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-6).
