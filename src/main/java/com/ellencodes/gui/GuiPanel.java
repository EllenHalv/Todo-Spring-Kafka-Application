package com.ellencodes.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiPanel extends JPanel implements ActionListener {

    private final JPanel taskComponentPanel;

    public GuiPanel() {
        JPanel panel = new JPanel();

        JFrame frame = new JFrame("Todo List Application");
        frame.setSize(410, 825);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        // add label for title
        JLabel titleLabel = new JLabel("Todo List");
        titleLabel.setBounds(150, 0, 80, 25);
        panel.add(titleLabel);

        JPanel taskPanel = new JPanel();

        // add task panel
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        taskPanel.add(taskComponentPanel);

        // add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBounds(10, 40, 380, 700);
        scrollPane.setMaximumSize(scrollPane.getPreferredSize());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        // new task button
        JButton newTaskButton = new JButton("New Task");
        newTaskButton.setBounds(10, 750, 100, 25);
        newTaskButton.addActionListener(this);
        panel.add(newTaskButton);

        frame.setVisible(true);

        /*// add task button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(100, 750, 100, 25);
        addTaskButton.addActionListener(this);
        panel.add(addTaskButton);

        frame.setVisible(true);*/
    }

    //todo: check for tasks in the database and add them to the gui (each task is a task component)

    @Override
    public void actionPerformed(ActionEvent e) {
        //action listener for the new task button
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("New Task")) {
            // create a task component
            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
            taskComponentPanel.add(taskComponent);

            // make the task field request focus after creation
            taskComponent.getTaskField().requestFocus();
            taskComponent.revalidate();
            taskComponent.repaint();
        }

        //action listener for the add task button
        /*if (command.equalsIgnoreCase("Add Task")) {
            // Get the user's input from GUI components
            String taskName = taskField.getText();
            boolean done = *//* Determine the done status from the GUI or user input *//*;

            // Create a new Task object
            Task task = new Task();
            task.setTaskName(taskName);
            task.setDone(done);

            // Use the TaskService to save the task to the database
            taskService.saveTask(task);
        }*/

    }

    //todo: en consumer ska skriva ut datan i gui konsollen
    //todo: en consumer ska spara datan i databasen

}

