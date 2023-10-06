package com.ellencodes.gui;

import com.ellencodes.client.Client;
import com.ellencodes.gui.swingworker.AddToDatabaseWorker;
import com.ellencodes.kafka.payload.Todo;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GuiPanel extends JPanel implements ActionListener {

    public static JPanel taskComponentPanel;

    public GuiPanel() {
        JPanel panel = new JPanel();

        JFrame frame = new JFrame("Todo List Application");
        frame.setSize(610, 650);
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
        scrollPane.setBounds(10, 40, 575, 500); //TODO sätt tillbaka till 475
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

    @Override
    public void actionPerformed(ActionEvent e) {

        //action listener for the new task button
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("New Task")) {
            // Show a pop-up dialog for creating a new task
            showAddTodoDialog();

        }

        //action listener for the load from db button
        if (command.equalsIgnoreCase("Load from DB")) {
            // Disable the button to prevent further clicks
            ((JButton)e.getSource()).setEnabled(false);

            // Fetch all db todos asynchronously
            CompletableFuture<Void> fetchTodosFuture = CompletableFuture.runAsync(() -> {
                Client.getAllDbTodos();
            });

            // Wait for completion then fetch the list of todos
            fetchTodosFuture.thenRunAsync(() -> {
                CompletableFuture<ArrayList<Todo>> todosFuture = TaskComponent.waitForListToBeLoaded();
                todosFuture.thenAccept(todos -> {
                    // Handle the fetched list here
                    if (todos != null) {
                        // Skapa och lägg till en TaskComponent för varje todo
                        for (Todo todoObject : todos) {
                            // create a task component
                            SwingUtilities.invokeLater(() -> {
                                //kolla om todo med id från listan redan finns i gui
                                for (Component component : taskComponentPanel.getComponents()) {
                                    if (component instanceof TaskComponent) {
                                        TaskComponent taskComponent = (TaskComponent) component;
                                        if (taskComponent.getIdField().getText().equals(todoObject.getId().toString())) {
                                            return;
                                        }
                                    }
                                }

                                // create a task component
                                createTaskComponent(todoObject, taskComponentPanel);
                            });

                        }
                    } else {
                        // Handle the case where the todos list is not yet available
                        System.err.println("Todos list is not available.");
                    }
                });
            });
        }
    }

    public static void createTaskComponent(Todo todo, JPanel taskComponentPanel) {
        TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
        taskComponentPanel.add(taskComponent);

        // make the task field request focus after creation
        taskComponent.getTaskField().requestFocus();
        taskComponent.revalidate();
        taskComponent.repaint();
        taskComponent.getTaskField().setText(todo.getTaskName());
        taskComponent.getIdField().setText(todo.getId().toString());

        taskComponentPanel.add(taskComponent);

        // Refresh the GUI
        taskComponentPanel.revalidate();
        taskComponentPanel.repaint();
    }

    private void showAddTodoDialog() {
        // Create a pop-up dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Todo");
        dialog.setSize(400, 200);
        dialog.setModal(true);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(2, 2));

        JTextField taskNameField = new JTextField();
        JButton addButton = new JButton("Add Todo");

        dialogPanel.add(new JLabel("Task Name:"));
        dialogPanel.add(taskNameField);
        dialogPanel.add(new JLabel());
        dialogPanel.add(addButton);

        dialog.add(dialogPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = taskNameField.getText();
                Todo todo = new Todo();
                todo.setTaskName(taskName);

                // Add the todo to the database
                AddToDatabaseWorker addToDatabaseWorker = new AddToDatabaseWorker(todo);
                addToDatabaseWorker.execute();

                // Close the dialog
                dialog.dispose();
            }
        });

        // Display the dialog
        dialog.setVisible(true);
    }

    private void addToDatabase(Todo todo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskName", todo.getTaskName());

        // Send the JSON object to the web API asynchronously
        CompletableFuture<Void> sendToWebAPI = CompletableFuture.runAsync(() -> {
            Client.sendToWebAPI(jsonObject);
            System.out.println("Data sent from gui panel");
        });

        // Wait for the sendToWebAPI operation to complete, then fetch the ID
        sendToWebAPI.thenRunAsync(() -> {
            CompletableFuture<Long> idFuture = TaskComponent.waitForIdToBeSet();
            idFuture.thenAccept(id -> {
                // Handle the fetched ID here
                System.out.println("ID fetched before adding to gui list: " + id);

                // Update your GUI or perform other tasks with the ID
                SwingUtilities.invokeLater(() -> {
                    // Fetch all db todos asynchronously
                    CompletableFuture<Void> fetchTodosFuture = CompletableFuture.runAsync(() -> {
                        Client.getAllDbTodos();
                    });

                    // Wait for completion then fetch the list of todos
                    fetchTodosFuture.thenRunAsync(() -> {
                        CompletableFuture<ArrayList<Todo>> todosFuture = TaskComponent.waitForListToBeLoaded();
                        todosFuture.thenAccept(todos -> {
                            // Handle the fetched list here
                            if (todos != null) {
                                // Skapa och lägg till en TaskComponent för varje todo
                                for (Todo todoObject : todos) {
                                    // create a task component
                                    SwingUtilities.invokeLater(() -> {
                                        //kolla om todo med id från listan redan finns i gui
                                        for (Component component : taskComponentPanel.getComponents()) {
                                            if (component instanceof TaskComponent) {
                                                TaskComponent taskComponent = (TaskComponent) component;
                                                if (taskComponent.getIdField().getText().equals(todoObject.getId().toString())) {
                                                    return;
                                                }
                                            }
                                        }

                                        // create a task component
                                        createTaskComponent(todoObject, taskComponentPanel);
                                    });

                                }
                            } else {
                                // Handle the case where the todos list is not yet available
                                System.err.println("Todos list is not available.");
                            }
                        });
                    });
                });
            });
        });
    }
}

