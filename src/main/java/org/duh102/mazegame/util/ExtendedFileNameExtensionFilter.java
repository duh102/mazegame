package org.duh102.mazegame.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ExtendedFileNameExtensionFilter extends FileFilter {
    private String description;
    private List<String> extensions;

    public ExtendedFileNameExtensionFilter(String description, String ... extensions) {
        this.description = description;
        this.extensions = Arrays.asList(extensions);
    }

    @Override
    public boolean accept(File file) {
        if(file != null) {
            if(file.isDirectory()) {
                return true;
            }
            String filename = file.getName();
            for(String extension : extensions) {
                if(filename.toLowerCase().endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
