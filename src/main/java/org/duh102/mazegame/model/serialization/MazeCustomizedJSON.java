package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.jr.annotationsupport.JacksonAnnotationExtension;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JacksonJrExtension;
import com.fasterxml.jackson.jr.ob.api.ExtensionContext;

public class MazeCustomizedJSON {
    public static JSON getJSON() {
        return JSON.builder().register(new JacksonJrExtension() {
            @Override
            protected void register(ExtensionContext extensionContext) {
                extensionContext.insertProvider(new MazeRWProvider());
            }
        }).register(JacksonAnnotationExtension.std).build();
    }
}
