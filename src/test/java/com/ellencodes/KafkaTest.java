package com.ellencodes;

import com.ellencodes.kafka.payload.Todo;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.ellencodes.appservice.AppService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import java.util.ArrayList;

@SpringBootTest(classes = KafkaTodoApplication1002Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KafkaTest {

    private static Todo todo;
    private static JSONObject jsonObject;

    @BeforeAll
    static void beforeAll() throws JSONException {
        todo = new Todo();
        todo.setTodoName("Make the bed");
        todo.setDone(false);
        todo.setId(1L);

        jsonObject = new JSONObject();
        jsonObject.put("taskName", todo.getTodoName());
        jsonObject.put("doneStatus", todo.isDone());
        jsonObject.put("id", todo.getId());
    }

    @Test
    @Order(1)
    public void sendToWebAPITest() throws JSONException {
        //bygg upp jsonobjekt
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskName", "Make the bed");

        //anropa metod för att skicka till webapi
        String actual = sendToWebAPI(jsonObject);

        String expected = "sent publish message";

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    public void getAllDbTodosTest() {
        //anropa metod för att hämta todos
        String actual = getAllDbTodos();

        String expected = "sent get all message";

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @Order(3)
    public void deleteTodoByIdTest() {
        //anropa metod för att ta bort todo
        String actual = deleteTodoById(1L);

        String expected = "sent delete message";

        //assert
        assertEquals(expected, actual);
    }
}

