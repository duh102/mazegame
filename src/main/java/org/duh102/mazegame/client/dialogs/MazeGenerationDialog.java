package org.duh102.mazegame.client.dialogs;

import org.duh102.mazegame.util.Point2DInt;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;

public class MazeGenerationDialog extends JDialog implements PropertyChangeListener {
    private JComboBox<String> generatorCombo;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JFormattedTextField seedField;

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
        seedField = new JFormattedTextField(NumberFormat.getNumberInstance());
        seedField.setColumns(19);
        seedField.setValue(0);
        JPanel panel = new JPanel();
        panel.add(generatorCombo);
        panel.add(xSpinner);
        panel.add(ySpinner);
        panel.add(seedField);
        setLocationRelativeTo(frame);
        add(panel);
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

    }
}
