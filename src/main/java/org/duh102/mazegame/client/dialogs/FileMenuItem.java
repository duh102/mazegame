package org.duh102.mazegame.client.dialogs;

import static java.awt.event.KeyEvent.*;

public enum FileMenuItem {
    SAVE("Save Maze", VK_S, "fsm"),
    LOAD("Load Maze", VK_L, "flm"),
    QUIT("Quit", VK_Q, "fq");

    private String displayText;
    private int keyMnemonic;
    private String actionEvent;

    FileMenuItem(String displayText, int keyMnemonic, String actionEvent) {
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
