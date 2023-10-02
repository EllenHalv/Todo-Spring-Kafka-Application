package com.ellencodes.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui implements ActionListener {
    /*private final JLabel todoTextLabel;
    private final JLabel todoStatusLabel;
    private final JTextField todoText;
    private final JTextField todoStatus;
    private final JButton addButton;
    private final JLabel success;*/
    private final JLabel titleLabel;
    private JPanel taskPanel, taskComponentPanel;


    public Gui(JLabel titleLabel/*JLabel todoTextLabel, JLabel todoStatusLabel, JLabel titleLabel, JTextField todoText, JTextField todoStatus, JButton addButton, JLabel success*/) {
        /*this.todoTextLabel = todoTextLabel;
        this.todoStatusLabel = todoStatusLabel;
        this.todoText = todoText;
        this.todoStatus = todoStatus;
        this.addButton = addButton;
        this.success = success;*/
        this.titleLabel = titleLabel;

    }

    public Gui() {
        JPanel panel = new JPanel();

        JFrame frame = new JFrame("Todo List Application");
        frame.setSize(410, 825);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        // add label for title
        titleLabel = new JLabel("Todo List");
        titleLabel.setBounds(150, 0, 80, 25);
        panel.add(titleLabel);

        taskPanel = new JPanel();

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

        // add task button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(10, 750, 100, 25);
        addTaskButton.addActionListener(this);
        panel.add(addTaskButton);

        frame.setVisible(true);

        /*// add label for todotext field
        todoTextLabel = new JLabel("Todo text");
        todoTextLabel.setBounds(10, 40, 80, 25);
        panel.add(todoTextLabel);

        // add text field for todotext
        todoText = new JTextField(20);
        todoText.setBounds(160, 40, 165, 25);
        panel.add(todoText);

        // add label for todostatus field
        todoStatusLabel = new JLabel("Todo is done (Y/N)");
        todoStatusLabel.setBounds(10, 70, 160, 25);
        panel.add(todoStatusLabel);

        //add text field for todostatus
        todoStatus = new JTextField(20);
        todoStatus.setBounds(160, 70, 165, 25);
        panel.add(todoStatus);

        //add button for adding todo
        addButton = new JButton("Add Todo");
        addButton.setBounds(150, 115, 100, 25);
        addButton.addActionListener(this);
        panel.add(addButton);

        //add label for response text (is empty until a value is set)
        success = new JLabel("");
        success.setBounds(115, 85, 300, 25);
        panel.add(success);
        success.setText("");*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Add Task")) {
            // create a task component
            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
            taskComponentPanel.add(taskComponent);

            // make the task field request focus after creation
            taskComponent.getTaskField().requestFocus();
            taskComponent.revalidate();
            taskComponent.repaint();
        }

    }

    // metod som hanterar input fältens inmatningsdata
    // kontrollerar så att data hämtats från varje fält, annars uppmanas användaren skriva på nytt
    /*@Override
    public void actionPerformed(ActionEvent e) {
        String todoTextInput = todoText.getText();
        String todoStatusInput = todoStatus.getText();

        if(todoTextInput.isEmpty() || todoStatusInput.isEmpty()) {
            success.setText("You need to fill in all fields!");
        } else {
            System.out.println(todoTextInput + ", " + todoStatusInput);
            success.setText("Todo added successfully!");
            //todo: en consumer ska skriva ut datan i gui konsollen
            //todo: en consumer ska spara datan i databasen
        }
    }*/
}

