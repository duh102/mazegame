package org.duh102.mazegame.client.dialogs;

import static java.awt.event.KeyEvent.*;

public enum VisualMenuItem {
    LOAD("Load TileSet from File", VK_L, "tl"),
    SET("Set Loaded TileSet", VK_S, "ts");

    private String displayText;
    private int keyMnemonic;
    private String actionEvent;

    VisualMenuItem(String displayText, int keyMnemonic, String actionEvent) {
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
