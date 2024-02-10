package ru.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;
    @BeforeEach
    void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void  shouldMaximumTasksInHistory() {
        int countHistoryMaximum = 10;
        for (int i = 0; i <  countHistoryMaximum; i++) {
            Task task = new Task("Тест" + i);
            historyManager.add(task);
        }
        historyManager.add(new Task("Тест"));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(countHistoryMaximum, history.size(), "Неверное количество задач в истории." );
    }
}