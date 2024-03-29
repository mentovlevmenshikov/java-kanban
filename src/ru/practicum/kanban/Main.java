package ru.practicum.kanban;

import ru.practicum.kanban.model.Epic;
import ru.practicum.kanban.model.StatusOfTask;
import ru.practicum.kanban.model.SubTask;
import ru.practicum.kanban.model.Task;
import ru.practicum.kanban.service.Managers;
import ru.practicum.kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task1 = taskManager.createTask(new Task("Первая задача"));
        Task task2 = taskManager.createTask(new Task("Вторая задача", "Очень важное задание"));

        Epic epic1 = taskManager.createEpic(new Epic("Первый epic", "с двумя подзадачами"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Подзадача первая", "Подзадача для первого epica", epic1.getId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Подзадача вторая", "Подзадача для первого epica", epic1.getId()));

        Epic epic2 = taskManager.createEpic(new Epic("Второй epic", "с одной позодачай"));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("Еще одна подзадача", epic2.getId()));

        printAllTasks(taskManager);
        System.out.println("\n\n");

        task1.setStatus(StatusOfTask.DONE);
        task2.setStatus(StatusOfTask.IN_PROGRESS);

        subTask1.setStatus(StatusOfTask.DONE);
        subTask2.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);

        subTask3.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);

        printAllTasks(taskManager);
        System.out.println("\n\n");

        taskManager.deleteTask(task2.getId());
        taskManager.deleteEpic(epic2.getId());
        taskManager.deleteSubTask(subTask2.getId());
        taskManager.getTask(task1.getId());
        printAllTasks(taskManager);
    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println("All tasks:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("All epics");
        System.out.println(taskManager.getAllEpics());
        System.out.println("All subtasks:");
        System.out.println(taskManager.getAllSubTasks());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : epic.getSubTasks()) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
