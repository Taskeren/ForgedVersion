# tVersion
MinecraftForge-styled Versioning

## Versioning Json Style

```json5
{
  "homepage": "<homepage/download page for your mod>",
  "<mcversion>": {
    "<modversion>": "<changelog for this version>", 
    // List all versions of your mod for the given Minecraft version, along with their changelogs
    // ...
  },
  "promos": {
    "<mcversion>-latest": "<modversion>",
    // Declare the latest "bleeding-edge" version of your mod for the given Minecraft version
    "<mcversion>-recommended": "<modversion>",
    // Declare the latest "stable" version of your mod for the given Minecraft version
    // ...
  }
}
```

`<mcversion>` is the version of Parent platform like Plugin Loader, etc.
`<modversion>` is the version of the Application or Plugin by you.
