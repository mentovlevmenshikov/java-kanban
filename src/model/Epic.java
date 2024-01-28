package model;

import java.util.*;

public class Epic extends Task {
    private Set<SubTask> subTasks = new HashSet<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title) {
        super(title);
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(HashSet<SubTask> subTasks) {
        this.subTasks = subTasks;
        calculateStatus();
    }

    public void addTask(SubTask subTask) {
        subTasks.add(subTask);
        calculateStatus();
    }

    public void updateTask(SubTask subTask) {
        subTasks.add(subTask);
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
