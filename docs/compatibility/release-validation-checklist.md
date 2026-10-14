# Release validation checklist

An artifact is a release candidate only when every applicable gate below is recorded for its exact Minecraft version.

## Automated gates

- [ ] Matrix build uses the target Minecraft, Yarn, Fabric Loader, Fabric API, mod, and Java values.
- [ ] `clean check` passes, including `validateCompatibility`.
- [ ] All 151 fish definitions are present and the generated fish inventory matches the source database.
- [ ] All 69 advancements, 16 biome-region tags, five rarity fishing tables, and all resource JSON files validate.
- [ ] The remapped JAR manifest names the exact Minecraft version and minimum Java version.
- [ ] The JAR contains the adapter-appropriate fishing data format: legacy NBT for 1.20.2–1.20.4 or components for 1.20.5–1.20.6.
- [ ] The artifact filename includes both the mod version and Minecraft version.

## Runtime gates

- [ ] Client launches with the target JAR and no Angler's Odyssey resource or mixin errors.
- [ ] Dedicated server reaches `Done` with the target JAR.
- [ ] A fresh world can obtain Angler's Odyssey fish in each applicable habitat and progression tier.
- [ ] Existing-world persistence, journal records, advancements, rods, and fishing behavior remain intact.
- [ ] Vanilla fishing remains available and no intended Angler's Odyssey pool is empty.

## Publication gate

- [ ] Compatibility matrix is updated with the exact artifact hash and test evidence.
- [ ] CurseForge version entry targets only the validated Minecraft version.
- [ ] Public release is authorized by the maintainer.
