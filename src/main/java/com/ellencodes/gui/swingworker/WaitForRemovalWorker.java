package com.ellencodes.gui.swingworker;

import com.ellencodes.client.Client;

import javax.swing.*;

public class WaitForRemovalWorker extends SwingWorker<Long, Void> {
    private final Long id;

    public WaitForRemovalWorker(Long id) {
        this.id = id;
    }

    @Override
    protected Long doInBackground() throws Exception {
        while (true) {
            Long currentId = Client.getCurrentTodoId();
            if (currentId != null && currentId.equals(id)) {
                return id;
            }
            Thread.sleep(100); // Adjust sleep duration as needed
        }
    }
}
