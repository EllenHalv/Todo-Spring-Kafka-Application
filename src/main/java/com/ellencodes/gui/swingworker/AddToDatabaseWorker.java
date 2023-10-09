package com.ellencodes.gui.swingworker;

import com.ellencodes.appservice.AppService;
import com.ellencodes.gui.TodoComponent;
import com.ellencodes.kafka.payload.Todo;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

public class AddToDatabaseWorker extends SwingWorker<Void, Void> {
    Todo todo;

    public AddToDatabaseWorker(Todo todo) {
        this.todo = todo;
    }

    @Override
    protected Void doInBackground() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todoName", todo.getTodoName());
        AppService.sendToWebAPI(jsonObject);
        return null;
    }

    @Override
    protected void done() {
        CompletableFuture<Long> idFuture = TodoComponent.waitForIdToBeSet();
        idFuture.thenAccept(id -> {

            SwingUtilities.invokeLater(() -> {
                FetchTodosWorker fetchTodosWorker = new FetchTodosWorker();
                fetchTodosWorker.execute();
            });
        });
    }
}
