package org.duh102.mazegame.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.duh102.mazegame.model.serialization.MazeCustomizedGSON;

import java.io.*;
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
    public Config writeToFile(File location) throws IOException {
        if(location == null) {
            throw new FileNotFoundException("File was null");
        }
        Gson json = MazeCustomizedGSON.getGsonBase().serializeNulls().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(location);
        json.toJson(this, writer);
        return this;
    }

    public List<String> getTileSetSearchPaths() {
        return tileSetSearchPaths;
    }
}
