package org.duh102.mazegame.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.duh102.mazegame.model.serialization.MazeCustomizedGSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private List<String> tileSetSearchPaths = new ArrayList<>();
    public Config() {
    }

    public static Config loadFromFile(File location) throws FileNotFoundException {
        if(location == null) {
            throw new FileNotFoundException("File was null");
        }
        if(!(location.exists() && location.isFile()) ) {
            throw new FileNotFoundException(location.toString());
        }
        try {
            Gson json = MazeCustomizedGSON.getGson();
            FileReader reader = new FileReader(location);
            Config config = json.fromJson(reader, Config.class);
            return config;
        } catch (JsonSyntaxException | JsonIOException e) {
            System.err.printf("Unable to read config from %s", location.getPath());
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getTileSetSearchPaths() {
        return tileSetSearchPaths;
    }
}
