package com.ellencodes.gui;

import com.ellencodes.client.Client;
import com.ellencodes.kafka.payload.Todo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GuiPanel extends JPanel implements ActionListener {

    private final JPanel taskComponentPanel;

    public GuiPanel() {
        JPanel panel = new JPanel();

        JFrame frame = new JFrame("Todo List Application");
        frame.setSize(510, 650);
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
        scrollPane.setBounds(10, 40, 475, 500);
        scrollPane.setMaximumSize(scrollPane.getPreferredSize());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        // new task button
        JButton newTaskButton = new JButton("New Task");
        newTaskButton.setBounds(10, 550, 100, 25);
        newTaskButton.addActionListener(this);
        panel.add(newTaskButton);

        //load from database button
        JButton loadFromDbButton = new JButton("Load from DB");
        loadFromDbButton.setBounds(320, 550, 150, 25);
        loadFromDbButton.addActionListener(this);
        panel.add(loadFromDbButton);

        frame.setVisible(true);
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

        //action listener for the load from db button
        if (command.equalsIgnoreCase("Load from DB")) {
            // Fetch all db todos asynchronously
            CompletableFuture<Void> fetchTodosFuture = CompletableFuture.runAsync(() -> {
                Client.getAllDbTodos();
            });

            fetchTodosFuture.thenRunAsync(() -> {
                // Wait until the todos list is populated, with a timeout if needed
                try {
                    CompletableFuture<Void> waitFuture = CompletableFuture.runAsync(() -> {
                        while (Client.getTodos() == null) {
                            try {
                                Thread.sleep(100); // Adjust sleep duration as needed
                            } catch (InterruptedException ignored) {
                                // Handle the exception as needed
                            }
                        }
                    });
                    try {
                        waitFuture.get(10, TimeUnit.SECONDS); // Adjust the timeout as needed
                    } catch (TimeoutException ex) {
                        throw new RuntimeException(ex);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Ensure that the todos list is populated before proceeding
                    if (Client.getTodos() != null) {

                        ArrayList<Todo> allTodos = Client.getTodos();

                        // Skapa och lägg till en TaskComponent för varje todo
                        for (Todo todo : allTodos) {
                            // create a task component
                            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                            taskComponentPanel.add(taskComponent);

                            // make the task field request focus after creation
                            taskComponent.getTaskField().requestFocus();
                            taskComponent.revalidate();
                            taskComponent.repaint();
                            taskComponent.getTaskField().setText(todo.getTaskName());
                            if (todo.isDone()) {
                                taskComponent.getCheckBox().setSelected(true);
                                taskComponent.getTaskField().setText("<html><body><strike>" + taskComponent.getTaskField().getText() + "</strike></body></html>");
                            }
                        }
                    } else {
                        // Handle the case where the todos list is not yet available
                        System.err.println("Todos list is not available.");
                    }
                } catch (RuntimeException ex) {
                    // Handle the exception as needed
                    System.err.println("RuntimeException: " + ex.getMessage());
                }
            });
        }
    }

    //todo: en consumer ska skriva ut datan i gui konsollen
    //todo: en consumer ska spara datan i databasen

}

