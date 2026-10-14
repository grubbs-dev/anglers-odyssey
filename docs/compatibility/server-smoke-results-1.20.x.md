# Dedicated-server smoke results

These are isolated `runServer` smoke runs from the sweep branch. Each run used its own run directory, accepted EULA, and was stopped cleanly after the server reported `Done`.

| Minecraft | Mod | Loader | Java target | Result |
|---|---:|---:|---:|---|
| 1.20.2 | 1.0.2 | 0.14.22 | 17 | passed |
| 1.20.3 | 1.0.3 | 0.15.0 | 17 | passed |
| 1.20.4 | 1.0.4 | 0.15.6 | 17 | passed |
| 1.20.5 | 1.0.5 | 0.15.10 | 21 | passed |
| 1.20.6 | 1.0.6 | 0.15.11 | 21 | passed |

Every run registered 151 custom fish, four custom rods, and the Angler's Journal. No Angler's Odyssey resource or recipe parsing errors remained after the 1.20.5+ journal recipe result was changed to the component-era `id` field. The server logs reached `Done` before a clean `stop` command.
