package model.tda;

public class LinkedQueue implements Queue{
    private Node front; //anterior
    private Node rear; //posterior
    private int counter; //control de elementos encolados

    //Constructor
    public LinkedQueue(){
        front=rear=null;
        this.counter=0;
    }

    @Override
    public int size() {
        return this.counter;
    }

    @Override
    public void clear() {
        front=rear=null;
        this.counter=0;
    }

    @Override
    public boolean isEmpty() {
        return front==null;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        LinkedQueue aux = new LinkedQueue();
        int pos1=1;
        int pos2=-1; //si es -1 no existe
        while(!isEmpty()){
            if(util.Utility.compare(front(), element)==0){
                pos2 = pos1;
            }
            aux.enQueue(deQueue());
            pos1++;
        }//while
        //al final dejamos la cola en su estado original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return pos2;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);
        if(isEmpty()){ //la cola no existe
            rear = newNode;
            //garantizo q anterior quede apuntando al primer nodo
            front=rear; //anterior=posterior
        }else{ //significa q al menos hay un elemento en la cola

            rear.next = newNode; //posterior.sgte = nuevoNodo
            rear = newNode; //posterior = nuevoNodo
        }
        //al final actualizo el contador
        this.counter++;
    }

    @Override
    public void enQueue(Object element, Integer priority) throws QueueException {

    }

    @Override
    public Object deQueue() throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        Object element = front.data;
        //caso 1. cuando solo hay un elemento
        //cuando estan apuntando al mismo nodo
        if(front==rear){
            clear(); //elimino la cola
        }else{ //caso 2. caso contrario
            front = front.next; //anterior=anterior.sgte
        }
        //actualizo el contador de elementos encolados
        counter--;
        return element;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        LinkedQueue aux = new LinkedQueue();
        boolean finded = false;
        while(!isEmpty()){
            if(util.Utility.compare(front(), element)==0){
                finded = true;
            }
            aux.enQueue(deQueue());
        }//while
        //al final dejamos la cola en su estado original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return finded;
    }

    @Override
    public Object peek() throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        return front.data;
    }

    @Override
    public Object front() throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");
        return front.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Linked Queue is Empty";
        String result = "Linked Queue content\n";
        int auxCounter=this.counter;
        LinkedQueue aux = new LinkedQueue();
        try {
            while (!isEmpty()) {
                result += front() + " ";
                aux.enQueue(deQueue());
            }
            //al final dejamos la cola con loas valores default
            while (!aux.isEmpty()) {
                enQueue(aux.deQueue());
            }
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
        this.counter=auxCounter;
        return result;
    }


}

//package domain.queue;
//
//public class LinkedQueue implements Queue {
//    private Node front;
//    private Node rear;
//    private int counter;
//
//    public LinkedQueue() {
//        front = rear = null;
//        counter = 1;
//    }
//
//    @Override
//    public int size() {
//        return counter;
//    }
//
//    @Override
//    public void clear() {
//        front = rear = null;
//        counter = 1;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return front == null;
//    }
//
//    @Override
//    public int indexOf(Object element) throws QueueException {
//        if (isEmpty()) throw new QueueException("Queue is empty");
//        if (rear.data.equals(element)) return counter;
//        if (front.data.equals(element)) return 1;
//        LinkedQueue aux= new LinkedQueue();
//        int index=1;
//            while(peek()!=element){
//                aux.enQueue(deQueue());
//                index++;
//            }
//            while(!isEmpty()){
//                aux.enQueue(deQueue());
//            }
//            while(!aux.isEmpty()){
//                enQueue(aux.deQueue());
//            }
//        return index;
//    }
//    @Override
//    public void enQueue(Object element) throws QueueException {
//        Node newNode = new Node(element);
//        if(isEmpty()){ //la cola no existe
//            rear = newNode;
//            //garantizo q anterior quede apuntando al primer nodo
//            front=rear; //anterior=posterior
//        }else{ //significa q al menos hay un elemento en la cola
//            rear.next = newNode; //posterior.sgte = nuevoNodo
//            rear = newNode; //posterior = nuevoNodo
//        }
//        //al final actualizo el contador
//        counter++;
//    }
//
//    @Override
//    public Object deQueue() throws QueueException {
//        if (isEmpty())
//            throw new QueueException("Queue is empty");
//
//        Object element = front.data;
//        //caso 1. cuando solo hay un elemento
//        //cuando estan apuntando al mismo nodo
//        if(front==rear){
//            clear(); //elimino la cola
//        }else{ //caso 2. caso contrario
//            front = front.next; //anterior=anterior.sgte
//        }
//        //actualizo el contador de elementos encolados
//        counter--;
//        return element;
//    }
//
//    @Override
//    public boolean contains(Object element) throws QueueException {
//        if (isEmpty())
//            throw new QueueException("Queue is empty");
//         boolean found = false;
//         LinkedQueue aux= new LinkedQueue();
//         while(!isEmpty()){
//             if(util.Utility.compare(front(), element)==0){
//                 found = true;
//             }
//             aux.enQueue(deQueue());
//         }
//         while(!aux.isEmpty()){
//             enQueue(aux.deQueue());
//         }
//        return found;
//    }
//
//    @Override
//    public Object peek() throws QueueException {
//        if(isEmpty())throw new QueueException("Queue is empty");
//        return front.data;
//    }
//
//    @Override
//    public Object front() throws QueueException {
//        return peek();
//    }
//
//    @Override
//    public String toString() {
//        if (isEmpty())
//            return "Linked Queue is Empty";
//
//        String result = "Linked Queue Content \n";
//        LinkedQueue aux = new LinkedQueue();
//
//        try{
//            while (!isEmpty()){
//                result += front() + "\n";
//                aux.enQueue(deQueue());
//            }
//
//            while (!aux.isEmpty()){
//                enQueue(aux.deQueue());
//            }
//        } catch (QueueException q) {
//            throw new RuntimeException();
//        }
//        return result;
//    }
//}
