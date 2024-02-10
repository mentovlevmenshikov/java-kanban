package ru.practicum.kanban.model;

import java.util.*;

public class Epic extends Task {
    private final Set<SubTask> subTasks = new HashSet<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title) {
        super(title);
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addTask(SubTask subTask) {
        subTasks.add(subTask);
        calculateStatus();
    }

    public void updateTask(SubTask subTask) {
        if (!subTasks.add(subTask)) {
            subTasks.remove(subTask);
            subTasks.add(subTask);
        }
        calculateStatus();
    }

    public void removeTask(SubTask subTask) {
        boolean isRemove = subTasks.remove(subTask);
        if (isRemove) {
            calculateStatus();
        }
    }

    public void calculateStatus() {
        if (subTasks.isEmpty()) {
            setStatus(StatusOfTask.NEW);
            return;
        }

        Iterator<SubTask> iterator = subTasks.iterator();
        StatusOfTask status = iterator.next().getStatus();
        while (iterator.hasNext()) {
            if (status != iterator.next().getStatus()) {
                setStatus(StatusOfTask.IN_PROGRESS);
                return;
            }
        }

        setStatus(status);
    }
}
