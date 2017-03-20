package com.leon.chat.activity;

/**
 * Created by leon on 17/2/14.
 */

public class TestActivity {

    public class Node{
        Node next;
        Node last;
        Object data;

        public Node(Object data){
            this.data = data;
        }

    }

    private Node head;
    private Node rear;

    public void add(Object data){
        Node node = new Node(data);
        if (head == null){
            head = node;
            rear = node;
        }else {
            rear.next = node;
            node.last = rear;
            rear = node;
        }
    }

}
