package com.ellencodes.gui;

import com.ellencodes.appservice.AppService;
import com.ellencodes.gui.swingworker.DeleteTodoWorker;
import com.ellencodes.kafka.payload.Todo;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a single todo component.
 * This class uses SwingWorker to perform database operations in the background.
 */

@Getter
public class TodoComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JTextPane textField;
    private final JButton deleteTodoButton;
    private final JPanel todoComponentPanel;
    private final JTextField idField;

    public TodoComponent(JPanel todoComponentPanel) {
        this.todoComponentPanel = todoComponentPanel;

        textField = new JTextPane();
        textField.setPreferredSize(new Dimension(200, 40));
        textField.setContentType("text/html");

        checkBox = new JCheckBox();
        checkBox.setPreferredSize(new Dimension(50, 50));
        checkBox.addActionListener(this);

        deleteTodoButton = new JButton("Delete Todo");
        deleteTodoButton.setBounds(200, 750, 100, 25);
        deleteTodoButton.addActionListener(this);

        // hidden id field
        idField = new JTextField();
        idField.setBounds(300, 750, 100, 25);
        idField.setVisible(false);

        // add all to this component
        this.add(checkBox);
        this.add(textField);
        this.add(deleteTodoButton);
        this.add(idField);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkBox.isSelected()) {
            // Add strike through text
            textField.setText("<html><body><strike>" + textField.getText() + "</strike></body></html>");
        } else if (!checkBox.isSelected()) {
            // Remove strike through text
            String todoText = textField.getText().replaceAll("<[^>]*>", "");
            textField.setText(todoText);
        }

        if (e.getActionCommand().equalsIgnoreCase("Delete Todo")) {
            String idText = idField.getText();

            try {
                if (!idText.isEmpty()) {
                    Long todoId = Long.valueOf(idText);
                    DeleteTodoWorker deleteTodoWorker = new DeleteTodoWorker(todoId);
                    deleteTodoWorker.execute();

                    // Remove the component from the UI
                    todoComponentPanel.remove(this);
                    todoComponentPanel.revalidate();
                    todoComponentPanel.repaint();
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Could not delete todo because ID was not found.");
            }
        }
    }

    public static CompletableFuture<Long> waitForIdToBeSet() {
        CompletableFuture<Long> idFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            while (true) {
                Long id = AppService.getCurrentTodoId();
                if (id != null) {
                    idFuture.complete(id);
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
                ArrayList<Todo> todos = AppService.getTodos();
                if (todos != null) {
                    listFuture.complete(todos);
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
