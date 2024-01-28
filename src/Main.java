import model.Epic;
import model.StatusOfTask;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask(new Task("Первая задача"));
        Task task2 = taskManager.createTask(new Task("Вторая задача", "Очень важное задание"));

        Epic epic1 = taskManager.createEpic(new Epic("Первый epic", "с двумя подзадачами"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Подзадача первая", "Подзадача для первого epica", epic1.getId()));
        epic1.addTask(subTask1);
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Подзадача вторая", "Подзадача для первого epica", epic1.getId()));
        epic1.addTask(subTask2);

        Epic epic2 = taskManager.createEpic(new Epic("Второй epic", "с одной позодачай"));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("Еще одна подзадача", epic2.getId()));
        epic2.addTask(subTask2);

        printTasks(taskManager);
        System.out.println("\n\n");

        task1.setStatus(StatusOfTask.DONE);
        task2.setStatus(StatusOfTask.IN_PROGRESS);

        subTask1.setStatus(StatusOfTask.DONE);
        subTask2.setStatus(StatusOfTask.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateTask(subTask2);

        subTask3.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);

        printTasks(taskManager);
        System.out.println("\n\n");

        taskManager.deleteTask(task2.getId());
        taskManager.deleteEpic(epic2.getId());
        printTasks(taskManager);

    }

    public static void printTasks(TaskManager taskManager) {
        System.out.println("All tasks:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("All epics");
        System.out.println(taskManager.getAllEpics());
        System.out.println("All subtasks:");
        System.out.println(taskManager.getAllSubTasks());
    }
}
