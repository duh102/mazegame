package org.duh102.mazegame.client.state;

import org.duh102.mazegame.model.exception.client.InvalidGameStateTransition;

import java.util.Map;
import java.util.Set;

public class GameStateContainer {
    private GameState currentState = GameState.PLAYING;
    public GameStateContainer() {}

    private static final Map<GameState, Set<GameState>> VALID_TRANSITION_MAP = Map.of(
            GameState.PLAYING, Set.of(GameState.EDITING, GameState.PLAYING, GameState.WON),
            GameState.EDITING, Set.of(GameState.EDITING, GameState.PLAYING),
            GameState.WON, Set.of(GameState.EDITING, GameState.PLAYING)
    );

    public synchronized GameState getState() {
        return currentState;
    }
    public synchronized GameStateContainer transition(GameState newState) throws InvalidGameStateTransition {
        if(!canTransition(currentState, newState)) {
            throw new InvalidGameStateTransition(currentState, newState);
        }
        currentState = newState;
        return this;
    }
    public static boolean canTransition(GameState current, GameState newState) {
        return VALID_TRANSITION_MAP.get(current).contains(newState);
    }
}
