package model.tda.graph;

import model.tda.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DirectedSinglyLinkedListGraph implements Graph {
    private SinglyLinkedList vertexList; //lista enlazada de vértices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public DirectedSinglyLinkedListGraph() {
        this.vertexList = new SinglyLinkedList();
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
    }

    @Override
    public int size() throws ListException {
        return vertexList.size();
    }

    @Override
    public void clear() {
        vertexList.clear();
    }

    @Override
    public boolean isEmpty() {
        return vertexList.isEmpty();
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        return indexOf(element)!=-1;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        int index = indexOf(a); //buscamos el índice del elemento en la lista enlazada
        if(index ==-1) return false;
        Vertex vertex = (Vertex) vertexList.getNode(index).data;
        return vertex!=null && !vertex.edgesList.isEmpty()
                && vertex.edgesList.contains(new EdgeWeight(b, null));
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(new Vertex(element)); //agrego un nuevo objeto vertice
        else if(!vertexList.contains(element))
            vertexList.add(new Vertex(element));
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        addRemoveVertexEdgeWeight(a, b, null, "addEdge"); //agrego la arista
    }

    private int indexOf(Object element) throws ListException {
        for(int i=1;i<=vertexList.size();i++){
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            if(util.Utility.compare(vertex.data, element)==0){
                return i; //encontro el vertice
            }
        }//for
        return -1; //significa q la data de todos los vertices no coinciden con element
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between the vertexes[" + a + "] y [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, weight, "addWeight"); //agrego la arista
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        if(!containsEdge(a, b)) {
            addRemoveVertexEdgeWeight(a, b, weight, "addEdge"); //agrego la arista
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        boolean removed = false;
        if(!vertexList.isEmpty() && containsVertex(element)){
            for (int i = 1; !removed&&i <= vertexList.size(); i++) {
                Vertex vertex = (Vertex) vertexList.getNode(i).data;
                if(util.Utility.compare(vertex.data, element)==0){ //ya lo encontro
                    vertexList.remove(new Vertex(element));
                    removed = true;
                    //ahora se debe eliminar la entrada de ese vertice de todas
                    //las listas de aristas de los otros vertices
                    int n = vertexList.size();
                    for (int j=1; vertexList!=null&&!vertexList.isEmpty()&&j<=n; j++) {
                        vertex = (Vertex) vertexList.getNode(j).data;
                        if(!vertex.edgesList.isEmpty())
                            addRemoveVertexEdgeWeight(vertex.data, element, null, "remove");
                    }
                }//if
            }//for i
        }//if
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("There's no some of the vertexes");
        addRemoveVertexEdgeWeight(a, b, null, "remove"); //suprimo la arista
    }

    private void addRemoveVertexEdgeWeight(Object a, Object b, Object weight, String action) throws ListException{
        for (int i = 1; i <= vertexList.size(); i++) {
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            if(util.Utility.compare(vertex.data, a)==0){
                switch(action){
                    case "addEdge":
                        vertex.edgesList.add(new EdgeWeight(b, weight));
                        break;
                    case "addWeight":
                        vertex.edgesList.getNode(new EdgeWeight(b, weight))
                                .setData(new EdgeWeight(b, weight));
                        break;
                    case "remove":
                        if(vertex.edgesList!=null&&!vertex.edgesList.isEmpty())
                            vertex.edgesList.remove(new EdgeWeight(b, weight));
                }
            }
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        stack.clear();
        stack.push(1); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertex = (Vertex)vertexList.getNode(index).data;
                vertex.setVisited(true); // lo marca
                info+=vertex+", ";
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }//dfs

    // Recorrido en amplitud
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        queue.clear();
        queue.enQueue(1); // encola el elemento
        int index2;
        while(!queue.isEmpty()){
            int index1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((index2=adjacentVertexNotVisited(index1)) != -1 ){
                // obtiene uno
                vertex = (Vertex)vertexList.getNode(index2).data;
                vertex.setVisited(true); //lo marco
                info+=vertex+", ";
                queue.enQueue(index2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) throws ListException {
        for (int i=1; i<=vertexList.size(); i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            vertex.setVisited(value); //value==true or false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Vertex vertex1 = (Vertex)vertexList.getNode(index).data;
        for(int i=1; i<=vertexList.size(); i++){
            Vertex vertex2 = (Vertex)vertexList.getNode(i).data;
            if(!vertex2.edgesList.isEmpty()&&vertex2.edgesList
                    .contains(new EdgeWeight(vertex1.data, null))
                    && !vertex2.isVisited())
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "Singly Linked List Graph Content...";
        try {
            for(int i=1; i<=vertexList.size(); i++){
                Vertex vertex = (Vertex)vertexList.getNode(i).data;
                result+="\nThe vertex in the position "+i+" is: "+vertex+"\n";
                if(!vertex.edgesList.isEmpty()){
                    result+="........EDGES AND WEIGHTS: "+vertex.edgesList+"\n";
                }//if

            }//for
        } catch (ListException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public Map<Object, Integer> dijkstra(Object source) throws GraphException, ListException {
        if (source == null || !containsVertex(source)) {
            throw new GraphException("El vértice fuente no existe");
        }

        // Mapa para almacenar la distancia mínima desde el origen a todos los vértices
        Map<Object, Integer> distances = new HashMap<>();
        // Mapa para rastrear cuáles vértices ya han sido visitados
        Map<Object, Boolean> visited = new HashMap<>();

        // Inicialización: poner todas las distancias a infinito y visitados como false
        for (int i = 0; i < vertexList.size(); i++) {
            Object vertex = vertexList.getNode(i).data;
            distances.put(vertex, Integer.MAX_VALUE); // "Infinito simbólico"
            visited.put(vertex, false);
        }

        // Distancia al vértice origen es 0
        distances.put(source, 0);

        // Loop principal de Dijkstra
        while (true) {
            // Obtener el vértice con la menor distancia que aún no ha sido visitado
            Object current = getMinDistanceVertex(distances, visited);
            if (current == null) break; // Si no hay más vértices por procesar, salimos

            // Marcar el vértice actual como visitado
            visited.put(current, true);

            // Actualizar las distancias para los vecinos del vértice actual
            for (int i = 0; i < vertexList.size(); i++) {
                Object neighbor = vertexList.getNode(i).data;

                // Sólo consideramos vecinos no visitados y con una arista válida
                if (!visited.get(neighbor) && containsEdge(current, neighbor)) {
                    int edgeWeight = getEdgeWeight(current, neighbor);
                    int newDistance = distances.get(current) + edgeWeight;

                    // Si encontramos una distancia más corta hacia el vecino, actualizamos
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                    }
                }
            }
        }

        return distances; // Retornamos el mapa de distancias mínimas desde el origen
    }

    /**
     * Obtiene el vértice con la menor distancia que aún no ha sido visitado.
     */
    private Object getMinDistanceVertex(Map<Object, Integer> distances, Map<Object, Boolean> visited) throws ListException {
        Object minVertex = null;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < vertexList.size(); i++) {
            Object vertex = vertexList.getNode(i).data;
            if (!visited.get(vertex) && distances.get(vertex) < minDistance) {
                minVertex = vertex;
                minDistance = distances.get(vertex);
            }
        }

        return minVertex;
    }

    public int getEdgeWeight(Object a, Object b) throws ListException {
        if (vertexList == null || vertexList.isEmpty()) {
            throw new ListException("El grafo está vacío");
        }

        // Buscar el vértice de origen en la lista de vértices
        if (!vertexList.contains(a)) {
            throw new ListException("El vértice de origen no existe");
        }

        // Obtener el nodo correspondiente al vértice de origen
        Node originNode = (Node) vertexList.getNode(a).data;

        // Obtener la lista de adyacencias de este vértice (aristas salientes)
        SinglyLinkedList adjacencyList = (SinglyLinkedList) originNode.data;

        // Validar que la lista de adyacencias no esté vacía
        if (adjacencyList == null || adjacencyList.isEmpty()) {
            throw new ListException("No hay aristas desde el vértice de origen");
        }

        // Buscar en la lista de adyacencias si hay una conexión al vértice destino
        for (int i = 0; i < adjacencyList.size(); i++) {
            // Obtener el nodo actual en la lista de adyacencias
            Node edgeNode = adjacencyList.getNode(i);

            // Verificar si el nodo actual representa una conexión al destino
            EdgeWeight edge = (EdgeWeight) edgeNode.data; // Suponiendo que `Edge` representa una arista
            if (util.Utility.compare(edge.getEdge(), b) == 0) {
                // Devolver el peso de la arista
                return (int) edge.getWeight();
            }
        }

        // Si no se encuentra una conexión al destino
        throw new ListException("No hay conexión desde el vértice de origen al destino");
    }


    public SinglyLinkedList getVertexList() {
        return vertexList;
    }
}