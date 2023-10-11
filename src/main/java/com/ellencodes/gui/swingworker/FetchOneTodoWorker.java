package com.ellencodes.gui.swingworker;

import com.ellencodes.appservice.AppService;
import com.ellencodes.gui.GuiPanel;
import com.ellencodes.gui.TodoComponent;
import com.ellencodes.kafka.payload.Todo;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

import static com.ellencodes.gui.GuiPanel.todoComponentPanel;

public class FetchOneTodoWorker extends SwingWorker<Void, Void> {

    Todo todo;
    public FetchOneTodoWorker(Todo todo) {
        this.todo = todo;
    }
    @Override
    protected Void doInBackground() throws Exception {
        AppService.getTodoById(todo.getId());
        return null;
    }

    @Override
    protected void done() {
        CompletableFuture<Long> idFuture = TodoComponent.waitForIdToBeSet();
        idFuture.thenAccept(id -> {
            if (id != null) {
                todo.setId(id);
                GuiPanel.createTodoComponent(todo, todoComponentPanel);
            } else {
                System.err.println("Todo is not available.");
            }
        });
    }
}
