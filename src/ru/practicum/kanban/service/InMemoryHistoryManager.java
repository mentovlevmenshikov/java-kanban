package ru.practicum.kanban.service;

import ru.practicum.kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        removeNode(node);
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }

    private void linkLast(Task task) {
        final Node prev = last;
        final Node newNode = new Node(prev, task, null);
        last = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node == null) return;

        Node prev = node.prev;
        Node next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }

        history.remove(node.item.getId());
    }
}
