package com.ellencodes.gui.swingworker;

import com.ellencodes.client.Client;
import com.ellencodes.gui.TaskComponent;
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
        jsonObject.put("taskName", todo.getTaskName());
        Client.sendToWebAPI(jsonObject);
        System.out.println("Data sent from gui panel");
        return null;
    }

    @Override
    protected void done() {
        CompletableFuture<Long> idFuture = TaskComponent.waitForIdToBeSet();
        idFuture.thenAccept(id -> {
            System.out.println("ID fetched before adding to gui list: " + id);

            SwingUtilities.invokeLater(() -> {
                FetchTodosWorker fetchTodosWorker = new FetchTodosWorker();
                fetchTodosWorker.execute();
            });
        });
    }
}
