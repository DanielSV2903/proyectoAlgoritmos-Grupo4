package model.tda;

public class HeaderLinkedQueue implements Queue {
    private Node front;
    private Node rear;
    private int counter;

    public HeaderLinkedQueue() {
        front =rear= new Node();
        counter = 0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear = new Node(); // nodo dummy otra vez
        counter = 0;
    }


    @Override
    public boolean isEmpty() {
        return front==rear;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");
        if (rear.data.equals(element)) return counter;
        if (front.data.equals(element)) return 1;
        HeaderLinkedQueue aux= new HeaderLinkedQueue();
        int index=1;
            while(peek()!=element){
                aux.enQueue(deQueue());
                index++;
            }
            while(!isEmpty()){
                aux.enQueue(deQueue());
            }
            while(!aux.isEmpty()){
                enQueue(aux.deQueue());
            }
        return index;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);
        rear.next = newNode;
        rear = newNode;
        counter++;
    }

    @Override
    public void enQueue(Object element, Integer priority) throws QueueException {

    }

    @Override
    public Object deQueue() throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");

        Object element = front.next.data;

        if (front.next == rear)
            clear();
        else
            front.next = front.next.next;
        counter--;
        return element;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");

        HeaderLinkedQueue aux= new HeaderLinkedQueue();
        boolean found = false;

         while(!isEmpty()){
             if(util.Utility.compare(front(), element)==0){
                 found = true;
             }
             aux.enQueue(deQueue());
         }
         while(!aux.isEmpty()){
             enQueue(aux.deQueue());
         }
        return found;
    }

    @Override
    public Object peek() throws QueueException {
        if(isEmpty())
            throw new QueueException("Queue is empty");
        return front.next.data;
    }

    @Override
    public Object front() throws QueueException {
        if(isEmpty())
            throw new QueueException("Header Linked Queue is Empty");
        return front.next.data;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Header Linked Queue is Empty";

        String result = "Header Linked Queue Content \n";
        HeaderLinkedQueue aux = new HeaderLinkedQueue();

        try{
            while (!isEmpty()){
                result += front() + "\n";
                aux.enQueue(deQueue());
            }

            while (!aux.isEmpty()){
                enQueue(aux.deQueue());
            }
        } catch (QueueException q) {
            throw new RuntimeException();
        }
        return result;
    }
}
