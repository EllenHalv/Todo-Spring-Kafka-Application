package com.ellencodes.kafka.payload;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "todos")
@Getter
@Setter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String todoName;
    private boolean done = false;

    public Todo(String todoName) {
        this.todoName = todoName;

    }

    public Todo() {}

    @Override
    public String toString() {
        return "\n-- Todo --" + "\nid: " + id +
                "\ntodoName: '" + todoName + '\'' +
                "\ndone: " + done;
    }
}