package ru.practicum.kanban.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic;

    @BeforeEach
    void createEpic() {
            epic = new Epic("Тестовый эпик");
    }

    @Test
    void addTask() {
        SubTask subTask = new SubTask("Подзадача", epic.getId());
        epic.addTask(subTask);
        Set<SubTask> subTasks = epic.getSubTasks();
        assertNotNull(subTasks, "Список подзадач пустой.");
        assertTrue(subTasks.contains(subTask), "Подзадача не добавилась.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач." );
    }

    @Test
    void updateTask() {
        SubTask subTask = new SubTask("Подзадача", "описание", epic.getId());
        epic.addTask(subTask);
        String newTitle = "Подзадача новая";
        String newDescription = "описание новое";
        StatusOfTask newStatus = StatusOfTask.IN_PROGRESS;
        SubTask subTaskNew = new SubTask(newTitle, newDescription, epic.getId());
        subTaskNew.setStatus(newStatus);
        epic.updateTask(subTaskNew);
        Iterator<SubTask> iterator = epic.getSubTasks().iterator();
        assertTrue(iterator.hasNext(), "Отсутсвуют подзадачи");
        SubTask subTaskSaved = iterator.next();
        assertEquals(newTitle, subTaskSaved.getTitle(), "Неверный заголовок подзадачи.");
        assertEquals(newDescription, subTaskSaved.getDescription(), "Неверное описание подзадачи.");
        assertEquals(newStatus, subTaskSaved.getStatus(), "Неверный статус подзадачи.");
        assertEquals(newStatus, epic.getStatus(), "Неверный статус эпика.");
    }

    @Test
    void removeTask() {
        SubTask subTask = new SubTask("Подзадача", "описание", epic.getId());
        subTask.setStatus(StatusOfTask.DONE);
        epic.addTask(subTask);
        assertEquals(StatusOfTask.DONE, epic.getStatus(), "Не верный статус эпика.");
        epic.removeTask(subTask);
        assertEquals(0, epic.getSubTasks().size(), "Не верное количество подзадач после удаления.");
        assertEquals(StatusOfTask.NEW, epic.getStatus(), "Не верный статус эпика.");
    }

    @Test
    void calculateStatus() {
        SubTask subTask = new SubTask("Подзадача", epic.getId());
        epic.addTask(subTask);
        assertEquals(StatusOfTask.NEW, epic.getStatus(), "Не верный статус эпика.");
        subTask.setStatus(StatusOfTask.DONE);
        epic.updateTask(subTask);
        assertEquals(StatusOfTask.DONE, epic.getStatus(), "Не верный статус эпика.");
        SubTask subTask2 = new SubTask("Подзадача 2", epic.getId());
        subTask2.setId(subTask.getId()+1);
        epic.addTask(subTask2);
        assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus(), "Не верный статус эпика.");
        epic.removeTask(subTask2);
        assertEquals(StatusOfTask.DONE, epic.getStatus(), "Не верный статус эпика.");
    }
}