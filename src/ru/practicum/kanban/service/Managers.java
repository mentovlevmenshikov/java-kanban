package ru.practicum.kanban.service;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
