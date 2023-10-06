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
    private String taskName;
    private boolean done = false;

    public Todo(String taskName) {
        this.taskName = taskName;

    }

    public Todo() {}

    @Override
    public String toString() {
        return "\n-- Todo --" + "\nid: " + id +
                "\ntaskName: '" + taskName + '\'' +
                "\ndone: " + done;
    }
}