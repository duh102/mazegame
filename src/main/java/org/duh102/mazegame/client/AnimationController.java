package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;

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

    private BeanRegistry registry;
    private CachedBeanRetriever<MazeDisplay> display;
    private CachedBeanRetriever<MazeControlListener> controlListener;
    private CachedBeanRetriever<GameWindow> gameWindow;

    public AnimationController(int targetFps, BeanRegistry registry) {
        this.registry = registry;
        display = new CachedBeanRetriever<>(registry, MazeDisplay.class);
        controlListener = new CachedBeanRetriever<>(registry, MazeControlListener.class);
        gameWindow = new CachedBeanRetriever<>(registry, GameWindow.class);

        this.targetFps = Math.min(targetFps, MAX_FPS);
        this.targetDT = 1000/targetFps;
        lastUpdate = System.currentTimeMillis();
    }
    public AnimationController(BeanRegistry registry) {
        this(60, registry);
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
        try {
            controlListener.get().notifyMovement();
        } catch(NoBeanFoundException nbfe) {
        }
        try {
            display.get().setIncrementalMovement(pixelsToMove);
        } catch(NoBeanFoundException nbfe) {
        }
        try {
            gameWindow.get().updateImage();
        } catch(NoBeanFoundException nbfe) {
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
