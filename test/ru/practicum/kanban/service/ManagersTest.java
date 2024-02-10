package ru.practicum.kanban.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTaskManager() {
        assertNotNull(Managers.getDefaultTaskManager(), "Managers не создал TaskManager.");
    }

    @Test
    void getDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistoryManager(), "Managers не создал HistoryManager.");
    }
}