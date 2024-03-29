package ru.practicum.kanban.service;

import ru.practicum.kanban.model.Epic;
import ru.practicum.kanban.model.SubTask;
import ru.practicum.kanban.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, SubTask> subTasks;
    private int idSequence = 0;
    private final HistoryManager  history;

    public InMemoryTaskManager(HistoryManager historyManager) {
        history = historyManager;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        Epic epic = epics.get(subTask.getEpicId());
        epic.addTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearTasks() {
        removeInHistory(tasks.keySet());
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        removeInHistory(epics.keySet());
        epics.clear();
        removeInHistory(subTasks.keySet());
        subTasks.clear();
    }

    @Override
    public void clearSubTask() {
        subTasks.clear();
        removeInHistory(subTasks.keySet());
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.calculateStatus();
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            history.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            history.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            history.add(subTask);
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setTitle(epic.getTitle());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic savedEpic = epics.get(epicId);
        if (savedEpic == null) {
            return;
        }
        savedEpic.removeTask(subTasks.get(subTask.getId()));
        savedEpic.updateTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void deleteTask(int id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            removeInHistory(removedTask.getId());
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic removedEpic = epics.remove(id);
        if (removedEpic != null) {
            removeInHistory(removedEpic.getId());
            for (SubTask subTask : removedEpic.getSubTasks()) {
                subTasks.remove(subTask.getId());
                removeInHistory(subTask.getId());
            }
        }
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask removedSubTask = subTasks.remove(id);
        if (removedSubTask != null) {
            removeInHistory(removedSubTask.getId());
            epics.computeIfPresent(removedSubTask.getEpicId(),
                    (epicId, epic) -> {
                        epic.removeTask(removedSubTask);
                        return epic;
                    });
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private int generateId() {
        return ++idSequence;
    }

    private void removeInHistory(Set<Integer> taskIds) {
        for (int id : taskIds) {
            removeInHistory(id);
        }
    }

    private void removeInHistory(int id) {
        history.remove(id);
    }
}