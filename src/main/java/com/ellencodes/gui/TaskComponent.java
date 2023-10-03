package com.ellencodes.gui;

import com.ellencodes.client.Client;
import com.ellencodes.kafka.payload.Todo;
import com.ellencodes.kafka.repository.TodoRepository;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Getter
public class TaskComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JTextPane taskField;
    private final JButton addTaskButton;
    private final JButton deleteTaskButton;
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

        // add task button
        addTaskButton = new JButton("Add Task");
        addTaskButton.setBounds(100, 750, 100, 25);
        addTaskButton.addActionListener(this);

        // delete task button
        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setBounds(200, 750, 100, 25);
        deleteTaskButton.addActionListener(this);

        // add to this task component
        this.add(checkBox);
        this.add(taskField);
        this.add(addTaskButton);
        this.add(deleteTaskButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Action listener for the checkbox
        if (checkBox.isSelected()) {
            // Add strike through text
            taskField.setText("<html><body><strike>" + taskField.getText() + "</strike></body></html>");
        } else if (!checkBox.isSelected()) {
            // Remove strike through text
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            taskField.setText(taskText);
        }

        // Action listener for the "Add Task" button
        if (e.getActionCommand().equalsIgnoreCase("Add Task")) {
            // Get the user's input from GUI components
            String htmlTaskName = taskField.getText();

            // Parse the HTML using apache commons
            String taskName = ConvertHtmlToString.convertHtmlToPlainText(htmlTaskName);

            // Create a new Todo object
            Todo todo = new Todo();
            todo.setTaskName(taskName);

            // Create a JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("taskName", todo.getTaskName());

            // Use the TaskService to save the task to the database
            Client.sendToWebAPI(jsonObject);
        }

        // Action listener for remove task button
        if (checkBox.isSelected() && e.getActionCommand().equalsIgnoreCase("Delete Task")) {
            // Get the user's input from GUI components
            String htmlTaskName = taskField.getText();

            //remove strike through text
            String taskText = htmlTaskName.replaceAll("<[^>]*>", "");

            // Parse the HTML using apache commons
            String taskName = ConvertHtmlToString.convertHtmlToPlainText(taskText);

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

                        // Fetch the task from the database
                        Todo todo = Client.getTodoByName(taskName);

                        // Remove the task from the database
                        if (todo != null) {
                            Client.deleteTodoById(todo.getId());
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

            // Remove the task component from the parent panel
            parentPanel.remove(this);
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }
}
