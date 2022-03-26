package org.duh102.mazegame.client.dialogs;

import org.duh102.mazegame.util.Point2DInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewMazeDialog extends JDialog implements ActionListener {
    private final JSpinner xSpinner;
    private final JSpinner ySpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    private int exitMethod = -1;

    public NewMazeDialog(Frame parent) {
        super(parent, true);

        SpinnerNumberModel model1 = new SpinnerNumberModel(10, 2, 100, 1);
        SpinnerNumberModel model2 = new SpinnerNumberModel(10, 2, 100, 1);
        xSpinner = new JSpinner(model1);
        ySpinner = new JSpinner(model2);

        okButton = new JButton("Create");
        okButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JLabel xSizeLabel = new JLabel("Maze Width");
        JLabel ySizeLabel = new JLabel("Maze Height");

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(layout);


        // left column
        c.weightx = 0;
        c.ipady = 10;
        c.insets = new Insets(3,0,3,0);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 6;
        c.anchor = GridBagConstraints.EAST;
        panel.add(xSizeLabel, c);
        c.gridy = 1;
        panel.add(ySizeLabel, c);
        c.gridy = 2;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(okButton, c);
        // right column
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 6;
        c.anchor = GridBagConstraints.WEST;
        panel.add(xSpinner, c);
        c.gridy = 1;
        panel.add(ySpinner, c);
        c.gridy = 2;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(cancelButton, c);

        setLocationRelativeTo(parent);
        add(panel);
    }
    public Point2DInt getChosenSize() {
        return Point2DInt.of((int)xSpinner.getValue(), (int)ySpinner.getValue());
    }
    public int getExitMethod() {
        return exitMethod;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(okButton)) {
            exitMethod = 0;
            dispose();
        } else if (actionEvent.getSource().equals(cancelButton)) {
            exitMethod = 1;
            dispose();
        }
    }
}
