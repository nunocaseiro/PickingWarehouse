package ipleiria.estg.dei.ei.model.search;


import java.util.Objects;

// Note: this class has a natural ordering that is inconsistent with equals.
public class Node implements Comparable<Node> {

    private Node parent;
    private double f; // g+h
    private double g; // cost of the path from the start node
    private double costFromAdjacentNode; // distance from the adjacent node to this one
    private int line;
    private int column;
    private int nodeNumber;

    public Node(int line, int column, int nodeNumber) {
        this.line = line;
        this.column = column;
        this.nodeNumber = nodeNumber;
    }

    public Node(double costFromAdjacentNode, int line, int column, int nodeNumber) {
        this.costFromAdjacentNode = costFromAdjacentNode;
        this.line = line;
        this.column = column;
        this.nodeNumber = nodeNumber;
    }

    public Node(Node parent, double f, double g, int line, int column, int nodeNumber) {
        this.parent = parent;
        this.f = f;
        this.g = g;
        this.line = line;
        this.column = column;
        this.nodeNumber = nodeNumber;
    }

    public Node getParent() {
        return parent;
    }

    public double getF() {
        return f;
    }

    public double getG() {
        return g;
    }

    public double getCostFromAdjacentNode() {
        return costFromAdjacentNode;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public int compareTo(Node o) {
        return (this.f < o.f) ? -1 : (f == o.f) ? 0 : 1;
    }

    @Override
    public String toString() {
        return nodeNumber + " -> " + line + "-" + column + "/" + costFromAdjacentNode;
    }
}
