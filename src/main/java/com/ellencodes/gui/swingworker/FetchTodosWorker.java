package com.ellencodes.gui.swingworker;

import com.ellencodes.client.AppService;
import com.ellencodes.gui.GuiPanel;
import com.ellencodes.gui.TaskComponent;
import com.ellencodes.kafka.payload.Todo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.ellencodes.gui.GuiPanel.taskComponentPanel;

public class FetchTodosWorker extends SwingWorker<ArrayList<Todo>, Void> {
    @Override
    protected ArrayList<Todo> doInBackground() throws Exception {
        AppService.getAllDbTodos();
        return null;
    }

    @Override
    protected void done() {
        CompletableFuture<ArrayList<Todo>> todosFuture = TaskComponent.waitForListToBeLoaded();
        todosFuture.thenAccept(todos -> {
            if (todos != null) {
                for (Todo todoObject : todos) {
                    SwingUtilities.invokeLater(() -> {
                        for (Component component : taskComponentPanel.getComponents()) {
                            if (component instanceof TaskComponent) {
                                TaskComponent taskComponent = (TaskComponent) component;
                                if (taskComponent.getIdField().getText().equals(todoObject.getId().toString())) {
                                    return;
                                }
                            }
                        }
                        GuiPanel.createTaskComponent(todoObject, taskComponentPanel);
                    });
                }
            } else {
                System.err.println("Todos list is not available.");
            }
        });
    }
}
