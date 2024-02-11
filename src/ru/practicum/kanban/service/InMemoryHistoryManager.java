package ru.practicum.kanban.service;

import ru.practicum.kanban.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
     private static int HISTORY_SIZE = 10;
    @Override
    public void add(Task task) {
        if (history.size() >= HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<Task>(history);
    }
}
