package model.tda.graph;

public class Edge {
    private Vertex destination;
    private Object weight; // Peso de la arista

    public Edge(Vertex destination, Object weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Object getWeight() {
        return weight;
    }
}
