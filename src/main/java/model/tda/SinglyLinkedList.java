package model.tda;

public class SinglyLinkedList implements List {
    private Node first; //apuntador al inicio de la lista

    //Constructor
    public SinglyLinkedList(){
        this.first = null;
    }

    @Override
    public int size() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        int counter = 0; //contador de nodos
        Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
        while(aux!=null){
            counter++;
            aux = aux.next;
        }
        return counter;
    }

    @Override
    public void clear() {
        this.first = null; //anula la lista
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public boolean contains(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        while(aux!=null){
            if(util.Utility.compare(aux.data, element)==0) return true; //ya lo encontro
            aux = aux.next; //muevo aux al nodo sgte
        }
        return false; //significa que no encontro el elemento
    }

    @Override
    public void add(Object element) {
        Node newNode = new Node(element);
        if(isEmpty())
            first = newNode;
        else{
            Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
            while(aux.next!=null){
                aux = aux.next; //mueve aux al nodo sgte
            }
            //se sale del while cuando aux esta en el ult nodo
            aux.next = newNode;
        }
    }

    @Override
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if(isEmpty())
            first = newNode;
        else
            newNode.next = first;
        first = newNode;
    }

    @Override
    public void addLast(Object element) {
        add(element);
    }

    @Override
    public void addInSortedList(Object element) throws ListException {
        if (isEmpty())
            add(element);
        else {
            sort();
            Node newNode = new Node(element);
            Node aux = first;
            Node aux2 = first.next;
        }
    }

    @Override
    public void remove(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        //Caso 1: El elemento a suprimir es el primero de la lista
        if(util.Utility.compare(first.data, element)==0){
            first = first.next;

            //Caso 2. El elemento puede estar en el medio o al final
        }else{
            Node prev = first; //nodo anterior
            Node aux = first.next; //nodo sgte

            while(aux!=null && !(util.Utility.compare(aux.data, element)==0)){
                prev = aux;
                aux = aux.next;
            }
            //se sale del while cuanda alcanza nulo
            //o cuando encuentra el elemento
            if(aux!=null && util.Utility.compare(aux.data, element)==0){
                //debo desenlazar  el nodo
                prev.next = aux.next;
            }
        }
    }

    @Override
    public Object removeFirst() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        Object value = first.data;
        first = first.next; //movemos el apuntador al nodo sgte
        return value;
    }

    @Override
    public Object removeLast() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        else {
            Node prev = first; //nodo anterior
            Node aux = first.next; //nodo sgte
            while(aux.next!=null){
                prev = aux;
                aux = aux.next;
            }
            Object value = aux.data;
            prev.next = null;
            return value;
        }
    }

    @Override
    public void sort() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        else {
            //Define 3 nodos para la navegacion y redefinicion del orden de la lista
            Node prev = first.prev; //el prev está definido en null
            Node current = first;
            Node next = first.next;
            //Condicion mientras que next no sea null (next = first.next)
            while (next != null) {
                //con el método compare, compara el elemento actual de la lista para saber si es mayor que el siguiente
                //Caso base: Inicio de la lista
                if (util.Utility.compare(current.data, next.data) == 1 && prev == null) {
                    //si el actual es menor que el siguiente entonces:
                    prev = next; //el nodo null pasa a almacenar los datos del dato menor
                    current.next = next.next; //elemento mayor pasa a la posicion siguiente (donde estaba el elemento menor)
                    prev.next = current;// el elemento menor pasa a estar antes del elemento mayor
                    first = prev;

                    //reiniciar punteros
                    prev = null;
                    current = first;
                    next = current.next;
                    continue;

                }
                if (util.Utility.compare(current.data, next.data) == 1 && prev != null){
                    Node aux = null;
                    aux = next;
                    current.next = next.next;
                    aux.next = current;
                    prev.next = aux;

                    //reiniciar punteros
                    prev = null;
                    current = first;
                    next = current.next;
                    continue;
                }
                prev = current;
                current = prev.next;
                if (current.next!=null) {
                    next = current.next;
                }else next=null;
            }
        }
    }

    @Override
    public int indexOf(Object element) throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        int index = 1; //el primer indice de la lista es 1
        while(aux!=null){
            if(util.Utility.compare(aux.data, element)==0)
                return index;
            index++;
            aux = aux.next;
        }
        return -1; //significa q el elemento no existe en la lista
    }

    @Override
    public Object getFirst() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        else {
            Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
            while(aux.next!=null){
                aux = aux.next; //mueve aux al nodo sgte
            }
            return aux.data;
        }
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        return null;
    }

    @Override
    public Object getNext(Object element) throws ListException {
        return null;
    }

    @Override
    public Node getNode(int index) throws ListException {
        if(isEmpty())
            throw new ListException("Singly Linked List is empty");
        Node aux = first;
        int i = 1; //posicion del primer nodo
        while(aux!=null){
            if(util.Utility.compare(i, index)==0){
                return aux;
            }
            i++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        return null;
    }
    public Node getNode(Object element) throws ListException {
        if(isEmpty()){
            throw new ListException("Singly Linked List is Empty");
        }
        Node aux = first;
        while(aux!=null){
            if(util.Utility.compare(aux.data, element)==0) {  //ya encontro el elemento
                return aux;
            }
            aux = aux.next; //muevo aux al sgte nodo
        }
        return null; //si llega aqui es xq no encontro el index
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Singly Linked List is empty";
        String result = "Singly Linked List Content\n";
        Node aux = first; //aux para moverme por la lista y no perder el puntero al inicio
        while(aux!=null){
            result+=aux.data+"\n";
            aux = aux.next; //lo muevo al sgte nodo
        }
        return result;
    }
    public <T> java.util.List<T> toTypedList() throws ListException {
        java.util.List<T> list = new java.util.ArrayList<>();
        if (isEmpty()) return list;
        Node aux = first;
        while (aux != null) {
            if (aux!=null)
                list.add((T) aux.data);
            aux = aux.next;
        }
        return list;
    }

}
