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
//
//package com.example.connectverseproject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class priorityQueue {
//    private List<post> heap;
//
//    public priorityQueue() {
//        heap = new ArrayList<>();
//    }
//
//    // Get parent/child indexes
//    private int parent(int i) { return (i - 1) / 2; }
//
//    // Swap helper
//    private void swap(int i, int j) {
//        post temp = heap.get(i);
//        heap.set(i, heap.get(j));
//        heap.set(j, temp);
//    }
//
//
//    // Insert (O(log n))
//    public void add(post p) {
//        heap.add(p);
//        int i = heap.size() - 1;
//
//        // Bubble up (min-heap: earlier datetime = higher priority)
//        while (i > 0 && heap.get(i).getDatetime().compareTo(heap.get(parent(i)).getDatetime()) > 0) {
//            swap(i, parent(i));
//            i = parent(i);
//        }
//    }
//
//    // Debug print
//    public void printHeap() {
//        for (post p : heap) {
//            System.out.println(p.getDatetime());
//        }
//    }
//    private void heapify(int i) {
//        int largest = i;
//        int l = 2*i+1;
//        int r = 2*i+2;
//
//        if (l < heap.size() && heap.get(l).getDatetime().compareTo(heap.get(largest).getDatetime()) > 0) {
//            largest = l;
//        }
//        if (r < heap.size() && heap.get(r).getDatetime().compareTo(heap.get(largest).getDatetime()) > 0) {
//            largest = r;
//        }
//
//        if (largest != i) {
//            swap(i, largest);
//            heapify(largest);
//        }
//    }
//    public post poll() {
//        if (heap.isEmpty()) return null;
//
//        post root = heap.get(0);
//        post last = heap.remove(heap.size() - 1);
//
//        if (!heap.isEmpty()) {
//            heap.set(0, last);
//            heapify(0);
//        }
//
//        return root;
//    }
//}
//
