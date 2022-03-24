package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.graphics.MazeResizeComponentListener;
import org.duh102.mazegame.util.BeanRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    BeanRegistry registry;
    MazeDisplay gameDisplay;
    ImageIcon gameIcon;

    public GameWindow(KeyListener kl, MazeResizeComponentListener mazeResizeListener, MazeDisplay display, BeanRegistry registry) {
        this.registry = registry;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Duh102's MazeGame");
        addKeyListener(kl);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(fileMenu);
        menuBar.addKeyListener(kl);

        JMenuItem item = new JMenuItem("Load Maze", KeyEvent.VK_L);
        item.addKeyListener(kl);
        fileMenu.add(item);

        item = new JMenuItem("Load Tileset", KeyEvent.VK_O);
        item.addKeyListener(kl);
        fileMenu.add(item);

        item = new JMenuItem("Set Tileset", KeyEvent.VK_T);
        item.addKeyListener(kl);
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem("Quit", KeyEvent.VK_Q);
        item.addKeyListener(kl);
        fileMenu.add(item);

        setJMenuBar(menuBar);

        gameDisplay = display;
        gameIcon = new ImageIcon(gameDisplay.redraw().getActiveImage());
        JLabel mazeContainer = new JLabel(gameIcon);
        mazeContainer.addComponentListener(mazeResizeListener);
        add(mazeContainer, BorderLayout.CENTER);

        setLocationByPlatform(true);
        setPreferredSize(new Dimension(640, 500));
    }

    public GameWindow updateImage() {
        gameIcon.setImage(gameDisplay.redraw().getActiveImage());
        repaint();
        return this;
    }

    public GameWindow packAndShow() {
        this.pack();
        this.setVisible(true);
        return this;
    }
}
