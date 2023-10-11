package com.ellencodes.gui.swingworker;

import com.ellencodes.appservice.AppService;
import com.ellencodes.gui.GuiPanel;
import com.ellencodes.gui.TodoComponent;
import com.ellencodes.kafka.payload.Todo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.ellencodes.gui.GuiPanel.todoComponentPanel;

public class FetchAllTodosWorker extends SwingWorker<ArrayList<Todo>, Void> {
    @Override
    protected ArrayList<Todo> doInBackground() throws Exception {
        AppService.getAllDbTodos();
        return null;
    }

    @Override
    protected void done() {
        CompletableFuture<ArrayList<Todo>> todosFuture = TodoComponent.waitForListToBeLoaded();
        todosFuture.thenAccept(todos -> {
            if (todos != null) {
                for (Todo todoObject : todos) {
                    SwingUtilities.invokeLater(() -> {
                        for (Component component : todoComponentPanel.getComponents()) {
                            if (component instanceof TodoComponent) {
                                TodoComponent todoComponent = (TodoComponent) component;
                                if (todoComponent.getIdField().getText().equals(todoObject.getId().toString())) {
                                    return;
                                }
                            }
                        }
                        GuiPanel.createTodoComponent(todoObject, todoComponentPanel);
                    });
                }
            } else {
                System.err.println("Todos list is not available.");
            }
        });
    }
}
