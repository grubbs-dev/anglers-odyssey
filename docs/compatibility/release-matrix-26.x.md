# Angler's Odyssey — Minecraft 26.x release matrix

| Release | Build | Automated invariants | Dedicated server | Client | Manual gameplay | Publish |
|---|---|---|---|---|---|---|
| 26.1 | 1.2.13 | passed | passed | bootstrap passed | no |
| 26.1.1 | 1.2.13.1 | passed | pending rerun | pending rerun | no |
| 26.1.2 | 1.2.13.2 | passed | pending rerun | pending rerun | no |
| 26.2 | 1.2.13.3 | passed | pending rerun | pending rerun | no |
| 26.3 | 1.2.13.4 | passed | pending rerun | pending rerun | no |

The 26.1 row has been rebuilt with the item-definition and journal-layer fixes and its client bootstrap reaches the integrated world. The remaining rows use the same isolated official26 adapter but require their final rebuild/runtime rerun before publication. Manual fishing, item-texture, and journal smoke tests remain required for every row.
