package com.example.connectverseproject;

public class priorityQueue {
    public class Node {
        post p;
        Node next;

        Node(post p) {
            this.p = p;
            next = null;
        }

    }
    Node root;
        public void feed_by_Priority(post p) {
            Node temp = root;
        Node newNode=new Node(p);
            // Case 1: Empty list or new post has earlier datetime than root → insert at front
            if (root == null || p.getDatetime().compareTo(root.p.getDatetime()) > 0) {
                newNode.next = root;
                root = newNode;
                return;
            }
            while (temp.next != null && p.getDatetime().compareTo(temp.next.p.getDatetime()) <= 0) {
                temp = temp.next;
            }

            // Insert newNode after temp
            newNode.next = temp.next;
            temp.next = newNode;

    }

    public void printQueue() {
        Node temp = root;
        while (temp != null) {
            System.out.println(temp.p.getDatetime()); // assuming post has getDatetime()
            temp = temp.next;
        }
    }
   public boolean isEmpty(){
            return root==null;
    }

    public post poll(){
            post p=root.p;
            root=root.next;
            return p;
    }

}
