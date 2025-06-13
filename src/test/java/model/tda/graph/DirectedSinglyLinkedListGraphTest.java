package model.tda.graph;

import model.tda.ListException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectedSinglyLinkedListGraphTest {

    @Test
    void test(){
        DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();

// Agregar vértices
        try {
            graph.addVertex("A");
            graph.addVertex("B");
            graph.addVertex("C");

// Agregar aristas con pesos
            graph.addEdgeWeight("A", "B", 10);
            graph.addEdgeWeight("A", "C", 15);
            graph.addEdgeWeight("B", "C", 5);

            int weight = graph.getEdgeWeight("A", "B");
            System.out.println("El peso entre A y B es: " + weight); // Debería imprimir "10"

        } catch (GraphException | ListException e) {
            System.out.println(e.getMessage());
        }
    }

}