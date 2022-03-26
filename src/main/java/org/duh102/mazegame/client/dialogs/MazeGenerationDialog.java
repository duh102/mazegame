package org.duh102.mazegame.client.dialogs;

import org.duh102.mazegame.util.Point2DInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

public class MazeGenerationDialog extends JDialog implements PropertyChangeListener, ActionListener {
    private JComboBox<String> generatorCombo;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JFormattedTextField seedField;
    private JButton okButton, cancelButton;
    private int exitMethod = -1;

    public MazeGenerationDialog(Frame frame, List<String> generatorList) {
        super(frame, true);
        generatorCombo = new JComboBox<>(generatorList.toArray(new String[]{}));
        generatorCombo.setSelectedIndex(0);
        SpinnerNumberModel model1 = new SpinnerNumberModel(10, 2, 100, 1);
        SpinnerNumberModel model2 = new SpinnerNumberModel(10, 2, 100, 1);
        xSpinner = new JSpinner(model1);
        ySpinner = new JSpinner(model2);
        xSpinner.setValue(10);
        ySpinner.setValue(10);
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        seedField = new JFormattedTextField(format);
        seedField.setColumns(19);
        seedField.setValue((new Random()).nextLong());
        seedField.addPropertyChangeListener(this);
        okButton = new JButton("Create");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JLabel algLabel = new JLabel("Algorithm");
        JLabel xSizeLabel = new JLabel("Maze Width");
        JLabel ySizeLabel = new JLabel("Maze Height");
        JLabel seedLabel = new JLabel("Seed");


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
        panel.add(algLabel, c);
        c.gridy = 1;
        panel.add(xSizeLabel, c);
        c.gridy = 2;
        panel.add(ySizeLabel, c);
        c.gridy = 3;
        panel.add(seedLabel, c);
        c.gridy = 4;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(okButton, c);

        // right column
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 6;
        c.anchor = GridBagConstraints.WEST;
        panel.add(generatorCombo, c);
        c.gridy = 1;
        panel.add(xSpinner, c);
        c.gridy = 2;
        panel.add(ySpinner, c);
        c.gridy = 3;
        panel.add(seedField, c);
        c.gridy = 4;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(cancelButton, c);

        setLocationRelativeTo(frame);
        add(panel);
    }

    public int getExitMethod() {
        return exitMethod;
    }
    public int getChosenGenerator() {
        return generatorCombo.getSelectedIndex();
    }
    public Point2DInt getChosenSize() {
        return Point2DInt.of((int)xSpinner.getValue(), (int)ySpinner.getValue());
    }
    public long getSeed() {
        return ((Number)seedField.getValue()).longValue();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(!propertyChangeEvent.getSource().equals(seedField)) {
            return;
        }
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
