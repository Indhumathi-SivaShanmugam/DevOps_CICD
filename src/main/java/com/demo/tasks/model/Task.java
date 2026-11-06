package com.demo.tasks.model;

import java.time.Instant;

public record Task(long id, String title, String description, boolean completed, Instant createdAt) {
    public Task markCompleted() {
        return new Task(id, title, description, true, createdAt);
    }
}
