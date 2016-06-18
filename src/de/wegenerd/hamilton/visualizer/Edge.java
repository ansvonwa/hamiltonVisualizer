package de.wegenerd.hamilton.visualizer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Edge {
    private Node from;
    private Node to;
    private static ArrayList<Edge> edgeList = new ArrayList<>();

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        Edge.edgeList.add(this);
    }

    public static ArrayList<Edge> getAll() {
        return edgeList;
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        double x1 = from.getX() + from.getWidth() / 2;
        double y1 = from.getY() + from.getHeight() / 2;
        double x2 = to.getX() + to.getWidth() / 2;
        double y2 = to.getY() + to.getHeight() / 2;
        gc.strokeLine(x1, y1, x2, y2);
    }
}
