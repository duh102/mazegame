package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.MazeDisplay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimationController {
    public static final long MAX_FPS = 120;
    private long targetFps;
    private long targetDT;
    private long lastUpdate = -1;
    private Boolean running = false;
    private double targetMoveSpeed = 300; // in pixels per second
    private ExecutorService scheduledThreadPool = Executors.newFixedThreadPool(1);
    private MazeDisplay display;
    private MazeControlListener controlListener;
    private GameWindow gameWindow;

    public AnimationController(int targetFps) {
        this.targetFps = Math.min(targetFps, MAX_FPS);
        this.targetDT = 1000/targetFps;
        lastUpdate = System.currentTimeMillis();
    }
    public AnimationController() {
        this(60);
    }

    public AnimationController setMazeDisplay(MazeDisplay mazeDisplay) {
        this.display = mazeDisplay;
        return this;
    }

    public AnimationController setControlListener(MazeControlListener controlListener) {
        this.controlListener = controlListener;
        return this;
    }

    public AnimationController setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        return this;
    }

    public synchronized AnimationController start() {
        running = true;
        scheduledThreadPool.submit(this::infiniteLoop);
        return this;
    }
    public synchronized AnimationController stop() {
        running = false;
        scheduledThreadPool.shutdown();
        return this;
    }
    public void infiniteLoop() {
        boolean outerRun = true;
        while(outerRun) {
            updateState();
            synchronized (this) {
                outerRun = running;
            }
        }
    }
    public void updateState() {
        long currentTime = System.currentTimeMillis();
        long dT = currentTime - lastUpdate;
        if(dT == 0) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException ie) {
                // if we get interrupted continually, we have problems
            }
            return;
        }
        lastUpdate = currentTime;
        double pixelsToMove = (dT+0.0)/1000 * (targetMoveSpeed);
        if(controlListener != null) {
            controlListener.notifyMovement();
        }
        if(display != null) {
            display.setIncrementalMovement(pixelsToMove);
        }
        if(gameWindow != null) {
            gameWindow.updateImage();
        }
        long updateTime = System.currentTimeMillis();
        long updateDt = updateTime - currentTime;
        long sleepTime = targetDT - updateDt;
        try {
            Thread.sleep(sleepTime);
        } catch(InterruptedException ie) {
            // do nothing, we'll compensate in how much we move
        }
    }
}
