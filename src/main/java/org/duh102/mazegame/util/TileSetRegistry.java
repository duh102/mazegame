package org.duh102.mazegame.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.duh102.mazegame.model.exception.maze.tileset.TileSetException;
import org.duh102.mazegame.model.serialization.MazeCustomizedGSON;
import org.duh102.mazegame.model.tileset.TileSet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TileSetRegistry {
    private List<TileSet> discoveredTileSets = null;
    private File rootFolder;
    private List<String> searchPaths;

    public TileSetRegistry(File rootFolder) {
        this.rootFolder = rootFolder;
    }

    public TileSetRegistry setSearchPaths(List<String> searchPaths) {
        this.searchPaths = searchPaths;
        return this;
    }

    public TileSetRegistry rescan() {
        discoveredTileSets = new ArrayList<>();
        List<File> fullSearchPath = new ArrayList<>();
        fullSearchPath.add(new File(rootFolder, "tilesets"));
        if(searchPaths != null) {
            for(String path : searchPaths) {
                File pathFile = new File(path);
                if(pathFile.isAbsolute()) {
                    fullSearchPath.add(pathFile);
                } else {
                    fullSearchPath.add(new File(rootFolder, path));
                }
            }
        }
        for(File searchPath : fullSearchPath) {
            List<TileSet> found = scanPath(searchPath);
            discoveredTileSets.addAll(found);
        }
        return this;
    }
    public List<TileSet> scanPath(File searchDirectory) {
        List<File> foundFiles = new ArrayList<>();
        if(searchDirectory.exists() && searchDirectory.isDirectory()) {
            System.err.printf("Searching for tilesets in %s\n", searchDirectory.getAbsolutePath());
            File[] subDirs = searchDirectory.listFiles(tileSetContainingFolder);
            if(subDirs == null) {
                return new ArrayList<>();
            }
            for(File subdirectory : subDirs) {
                File tileSetFile = new File(subdirectory.getPath(), "tileset.json");
                foundFiles.add(tileSetFile);
            }
        }
        List<TileSet> foundTileSets = new ArrayList<>();
        if(foundFiles.size() > 0) {
            Gson jsonReader = MazeCustomizedGSON.getGson();
            for(File tileFile : foundFiles) {
                try {
                    FileReader reader = new FileReader(tileFile);
                    TileSet tileSet = jsonReader.fromJson(reader, TileSet.class);
                    if(!tileSet.isFullyDefined()) {
                        throw new TileSetException("Not fully defined");
                    }
                    String tileDir = tileFile.getParent();
                    tileSet.setTileFile(new File(tileDir, tileSet.getTileFile()).getPath());
                    tileSet.setCharacterFile(new File(tileDir, tileSet.getCharacterFile()).getPath());
                    tileSet.setEntranceFile(new File(tileDir, tileSet.getEntranceFile()).getPath());
                    tileSet.setExitFile(new File(tileDir, tileSet.getExitFile()).getPath());
                    foundTileSets.add(tileSet);
                } catch (FileNotFoundException | JsonSyntaxException | JsonIOException e) {
                    System.err.printf("Didn't find a valid TileSet in %s\n", tileFile.toString());
                    e.printStackTrace();
                } catch (TileSetException tse) {
                    System.err.printf("Exception in loading TileSet in %s: %s\n", tileFile.toString(), tse.getMessage());
                }
            }
        }
        return foundTileSets;
    }
    private final FileFilter tileSetContainingFolder = file -> {
        if(!file.isDirectory()) {
            return false;
        }
        File testFile = new File(file.getPath(), "tileset.json");
        return testFile.exists() && testFile.isFile();
    };

    public List<TileSet> getDiscoveredTileSets() {
        return discoveredTileSets;
    }
}
