package model.tda;

public class ArrayQueue implements Queue {
    private Object[] queue;
    private int front;
    private int rear;
    private int n;

    public ArrayQueue(int n){
        if(n<=0) System.exit(1); //se sale
        this.n = n;
        this.queue = new Object[n];
        this.rear = n-1; //ultimo elemento de la cola
        this.front = rear;
    }
    @Override
    public int size() {
        return rear-front;
    }

    @Override
    public void clear() {
        this.rear = n-1; //ultimo elemento de la cola
        front = rear;
        this.queue = new Object[n];
    }

    @Override
    public boolean isEmpty() {
        return rear==front;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");

        ArrayQueue aux = new ArrayQueue(size());
        int index = 0;
        int foundedIndex = -1;//-1 si el indice no fue encontrado

        while (!isEmpty()) {
            Object current = deQueue();//se guarda en una variable para comparar
            if (foundedIndex == -1 && current.equals(element)) {
                foundedIndex = index;
            }
            aux.enQueue(current);
            index++;
        }
        while (!aux.isEmpty()) {
            enQueue(aux.deQueue());
        }

        return foundedIndex;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        if(size()==queue.length)
            throw new QueueException("Array Queue is Full");

        //la primera vez no entra al for
        for (int i = front; i < rear; i++) {
            queue[i] = queue[i+1];
        }
        //siempre encola en el extremo posterior
        //y mueve anterior una posicion a la izq
        queue[rear] = element;
        front--; //la idea es q anterior quede en un campo vacio
    }

    @Override
    public void enQueue(Object element, Integer priority) throws QueueException {

    }

    @Override
    public Object deQueue() throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");
        return queue[++front];
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");

        ArrayQueue aux = new ArrayQueue(size());
        boolean found = false;

        while (!isEmpty()) {
            Object current = deQueue();//guardo en una variable para evaluar
            if (!found && current.equals(element)) {
                found = true;
            }
            aux.enQueue(current);
        }
        while (!aux.isEmpty()) {
            enQueue(aux.deQueue());
        }

        return found;
    }

    @Override
    public Object peek() throws QueueException {
        if (isEmpty())
            throw new QueueException("Queue is empty");
        return queue[front+1];
    }

    @Override
    public Object front() throws QueueException {
        return peek();
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Array Queue is Empty";

        String result = "Array Queue Content \n";
        ArrayQueue aux = new ArrayQueue(size());

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
