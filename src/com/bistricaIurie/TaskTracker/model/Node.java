package com.bistricaIurie.TaskTracker.model;

public class Node {
    private Node prev;
    private final Task value;
    private Node next;

    public Node(Task value) {
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Task getValue() {
        return value;
    }

//    public void setValue(Task value) {
//        this.value = value;
//    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        Task nodePrev = null;
        Task nodeNext = null;
        if (this.prev != null) {
            nodePrev = prev.getValue();
        }
        if (this.next != null) {
            nodeNext = next.getValue();
        }

        return "Node{" +
                "prev=" + nodePrev +
                ", value=" + value +
                ", next=" + nodeNext +
                '}';
    }
}
