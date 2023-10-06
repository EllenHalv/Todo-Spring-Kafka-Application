package com.ellencodes.gui;

import com.ellencodes.client.Client;
import com.ellencodes.gui.swingworker.WaitForRemovalWorker;
import com.ellencodes.kafka.payload.Todo;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
public class TaskComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JTextPane taskField;
    private final JButton deleteTaskButton;
    private final JPanel taskComponentPanel;
    private final JTextField idField;

    public TaskComponent(JPanel taskComponentPanel) {
        this.taskComponentPanel = taskComponentPanel;

        // task field
        taskField = new JTextPane();
        taskField.setPreferredSize(new Dimension(200, 40));
        taskField.setContentType("text/html");

        // checkbox
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(new Dimension(50, 50));
        checkBox.addActionListener(this);

        // delete task button
        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.setBounds(200, 750, 100, 25);
        deleteTaskButton.addActionListener(this);

        // hidden id field
        idField = new JTextField();
        idField.setBounds(300, 750, 100, 25);
        idField.setVisible(true);



        // add to this task component
        this.add(checkBox);
        this.add(taskField);
        this.add(deleteTaskButton);
        this.add(idField);
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

        // Action listener for remove task button
        if (e.getActionCommand().equalsIgnoreCase("Delete Task")) {
            String idText = idField.getText();
            Long taskId;

            if (!idText.isEmpty()) {
                taskId = Long.valueOf(idText);
                System.out.println("ID fetched from the hidden ID field: " + taskId);
            } else {
                taskId = null;
                System.err.println("ID is null or not available yet.");
            }

            if (taskId != null) {
                CompletableFuture<Void> deleteTodoById = CompletableFuture.runAsync(() -> {
                    Client.deleteTodoById(taskId);
                    System.out.println("sent id to delete from gui:" + taskId);
                });

                deleteTodoById.thenRunAsync(() -> {
                    WaitForRemovalWorker worker = new WaitForRemovalWorker(taskId);
                    worker.addPropertyChangeListener(evt -> {
                        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                            try {
                                Long id = worker.get();
                                System.out.println("ID fetched to delete: " + id);
                            } catch (InterruptedException | ExecutionException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    taskComponentPanel.remove(this);
                    taskComponentPanel.revalidate();
                    taskComponentPanel.repaint();

                    worker.execute();
                });
            }
        }
    }

    public static CompletableFuture<Long> waitForRemovalInDatabase(Long id) {
        CompletableFuture<Long> idFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            while (true) {
                Long currentId = Client.getCurrentTodoId();
                if (currentId != null) {
                    if (currentId == id) {
                        idFuture.complete(id);
                        System.out.println("ID fetched from the database: " + id);
                        break;
                    }
                }
                try {
                    Thread.sleep(100); // Adjust sleep duration as needed
                } catch (InterruptedException ignored) {
                    System.out.println("InterruptedException: " + ignored.getMessage());
                }
            }
        });

        return idFuture;
    }

    public static CompletableFuture<Long> waitForIdToBeSet() {
        CompletableFuture<Long> idFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            while (true) {
                Long id = Client.getCurrentTodoId();
                if (id != null) {
                    idFuture.complete(id);
                    System.out.println("ID fetched from the database: " + id);
                    break;
                }
                try {
                    Thread.sleep(100); // Adjust sleep duration as needed
                } catch (InterruptedException ignored) {
                    System.out.println("InterruptedException: " + ignored.getMessage());
                }
            }
        });

        return idFuture;
    }

    public static CompletableFuture<ArrayList<Todo>> waitForListToBeLoaded() {
        CompletableFuture<ArrayList<Todo>> listFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            while (true) {
                ArrayList<Todo> todos = Client.getTodos();
                if (todos != null) {
                    listFuture.complete(todos);
                    System.out.println("List fetched from the database: " + todos);
                    break;
                }
                try {
                    Thread.sleep(100); // Adjust sleep duration as needed
                } catch (InterruptedException ignored) {
                    System.out.println("InterruptedException: " + ignored.getMessage());
                }
            }
        });

        return listFuture;
    }
}
