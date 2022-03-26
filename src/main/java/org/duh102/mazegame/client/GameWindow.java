package org.duh102.mazegame.client;

import org.duh102.mazegame.client.dialogs.FileMenuItem;
import org.duh102.mazegame.client.dialogs.MazeMenuItem;
import org.duh102.mazegame.client.dialogs.VisualMenuItem;
import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.graphics.MazeResizeComponentListener;
import org.duh102.mazegame.util.beanreg.BeanRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    BeanRegistry registry;
    MazeDisplay gameDisplay;
    ImageIcon gameIcon;

    public GameWindow(KeyListener kl, ActionListener actionListener, MazeResizeComponentListener mazeResizeListener, MazeDisplay display, BeanRegistry registry) {
        this.registry = registry;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Duh102's MazeGame");
        addKeyListener(kl);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu mazeMenu = new JMenu("Maze");
        JMenu visualMenu = new JMenu("Visual");

        fileMenu.setMnemonic(KeyEvent.VK_F);
        mazeMenu.setMnemonic(KeyEvent.VK_M);
        visualMenu.setMnemonic(KeyEvent.VK_V);

        menuBar.add(fileMenu);
        menuBar.add(mazeMenu);
        menuBar.add(visualMenu);
        menuBar.addKeyListener(kl);

        JMenuItem item;
        for(FileMenuItem fmi : FileMenuItem.values()) {
            item = new JMenuItem(fmi.getDisplayText(), fmi.getKeyMnemonic());
            item.addKeyListener(kl);
            item.setActionCommand(fmi.getActionEvent());
            item.addActionListener(actionListener);
            fileMenu.add(item);
        }
        for(MazeMenuItem mmi : MazeMenuItem.values()) {
            item = new JMenuItem(mmi.getDisplayText(), mmi.getKeyMnemonic());
            item.addKeyListener(kl);
            item.setActionCommand(mmi.getActionEvent());
            item.addActionListener(actionListener);
            mazeMenu.add(item);
        }
        for(VisualMenuItem vmi : VisualMenuItem.values()) {
            item = new JMenuItem(vmi.getDisplayText(), vmi.getKeyMnemonic());
            item.addKeyListener(kl);
            item.setActionCommand(vmi.getActionEvent());
            item.addActionListener(actionListener);
            visualMenu.add(item);
        }

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
