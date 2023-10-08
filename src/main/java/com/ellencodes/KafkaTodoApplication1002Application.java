package com.ellencodes;

import com.ellencodes.client.AppService;
import com.ellencodes.gui.Gui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.net.MalformedURLException;

@SpringBootApplication

public class KafkaTodoApplication1002Application {

    public static void main(String[] args) throws MalformedURLException {
        System.setProperty("gui.enabled", "true");

        // Start the GUI if the system property is set
        if (System.getProperty("gui.enabled", "false").equalsIgnoreCase("true")) {
            Gui.main(args);
        }

        // Start the Spring Boot application
        SpringApplication.run(KafkaTodoApplication1002Application.class, args);

        // Start the client menu
        boolean shouldExit = AppService.userMenu(); // start the client menu and get the exit status

        // Keep running until user enters "0"
        while (!shouldExit) {
            shouldExit = AppService.userMenu();
        }

        // If shouldExit is true, stop the application
        System.out.println("Exiting the application.");
        System.exit(0);    }

}
