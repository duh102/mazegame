# maze.json File Format
A maze.json file describes a maze. There are three properties:

* tiles
  * A 2d array of tile objects (see [Tile Object](#tile-object))
* entrance
  * An integer point location within the maze, indexed from the top left
* exit
  * An integer point location within the maze, indexed from the top left

The minimum size for a maze is 2x2 tiles, but it need not be square. It also can be sparsely defined, with nulls at locations in the tile grid.


Example
```
{
  "tiles": [
    [
      {
        "exits": 2,
        "variantSeed": 1431162155
      },
      {
        "exits": 2,
        "variantSeed": -73789608
      }
    ],
    [
      {
        "exits": 12,
        "variantSeed": -1728529858
      },
      {
        "exits": 9,
        "variantSeed": 0
      }
    ]
  ],
  "entrance": {
    "x": 1,
    "y": 1
  },
  "exit": {
    "x": 0,
    "y": 0
  }
}
```

## Tile Object
A tile describes a rectangular floor section, and is the smallest unit of movement. Each tile can have up to 4 exits: up, right, down, and left.

A tile object is composed of two properties:

* exits
  * A bitmask of the valid exits from this tile
  * Up 1
  * Right 2
  * Down 4
  * Left 8
  * To create an exits value, add up the corresponding numbers for the directions you want the tile to exit from
* variantSeed
  * A 64 bit integer value that will seed the RNG to pick a tile variant for the selected tileset

Example:
```
{
  "exits": 6,
  "variantSeed": -73789608
}
```
Given that the exits value is 6, we can determine that this tile has valid exits to the right and down (2+4).
