package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private HashMap<Integer, Node> historyList = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (size == 0) {
            linkLast(task);
            historyList.put(task.getTaskID(), this.head);
        } else if (historyList.containsKey(task.getTaskID())) {
            removeNode(historyList.get(task.getTaskID()));
            linkLast(task);
            if (size == 1) {
                historyList.put(task.getTaskID(), head);
            } else {
                historyList.put(task.getTaskID(), tail);
            }
        } else {
            linkLast(task);
            historyList.put(task.getTaskID(), this.tail);
        }

    }

    @Override
    public void remove (int id) {
        if (historyList.containsKey(id)) {
            removeNode(historyList.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast (Task task) {
        if (size == 0) {
            this.head = new Node(task);
            size++;
        } else if (size == 1) {
            this.tail = new Node(task);
            this.tail.setPrev(head);
            this.head.setNext(tail);
            size++;
        } else {
            Node node = tail;
            this.tail = new Node(task);
            this.tail.setPrev(node);
            node.setNext(this.tail);
            size++;
        }
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        if (size == 0) {
            return list;
        } else {
            Node tempNode = head;
            for (int i = 0; i < size; i++) {
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

        if (node.getPrev() == null) {
            head = head.getNext();
            if (head != null) {
                head.setPrev(null);
            }
            historyList.remove(node.getValue().getTaskID());
            size--;
        } else if (node.getNext() == null) {
            tail = tail.getPrev();
            if (tail != null) {
                tail.setNext(null);
            }
            historyList.remove(node.getValue().getTaskID());
            size--;
            if (size == 1) {
                head.setNext(null);
            }
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            historyList.remove(node.getValue().getTaskID());
            size--;
        }
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
