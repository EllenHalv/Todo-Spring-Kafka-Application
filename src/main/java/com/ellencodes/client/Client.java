package com.ellencodes.client;

import com.ellencodes.kafka.payload.Todo;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    private static ArrayList<Todo> todos;

    public static void main(String[] args) throws MalformedURLException {

        SpringApplication.run(Client.class, args);

        userMenu();
    }

    public static boolean userMenu() throws MalformedURLException {
        String userChoise = "";

        do {

            printMenu();

            Scanner scan = new Scanner(System.in);
            System.out.print("Gör ditt val: ");
            userChoise = scan.nextLine();

            switch (userChoise) {
                case "1": {
                    userInputForKafka();
                    break;
                }
                case "2": {
                    getDataFromKafka("ellencodesJson");
                    break;
                }
                case "0": {
                    break;
                }
                default: {
                    System.out.println("Felaktig input. Försök igen");
                    break;
                }
            }

            if (!userChoise.equals("0")) {
                System.out.println("Press any key to continue");
                scan.nextLine();
            }

        } while (!userChoise.equals("0"));
        return true;
    }

    public static void printMenu() {
        System.out.println("Gör dit val!");
        System.out.println("------------");
        System.out.println("1. Skriv data till Kafka Server");
        System.out.println("2. Hämta data från Kafka Server");
        System.out.println("0. Avsluta");
    }

    public static void userInputForKafka() throws MalformedURLException {
        Todo todo = new Todo();

        //Låt användaren mata in egen data
        Scanner scan = new Scanner(System.in);
        System.out.print("Skriv in en todo-uppgift: ");
        String taskName = scan.nextLine();

        todo.setTaskName(taskName);

        JSONObject myObj = new JSONObject();
        myObj.put("taskName", todo.getTaskName());

        //Skicka Payload till WebAPI via en Request
        sendToWebAPI(myObj);
    }

    public static String sendToWebAPI(JSONObject myObj) {
        String returnResp = "";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/messages/publish");

            // Skapa en JSON-förfrågningskropp
            String jsonPayload = myObj.toJSONString();
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Skicka förfrågan och hantera svaret
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    System.out.println("Svar från server: " + responseString);
                    returnResp = responseString;
                }
            } catch (ParseException e) {
                System.err.println("ParseException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return returnResp;
    }

    public static ArrayList<Todo> getDataFromKafka(String topicName) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "fetchingGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put("spring.json.trusted.packages", "*");

        Consumer<String, Todo> consumer = new KafkaConsumer<>(props);
        consumer.assign(Collections.singletonList(new TopicPartition(topicName, 0)));

        //Gå till början av Topic
        consumer.seekToBeginning(consumer.assignment());

        //Create User list
        ArrayList<Todo> todos = new ArrayList<>();

        //WhileLoop som hämtar i JSON format
        while (true) {
            ConsumerRecords<String, Todo> records = consumer.poll(Duration.ofMillis(100));
            if (records.isEmpty()) continue;
            for (ConsumerRecord<String, Todo> record : records) {
                todos.add(record.value());
            }
            break;
        }

        for (Todo todo : todos) {
            System.out.println(todo.toString());
        }
        return todos;
    }

    public static void deleteTodoById(Long id) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete("http://localhost:8080/api/v1/messages/delete");

            // Skapa en JSON-förfrågningskropp
            String jsonPayload = id.toString();
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpDelete.setEntity(entity);

            // Skicka förfrågan och hantera svaret
            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    System.out.println("Svar från server: " + responseString);
                }
            } catch (ParseException e) {
                System.err.println("ParseException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void getAllDbTodos() {
        //hämta alla todos från databasen
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://localhost:8080/api/v1/messages/get");

            // Skicka förfrågan och hantera svaret
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    System.out.println("Svar från server: " + responseString);
                }
            } catch (ParseException e) {
                System.err.println("ParseException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void setTodos(ArrayList<Todo> dbTodos) {
        Client.todos = dbTodos;
    }

    public static ArrayList<Todo> getTodos() {
        return todos;
    }

    public static Todo getTodoByName(String taskName) {
        //hämta en todo baserat på namn
        try {
            //loopa igenom listan
            for (Todo todo : todos) {
                //om taskName finns i listan
                if (todo.getTaskName().equals(taskName)) {
                    //returnera tasken
                    return todo;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
