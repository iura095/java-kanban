package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyList = new HashMap<>();
    private Node head = new Node(null);
    private Node tail = new Node(null);
    private int size = 0;

    public HashMap<Integer, Node> getHistoryList() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyList.containsKey(task.getTaskID())) {
            remove(task.getTaskID());
        }
        linkLast(task);
        historyList.put(task.getTaskID(), tail.getPrev());
    }

    @Override
    public void remove(int id) {
        removeNode(historyList.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        Node newNode = new Node(task);
        if (size == 0) {
            head.setNext(newNode);
            newNode.setPrev(head);
            newNode.setNext(tail);
        } else {
            tail.getPrev().setNext(newNode);
            newNode.setPrev(tail.getPrev());
            newNode.setNext(tail);
        }
        tail.setPrev(newNode);
        size++;
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        if (size == 0) {
            return list;
        } else {
            if (head.getNext() == null) {
                return list;
            }
            Node tempNode = head.getNext();
            while (tempNode.getValue() != null) {
                list.add(tempNode.getValue());
                tempNode = tempNode.getNext();
            }
        }
        return list;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        size--;
    }

    public List<Node> getNodeList() {
        return new ArrayList<>(historyList.values());
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

}
