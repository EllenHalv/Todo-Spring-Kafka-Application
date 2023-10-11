package com.ellencodes.appservice;

import com.ellencodes.kafka.payload.Todo;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class AppService {

    @Getter
    private static ArrayList<Todo> todos;
    @Getter
    private static Long currentTodoId;

    public static void main(String[] args) {}

    public static String sendToWebAPI(JSONObject myObj) {
        String responseString;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/messages/publish");

            // Skapa en JSON-förfrågningskropp
            String jsonPayload = myObj.toJSONString();
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Skicka förfrågan och hantera svaret
            responseString = sendRequest(httpClient, httpPost);

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return responseString;
    }

    public static String deleteTodoById(Long id) {
        String responseString;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete("http://localhost:8080/api/v1/messages/delete");

            String jsonPayload = id.toString();
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpDelete.setEntity(entity);

            responseString = sendRequest(httpClient, httpDelete);

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return responseString;
    }

    public static String getAllDbTodos() {
        String responseString;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://localhost:8080/api/v1/messages/get");

            responseString = sendRequest(httpClient, httpGet);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return responseString;
    }

    private static String sendRequest(CloseableHttpClient httpClient, HttpUriRequestBase httpRequest) throws IOException {
        String responseString = null;
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
        } catch (ParseException e) {
            System.err.println("ParseException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return responseString;
    }

    public static String getTodoById(Long id) {
        String responseString;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://localhost:8080/api/v1/messages/get/" + id.toString());

            responseString = sendRequest(httpClient, httpGet);
        } catch (NullPointerException | IOException e) {
            System.err.println("NullPointerException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return responseString;
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

        ArrayList<Todo> todos = new ArrayList<>();

        //hämta i JSON format
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

    public static void setCurrentTodoId(Long todoId) {
        currentTodoId = todoId;
    }

    public static void setTodos(ArrayList<Todo> dbTodos) {
        AppService.todos = dbTodos;
    }
}
