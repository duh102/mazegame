package org.duh102.mazegame.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
    private List<String> searchPaths;

    public TileSetRegistry(List<String> searchPaths) {
        this.searchPaths = searchPaths;
    }

    public TileSetRegistry setSearchPaths(List<String> searchPaths) {
        this.searchPaths = searchPaths;
        return this;
    }

    public TileSetRegistry rescan() {
        discoveredTileSets = new ArrayList<>();
        List<TileSet> found = scanPath(".");
        discoveredTileSets.addAll(found);
        for(String searchPath : searchPaths) {
            found = scanPath(searchPath);
            discoveredTileSets.addAll(found);
        }
        return this;
    }
    public List<TileSet> scanPath(String path) {
        File searchDirectory = new File(path);
        List<File> foundFiles = new ArrayList<>();
        if(searchDirectory.exists() && searchDirectory.isDirectory()) {
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
                    foundTileSets.add(tileSet);
                } catch (FileNotFoundException | JsonSyntaxException | JsonIOException e) {
                    System.err.printf("Didn't find a valid TileSet in %s\n", tileFile.toString());
                    e.printStackTrace();
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
