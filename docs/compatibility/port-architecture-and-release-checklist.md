# Port architecture and release gates

## Architecture

The canonical fish catalog, rarity rules, habitat model, selector, records, journal state, progression, and loot semantics stay in shared source sets. A Minecraft-version adapter owns only APIs that moved: item/data-component access, persistent-state construction, custom payload registration, advancement lookup, tooltips, rendering hooks, and mixin targets. Each supported version gets its own Gradle dependency set and adapter package; it does not get a forked copy of fish gameplay.

The current 1.21.1 branch is the first adapter implementation. The 1.20.1 release remains available at tag `v1.0.0` and is not rewritten by this port.

## Prioritized sequence

1. Lock the 1.20.1 baseline and catalog (151 fish, resources, journal keys, advancements, and release artifact).
2. Complete 1.21.1 API/data-component/payload/persistence migration and compile gates.
3. Add deterministic fish-obtainability and per-biome pool tests, including empty-pool failures.
4. Run dedicated-server and client smoke tests for 1.21.1; record logs and world-save migration results. **Complete for 1.21.1.**
5. Port 1.20.2–1.21 patch releases in order, recording official release-note and registry diffs for each.
6. Expand the CI matrix and artifact naming only as each version passes its own gates.
7. Prepare release assets and notes; publish only after explicit authorization.

## Release-validation checklist

- [x] Correct Minecraft/Fabric Loader/Fabric API dependencies and mod metadata for 1.21.1.
- [x] Registry audit against the actual game JAR; every vanilla biome classified.
- [x] All 151 fish have a valid effective habitat and at least one obtainable pool per intended restriction.
- [x] Rarity, progression, shiny, trophy, and vanilla enchantment behavior smoke-tested.
- [x] Journal and fish-record saves load and round-trip in the 1.21.1 development world.
- [x] Datapack/resource validation passes, including singular paths and component-era schemas.
- [x] `build` and version-qualified remapped JAR pass.
- [x] Dedicated server launches with the mod, loads a new world, casts/retrieves, and saves cleanly.
- [x] Client launches, renders rods/fish, opens the journal, receives the journal payload, and closes cleanly.
- [x] Compatibility report and changelog identify automated versus manual results.
- [ ] No public release or upload occurs without explicit user authorization.
