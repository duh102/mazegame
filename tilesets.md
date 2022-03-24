# Creating Custom Tilesets
Creating a new tileset is fairly straightforward. You will need three things:
1. A single image containing all of the tiles
2. A single image depicting the character
3. A tileset.json file that provides information about your tileset

Tilesets are by default loaded from the "tileset" directory in the current working directory.
Each tileset is required to be in its own folder, but the images can be anywhere.

An example project structure is as follows:

```
tilesets/
 MyTileset/
  tileset.json
  tile_images.png
  character_image.png
```

### Syntax for tileset.json
tileset.json needs to have several parameters defined to work:
* tileSetName
* tileImages
* characterImage
* tileSize
* tileStartOffset
* characterImageOffset
* variants

Each is relatively self-explanatory, but in detail:

* tileSetName
  * Indicates the name that should be shown on the "Set TileSet" dialog
  * Can be anything, but should be unique, otherwise the program will get confused (should probably fix this tbh)
* tileImages
  * Path, relative to the tileset.json-containing directory, of where the floor tiles image is
* characterImage
  * Path, relative to the tileset.json-containing directory, of where the character image is
* tileSize
  * An object describing the width and height of a tile
  * The x property indicates width, in pixels, y indicates height
* tileStartOffset
  * An object describing where in the image the tile slicer should begin
  * This only applies if, in the image, your tiles have been placed at some offset from the top left of the image
  * The x property indicates horizontal offset from the left side of the image, in pixels, y indicates vertical offset from the top of the image
* characterImageOffset
  * An object describing how the character image should be manipulated to place the "foot" of the character in the middle of the spot it moves to
  * You can also think of it as, in pixels from the top and left of the image, the "root" of the image should be
  * The image will be moved this many pixels left and up relative to the center of the tile that the character is on
  * The first property indicates how many pixels left (subpixels accepted), the second property indicates how many pixels up
* variants
  * This property indicates how many variants of your tiles you have included in the tileset
  * Variants are read vertically, tile directions are read horizontally
  * See the "Creating a Tile Set Image" section for detailed instructions

Here is an example
```
{
  "tileSetName": "Grass 2x",
  "tileImages": "grass_tileset_2x_3.png",
  "characterImage": "grass_character_2x.png",
  "tileSize": {
    "x": 64,
    "y": 64
  },
  "tileStartOffset": {
    "x": 0,
    "y": 0
  },
  "variants": 2,
  "characterImageOffset": {
    "first": 16.0,
    "second": 20.0
  }
}
```

### Creating a Tile Set Image
Tile images are formatted in a very specific way and so your tile image must obey this format in order to display properly.
Begin by choosing a tile size; this may be rectangular and not square, but your entire image must be a multiple of this tile size.
The example tilesets included with the game are both square, with tile sizes of 64x64px and 128x128px
You must then create 16 tile shapes, in a specific order, leading to a total image size of at least 16*(tile width) pixels wide.
You are then free to create between one and infinite variants, leading to a total image size of at least (variants)*(tile height) pixels tall.

The order of the tile directions is as follows:
1. No exits
2. Up
3. Right
4. Up & Right
5. Down
6. Up & Down
7. Right & Down
8. Up & Right & Down
9. Left
10. Up & Left
11. Right & Left
12. Up & Right & Left
13. Down & Left
14. Up & Down & Left
15. Right & Down & Left
16. Up & Right & Down & Left

