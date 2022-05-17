# ForgedVersion

MinecraftForge-styled Versioning

## Forge Version Json

```json5
{
  "homepage": "<homepage/download page for your mod>",
  "<channel>": {
    "<build-version>": "<changelog for this version>", 
    // List all versions of your mod for the given Minecraft version, along with their changelogs
    // ...
  },
  "promos": {
    "<channel>-latest": "<build-version>",
    // Declare the latest "bleeding-edge" version of your mod for the given Minecraft version
    "<channel>-recommended": "<build-version>",
    // Declare the latest "stable" version of your mod for the given Minecraft version
    // ...
  }
}
```
