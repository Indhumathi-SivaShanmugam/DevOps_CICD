package com.demo.tasks.service;

import com.demo.tasks.model.Task;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Task create(String title, String description) {
        long id = sequence.incrementAndGet();
        Task task = new Task(id, title.trim(), description, false, Instant.now());
        tasks.put(id, task);
        return task;
    }

    public List<Task> findAll() {
        return tasks.values().stream()
                .sorted(Comparator.comparingLong(Task::id))
                .toList();
    }

    public Task get(long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    public Task complete(long id) {
        return tasks.computeIfPresent(id, (key, task) -> task.markCompleted());
    }

    public void delete(long id) {
        if (tasks.remove(id) == null) {
            throw new TaskNotFoundException(id);
        }
    }

    public Map<String, Long> stats() {
        long total = tasks.size();
        long done = tasks.values().stream().filter(Task::completed).count();
        return Map.of("total", total, "completed", done, "pending", total - done);
    }
}
