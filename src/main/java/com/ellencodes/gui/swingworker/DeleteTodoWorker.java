package com.ellencodes.gui.swingworker;

import com.ellencodes.appservice.AppService;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class DeleteTodoWorker extends SwingWorker<Long, Void> {
    private final Long id;

    public DeleteTodoWorker(Long id) {
        this.id = id;
    }

    @Override
    protected Long doInBackground() throws Exception {
        AppService.deleteTodoById(id);
        return null;
    }

    @Override
    protected void done() {
        WaitForRemovalWorker worker = new WaitForRemovalWorker(id);
        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    Long id = worker.get();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
