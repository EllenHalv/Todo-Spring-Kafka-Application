package com.ellencodes.gui;

import com.ellencodes.gui.swingworker.AddToDatabaseWorker;
import com.ellencodes.gui.swingworker.FetchAllTodosWorker;
import com.ellencodes.kafka.payload.Todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiPanel extends JPanel implements ActionListener {

    public static JPanel todoComponentPanel;

    public GuiPanel() {
        JPanel panel = new JPanel();

        JFrame frame = new JFrame("Todo List Application");
        frame.setSize(510, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Todo List");
        titleLabel.setBounds(150, 0, 80, 25);
        panel.add(titleLabel);

        JPanel todoPanel = new JPanel();

        todoComponentPanel = new JPanel();
        todoComponentPanel.setLayout(new BoxLayout(todoComponentPanel, BoxLayout.Y_AXIS));
        todoPanel.add(todoComponentPanel);

        JScrollPane scrollPane = new JScrollPane(todoPanel);
        scrollPane.setBounds(10, 40, 475, 500);
        scrollPane.setMaximumSize(scrollPane.getPreferredSize());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        JButton newTodoButton = new JButton("New Todo");
        newTodoButton.setBounds(10, 550, 100, 25);
        newTodoButton.addActionListener(this);
        panel.add(newTodoButton);

        JButton loadFromDbButton = new JButton("Load from DB");
        loadFromDbButton.setBounds(320, 550, 150, 25);
        loadFromDbButton.addActionListener(this);
        panel.add(loadFromDbButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("New Todo")) {
            // Show a pop-up dialog for creating a new todo
            showAddTodoDialog();

        }

        if (command.equalsIgnoreCase("Load from DB")) {
            // Disable the button to prevent further clicks
            ((JButton)e.getSource()).setEnabled(false);

            // Fetch all db todos asynchronously
            FetchAllTodosWorker fetchAllTodosWorker = new FetchAllTodosWorker();
            fetchAllTodosWorker.execute();
        }
    }

    public static void createTodoComponent(Todo todo, JPanel todoComponentPanel) {
        TodoComponent todoComponent = new TodoComponent(todoComponentPanel);
        todoComponentPanel.add(todoComponent);

        // make the field request focus after creation
        todoComponent.getTextField().requestFocus();
        todoComponent.revalidate();
        todoComponent.repaint();
        todoComponent.getTextField().setText(todo.getTodoName());
        todoComponent.getIdField().setText(todo.getId().toString());

        todoComponentPanel.add(todoComponent);

        // Refresh the GUI
        todoComponentPanel.revalidate();
        todoComponentPanel.repaint();
    }

    private void showAddTodoDialog() {
        // Create a pop-up dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Todo");
        dialog.setSize(400, 200);
        dialog.setModal(true);

        JPanel dialogPanel = new JPanel();

        JTextField todoNameField = new JTextField();
        todoNameField.setPreferredSize(new Dimension(200, 30));

        JButton addButton = new JButton("Add Todo");
        addButton.setPreferredSize(new Dimension(100, 30));

        dialogPanel.add(new JLabel("Todo Name:"));
        dialogPanel.add(todoNameField);
        dialogPanel.add(new JLabel());
        dialogPanel.add(addButton);

        dialog.add(dialogPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String todoName = todoNameField.getText();

                Todo todo = new Todo();
                todo.setTodoName(todoName);

                // Add to the database
                AddToDatabaseWorker addToDatabaseWorker = new AddToDatabaseWorker(todo);
                addToDatabaseWorker.execute();

                // Close the dialog
                dialog.dispose();
            }
        });

        // Display the dialog
        dialog.setVisible(true);
    }
}

