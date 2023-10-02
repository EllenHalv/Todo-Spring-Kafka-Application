package com.ellencodes;

import com.ellencodes.client.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.net.MalformedURLException;

@SpringBootApplication

public class KafkaTodoApplication1002Application {

    public static void main(String[] args) throws MalformedURLException {
        SpringApplication.run(KafkaTodoApplication1002Application.class, args);

        boolean shouldExit = Client.userMenu(); // start the client menu and get the exit status

        // Keep running until user enters "0"
        while (!shouldExit) {
            shouldExit = Client.userMenu();
        }

        // If shouldExit is true, stop the application
        System.out.println("Exiting the application.");
        System.exit(0);    }

}
