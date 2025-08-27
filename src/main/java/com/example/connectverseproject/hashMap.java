

// user defined class as can use in place of in-built hashmap ,whee key would be username and value would be user

package com.example.connectverseproject;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.LinkedList;

public class hashMap <key,value> {

    class Node{
        key k;
        value v;
        Node(key k,value v){
            this.k=k;
            this.v=v;
        }
    }

    int size;
    int capacity;
    LinkedList<Node> buckets [];

    hashMap(){
        capacity=10;
        this.buckets=new LinkedList[capacity];
        for (int i=0;i<capacity;i++){
            buckets[i]=new LinkedList<>();
        }
    }


    private int hashing(key k){
        int code=k.hashCode();
        return Math.abs(code) %capacity;
    }
    private int searching(int bi,key k){
        LinkedList<Node> toFind=buckets[bi];
        for (int i=0;i<toFind.size();i++){
            if(toFind.get(i)==k){
                return i;
            }
        }
        return -1;
    }
    private void rehasing(){
        this.capacity=2*capacity;
        LinkedList<Node> oldBuckets[]=buckets;
        for (int i=0;i<capacity*2;i++){
            oldBuckets[i]=new LinkedList<>();
        }
        for (int i=0;i<oldBuckets.length;i++){
            LinkedList<Node> ListAtEachIndex=oldBuckets[i];
            for (int j=0;j<ListAtEachIndex.size();j++){
                Node node=oldBuckets[i].get(j);
                put(node.k, node.v);
            }
        }
    }
    public void put(key k,value v){

        int bucketIdx=hashing(k);
        int toFind=searching(bucketIdx,k);
         if(toFind==-1){
             buckets[bucketIdx].addFirst(new Node(k,v));
             size++;
         }else{
             AppData.showAlert(Alert.AlertType.WARNING,"Username Exists","⚠️ Username already exists. Try another.");
         }

        double lambda=(double) size/capacity;
         if(lambda>2.0)
             rehasing();


    }

    public value get(key k){
        int bucketIdx=hashing(k);
        int toFind=searching(bucketIdx,k);
        if(toFind==-1){
            return null;
        }else{
            Node node=buckets[bucketIdx].get(toFind);
            return node.v;
        }

    }

    public value remove(key k){
        int bucketIdx=hashing(k);
        int toFind=searching(bucketIdx,k);
        if(toFind==-1){
            return null;
        }else{
            Node node=buckets[bucketIdx].get(toFind);
            size--;
            return node.v;
        }

    }

    public boolean containsKey(key k){
        int bucketIdx=hashing(k);
        int toFind=searching(bucketIdx,k);
        if(toFind==-1){
            return false;
        }else{
            return true;
        }

    }
    public ArrayList<key> keysSet(){
        ArrayList<key> set=new ArrayList<>();
        for (int i=0;i<buckets.length;i++){
            LinkedList<Node> ll=buckets[i];
            for (int j=0;j<ll.size();j++){
                Node node=buckets[i].get(j);
                set.add(node.k);

            }
        }
        return set;
    }
    public boolean isEmpty(){
        return size==0;
    }


}
