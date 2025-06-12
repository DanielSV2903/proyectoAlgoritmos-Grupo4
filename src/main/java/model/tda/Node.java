package model.tda;

public class Node {
    public Object data;
    public Node prev; //apuntador al nodo anterior
    public Node next; //apuntador al nodo siguiente
    public Integer priority;

    //Constructor 1
    public Node(Object data) {
        this.data = data;
        this.prev = this.next = null; //puntero al sgte nodo es nulo por default
    }

    //Constructor 2
    public Node() {
        this.prev = this.next = null;
    }
    //Constructor sobrecargado
    public Node(Object data, Integer priority){
        this.data = data;
        this.priority = priority;
        this.next = null; //apunta a nulo
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
