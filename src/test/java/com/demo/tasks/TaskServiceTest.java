package com.demo.tasks;

import com.demo.tasks.model.Task;
import com.demo.tasks.service.TaskNotFoundException;
import com.demo.tasks.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService();
    }

    @Test
    void createAssignsIncrementingIds() {
        Task first = service.create("one", null);
        Task second = service.create("two", null);
        assertEquals(1, first.id());
        assertEquals(2, second.id());
        assertFalse(first.completed());
    }

    @Test
    void completeMarksTaskDone() {
        Task task = service.create("write report", "CIA 2");
        assertTrue(service.complete(task.id()).completed());
    }

    @Test
    void getUnknownTaskThrows() {
        assertThrows(TaskNotFoundException.class, () -> service.get(99));
    }

    @Test
    void deleteRemovesTask() {
        Task task = service.create("temp", null);
        service.delete(task.id());
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void statsCountsCorrectly() {
        Task a = service.create("a", null);
        service.create("b", null);
        service.complete(a.id());
        assertEquals(2L, service.stats().get("total"));
        assertEquals(1L, service.stats().get("completed"));
        assertEquals(1L, service.stats().get("pending"));
    }
}
