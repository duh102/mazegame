package org.duh102.mazegame.client.dialogs;

import static java.awt.event.KeyEvent.*;

public enum MazeMenuItem {
    NEW("New Blank Maze", VK_N, "mb"),
    GENERATE("Generate Maze", VK_G, "mg"),
    EDIT("Toggle Edit Mode", VK_E, "me");

    private String displayText;
    private int keyMnemonic;
    private String actionEvent;

    MazeMenuItem(String displayText, int keyMnemonic, String actionEvent) {
        this.displayText = displayText;
        this.keyMnemonic = keyMnemonic;
        this.actionEvent = actionEvent;
    }

    public String getDisplayText() {
        return displayText;
    }
    public int getKeyMnemonic() {
        return keyMnemonic;
    }
    public String getActionEvent() {
        return actionEvent;
    }
}
