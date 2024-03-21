package ru.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.kanban.model.Epic;
import ru.practicum.kanban.model.StatusOfTask;
import ru.practicum.kanban.model.SubTask;
import ru.practicum.kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void  createTaskManager() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void createTask() {
        Task task = taskManager.createTask(new Task("Первая задача"));
        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = taskManager.createEpic(new Epic("Первый epic", "эпик"));
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпикии не совпадают.");
    }

    @Test
    void createSubTask() {
        Epic epic = taskManager.createEpic(new Epic("Первый epic", "эпик"));
        SubTask subTask = taskManager.createSubTask(new SubTask("Первый subtask", epic.getId()));
        SubTask savedSubTask = taskManager.getSubTask(subTask.getId());
        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
        assertEquals(epic.getId(), savedSubTask.getEpicId(), "Эпики у подзадачи не совпадают.");
    }

    @Test
    void getAllTasks() {
        Task task1 = taskManager.createTask(new Task("Первая задача"));
        Task task2 = taskManager.createTask(new Task("Вторая задача"));
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают 1.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают 2.");
    }

    @Test
    void getAllEpics() {
        Epic epic1 = taskManager.createEpic(new Epic("Первый эпик"));
        Epic epic2 = taskManager.createEpic(new Epic("Второй эпик"));
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают 1.");
        assertEquals(epic2, epics.get(1), "Эпики не совпадают 2.");
    }

    @Test
    void getAllSubTasks() {
        Epic epic = taskManager.createEpic(new Epic("Первый epic", "эпик"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Первый subtask", epic.getId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Второй subtask", epic.getId()));

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(2, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask1, subTasks.get(0), "Подзадачи не совпадают 1.");
        assertEquals(subTask2, subTasks.get(1), "Подзадачи не совпадают 2");

        assertEquals(epic.getId(), subTasks.get(0).getEpicId(), "Эпики у подзадачи не совпадают 1.");
        assertEquals(epic.getId(), subTasks.get(1).getEpicId(), "Эпики у подзадачи не совпадают 2.");
    }

    @Test
    void clearTasks() {
        taskManager.createTask(new Task("Первая задача"));
        taskManager.createTask(new Task("Вторая задача"));
        taskManager.clearTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Неверное количество задач после очистки.");
    }

    @Test
    void clearEpics() {
        taskManager.createEpic(new Epic("Первый эпик"));
        taskManager.createEpic(new Epic("Второй эпик"));
        taskManager.clearEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков после очистки.");
    }

    @Test
    void clearSubTask() {
        Epic epic = taskManager.createEpic(new Epic("Первый epic", "эпик"));
        taskManager.createSubTask(new SubTask("Первый subtask", epic.getId()));
        taskManager.createSubTask(new SubTask("Второй subtask", epic.getId()));
        taskManager.clearSubTask();
        assertEquals(0, taskManager.getAllSubTasks().size(), "Неверное количество подзадач после очистки.");
    }

    @Test
    void updateTask() {
        Task taskOriginal = taskManager.createTask(new Task("Задача", "исходная"));
        int idOriginal = taskOriginal.getId();
        String newTitle = "Новая задача";
        String newDescription = "новое описание";
        StatusOfTask newStatus = StatusOfTask.DONE;
        Task newTask = new Task(newTitle, newDescription);
        newTask.setId(idOriginal);
        newTask.setStatus(newStatus);
        taskManager.updateTask(newTask);
        Task savedTaskAfterUpdate = taskManager.getTask(idOriginal);
        assertEquals(newTitle, savedTaskAfterUpdate.getTitle(), "После обновления задачи заголовок не совпадает.");
        assertEquals(newDescription, savedTaskAfterUpdate.getDescription(), "После обновления задачи описание не совпадает.");
        assertEquals(idOriginal, savedTaskAfterUpdate.getId(), "После обновления задачи ИД не совпадает.");
        assertEquals(newStatus, savedTaskAfterUpdate.getStatus(), "После обновления задачи статус не совпадает.");
    }

    @Test
    void updateEpic() {
        Epic epicOriginal = taskManager.createEpic(new Epic("Эпик", "исходный"));
        taskManager.createSubTask(new SubTask("Первый subtask", epicOriginal.getId()));
        int idOriginal = epicOriginal.getId();
        String newTitle = "Новый эпик";
        String newDescription = "новое описание";
        Epic newEpic = new Epic(newTitle, newDescription);
        newEpic.setId(idOriginal);
        taskManager.updateEpic(newEpic);
        Epic savedEpicAfterUpdate = taskManager.getEpic(idOriginal);
        assertEquals(newTitle, savedEpicAfterUpdate.getTitle(), "После обновления эпика заголовок не совпадает.");
        assertEquals(newDescription, savedEpicAfterUpdate.getDescription(), "После обновления эпика описание не совпадает.");
        assertEquals(idOriginal, savedEpicAfterUpdate.getId(), "После обновления эпика ИД не совпадает.");
        assertEquals(1, savedEpicAfterUpdate.getSubTasks().size(), "После обновления эпика количество подзадач не совпадает.");
    }

    @Test
    void updateSubTask() {
        Epic epic = taskManager.createEpic(new Epic("Эпик", "эпик"));
        SubTask subTaskOriginal = taskManager.createSubTask(new SubTask("Подзадача", "исходная", epic.getId()));
        int idOriginal = subTaskOriginal.getId();
        String newTitle = "Новая подзадача";
        String newDescription = "новое описание";
        StatusOfTask newStatus = StatusOfTask.DONE;
        int idEpic = epic.getId();
        SubTask newSubTask = new SubTask(newTitle, newDescription, idEpic);
        newSubTask.setId(idOriginal);
        newSubTask.setStatus(newStatus);
        taskManager.updateSubTask(newSubTask);
        SubTask savedSubTaskAfterUpdate = taskManager.getSubTask(idOriginal);
        assertEquals(newTitle, savedSubTaskAfterUpdate.getTitle(), "После обновления подзадачи заголовок не совпадает.");
        assertEquals(newDescription, savedSubTaskAfterUpdate.getDescription(), "После обновления подзадачи описание не совпадает.");
        assertEquals(idEpic, savedSubTaskAfterUpdate.getEpicId(), "После обновления подзадачи ИД эпика не совпадает.");
        assertEquals(newStatus, savedSubTaskAfterUpdate.getStatus(), "После обновления подзадачи статус подзадачи не совпадает.");
        assertEquals(newStatus, epic.getStatus(), "После обновления подзадачи статус эпика не совпадает.");
    }

    @Test
    void deleteTask() {
        Task task1 = taskManager.createTask(new Task("Первая задача"));
        taskManager.createTask(new Task("Вторая задача"));
        taskManager.deleteTask(task1.getId());
        assertNull(taskManager.getTask(task1.getId()), "Задача не была удалена.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач после удаления.");
    }

    @Test
    void deleteEpic() {
        taskManager.createEpic(new Epic("Первый эпик"));
        Epic epic2 = taskManager.createEpic(new Epic("Второй эпик"));
        SubTask subTask = taskManager.createSubTask(new SubTask("Подзадача", epic2.getId()));
        taskManager.deleteEpic(epic2.getId());
        assertNull(taskManager.getEpic(epic2.getId()), "Эпик не был удален.");
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков после удаления.");
        assertNull(taskManager.getSubTask(subTask.getId()), "Подзадача из удаленного эпика не удалена.");
    }

    @Test
    void deleteSubTask() {
        Epic epic = taskManager.createEpic(new Epic("Первый epic", "эпик"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Первый subtask", epic.getId()));
        taskManager.createSubTask(new SubTask("Второй subtask", epic.getId()));
        taskManager.deleteSubTask(subTask1.getId());
        assertNull(taskManager.getSubTask(subTask1.getId()), "Подзадача не была удалена.");
        assertEquals(1, taskManager.getAllSubTasks().size(), "Неверное количество подзадач после удаления.");
    }

    @Test
    void getHistory() {
        int countTask = 12;
        for (int i = 0; i < countTask; i++) {
            taskManager.createTask(new Task("Задача № " + (i+1)));
        }

        for (Task task : taskManager.getAllTasks()) {
            taskManager.getTask(task.getId());
            assertTrue(taskManager.getHistory().contains(task), "Задача не попала в историю.");
        }

        int i = 0;
        for (Task task : taskManager.getHistory()) {
            if (i % 2 == 0) {
                taskManager.getTask(task.getId());
            }
            i++;
        }

        assertEquals(countTask, taskManager.getHistory().size(), "Неверное число задач в истории.");

        Epic epic = taskManager.createEpic(new Epic("Эпик"));
        List<Task> history = taskManager.getHistory();
        Task taskFirstInHistory = history.get(0);
        taskManager.getEpic(epic.getId());
        assertTrue(taskManager.getHistory().contains(epic), "Эпик не попал в историю.");
        taskManager.getTask(taskFirstInHistory.getId());
        history = taskManager.getHistory();
        assertNotEquals(taskFirstInHistory, history.get(0), "Задача осталась первой в истории.");
        assertEquals(taskFirstInHistory, history.get(history.size() - 1), "Задача не стала последней в истории.");

        SubTask subTaskTask = taskManager.createSubTask(new SubTask("Подзадача", epic.getId()));
        taskManager.getSubTask(subTaskTask.getId());
        assertTrue(taskManager.getHistory().contains(subTaskTask), "Подзадача не попала в историю.");
    }
}