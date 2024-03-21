package ru.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;
    @BeforeEach
    void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldOnceInHistory() {
        Task task = new Task("Тест");
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        int count = 0;
        for (Task taskInHistory : history) {
            if (task.equals(taskInHistory)) {
                count++;
            }
        }
        assertEquals(1, count, "Задача встречается в истории не верное количество раз.");
    }

    @Test
    void remove() {
        Task task = new Task("Тест");
        historyManager.add(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task), "Задача не удалилась");
    }
}