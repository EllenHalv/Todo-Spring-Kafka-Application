package com.ellencodes;

import com.ellencodes.gui.Gui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.net.MalformedURLException;

@SpringBootApplication

public class KafkaTodoApplication {

    public static void main(String[] args) throws MalformedURLException {
        System.setProperty("gui.enabled", "true");

        // Start the GUI if the system property is set
        if (System.getProperty("gui.enabled", "false").equalsIgnoreCase("true")) {
            Gui.main(args);
        }

        // Start the Spring Boot application
        SpringApplication.run(KafkaTodoApplication.class, args);

        System.out.println(
                "------------------------------------" +
                "\n  Welcome to the Todo Application!  " +
                "\n------------------------------------");
    }
}
