package org.duh102.mazegame.model.exception.client;

import org.duh102.mazegame.client.GameState;

public class InvalidGameStateTransition extends RuntimeException {
    public InvalidGameStateTransition() {
        super();
    }
    public InvalidGameStateTransition(String message) {
        super(message);
    }
    public InvalidGameStateTransition(GameState current, GameState next) {
        this(String.format("Invalid transition from %s to %s", current.name(), next.name()));
    }
}
