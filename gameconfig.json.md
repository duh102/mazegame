# gameconfig.json
The gameconfig.json file contains some options that can be persisted between game runs.

## tileSetSearchPaths
This property adds extra search paths for tilesets. By default, the game looks in (folder containing the jar file)/tilesets, but if you have more tilesets installed somewhere else you can enter paths in this list to also search those paths.

```

{
...
  "tileSetSearchPaths": [
    "/home/user/shared/tilesets",
    "/home/user/shared/downloaded/tilesets"
  ]
...
}
```
