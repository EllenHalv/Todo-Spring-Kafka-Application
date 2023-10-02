package com.ellencodes.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class TaskComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JTextPane taskField;
    private final JButton deleteButton;
    private final JPanel parentPanel;

    public TaskComponent(JPanel parentPanel) {
        this.parentPanel = parentPanel;

        // task field
        taskField = new JTextPane();
        taskField.setPreferredSize(new Dimension(200, 40));
        taskField.setContentType("text/html");

        // checkbox
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(new Dimension(50, 50));
        checkBox.addActionListener(this);

        // delete button
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(50, 50));
        deleteButton.addActionListener(this);

        // add to this task component
        this.add(checkBox);
        this.add(taskField);
        this.add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(checkBox.isSelected()) {
            taskField.setText("<html><body><strike>" + taskField.getText() + "</strike></body></html>");
        } else if (!checkBox.isSelected()){
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            taskField.setText(taskText);
        }

        if(e.getActionCommand().equalsIgnoreCase("X")){
            parentPanel.remove(this);
            parentPanel.repaint();
            parentPanel.revalidate();
        }
    }
}
