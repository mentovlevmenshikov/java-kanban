package ru.practicum.kanban.service;

import ru.practicum.kanban.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() > 9) {
            history.removeLast();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
