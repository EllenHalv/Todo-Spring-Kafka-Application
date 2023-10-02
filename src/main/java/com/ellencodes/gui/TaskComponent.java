package com.ellencodes.gui;

import com.ellencodes.client.Client;
import com.ellencodes.kafka.payload.Todo;
import lombok.Getter;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class TaskComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JTextPane taskField;
    private final JButton addTaskButton;
    //private final JButton deleteButton;
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

       /* // delete button
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(50, 50));
        deleteButton.addActionListener(this);*/

        // add to this task component
        this.add(checkBox);
        this.add(taskField);
        this.add(addTaskButton);
        //this.add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(checkBox.isSelected()) {
            taskField.setText("<html><body><strike>" + taskField.getText() + "</strike></body></html>");
        } else if (!checkBox.isSelected()){
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
    }

        /*if(e.getActionCommand().equalsIgnoreCase("X")){
            parentPanel.remove(this);
            parentPanel.repaint();
            parentPanel.revalidate();
        }*/
}
