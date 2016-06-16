package de.wegenerd.hamilton.visualizer;

import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

public class Node {
    private static double HEIGHT = 10;
    private static double WIDTH = 10;
    private int id;
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();

    private Node(int id) {
        this.id = id;
    }

    public static Node create(int id) {
        Node node = nodeMap.get(id);
        if (node == null) {
            node = new Node(id);
            nodeMap.put(id, node);
        }
        return node;
    }

    public static Node create(String id) {
        return Node.create(new Integer(id));
    }

    public double getX() {
        return 0;
    }

    public double getY() {
        return 0;
    }

    public void draw(GraphicsContext gc) {
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    public double getHeight() {
        return Node.HEIGHT;
    }

    public double getWidth() {
        return Node.WIDTH;
    }
}
