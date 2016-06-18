package de.wegenerd.hamilton.visualizer;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private static double HEIGHT = Controller.SIZE_FACTOR * 0.5;
    private static double WIDTH = Controller.SIZE_FACTOR * 0.5;
    private static ArrayList<Node> highlightedNodes = new ArrayList<>();
    private int id;
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();
    private static ArrayList<Node> nodeList = new ArrayList<>();
    private double x;
    private double y;
    private boolean startNode;
    private boolean endNode;
    private ArrayList<Edge> edges = new ArrayList<>();
    private boolean highlight;

    private Node(int id) {
        this.id = id;
    }

    public static Node create(int id, double x, double y) {
        Node node = nodeMap.get(id);
        if (node == null) {
            node = new Node(id);
            nodeMap.put(id, node);
            nodeList.add(node);
        }
        node.setX(x);
        node.setY(y);
        return node;
    }

    public static Node get(int id) {
        return nodeMap.get(id);
    }

    public static Node get(String id) {
        return Node.get(new Integer(id));
    }

    public static Node create(String id, String x, String y) {
        return Node.create(new Integer(id), new Double(x), new Double(y));
    }

    public double getX() {
        return x * Controller.SIZE_FACTOR;
    }

    public double getY() {
        return y * Controller.SIZE_FACTOR;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(1);
        if (isStartNode()) {
            gc.setFill(Color.LIGHTGREEN);
        } else if (isEndNode()) {
            gc.setFill(Color.LIGHTBLUE);
        } else if (isHighlight()) {
            gc.setFill(Color.ORANGE);
            gc.setLineWidth(2);
        } else {
            gc.setFill(Color.WHITESMOKE);
        }
        gc.setStroke(Color.BLACK);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
        gc.strokeOval(getX(), getY(), getWidth(), getHeight());
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(Integer.toString(id), getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public double getHeight() {
        return Node.HEIGHT;
    }

    public double getWidth() {
        return Node.WIDTH;
    }

    public static ArrayList<Node> getAll() {
        return nodeList;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isStartNode() {
        return startNode;
    }

    public void setStartNode(boolean startNode) {
        if (startNode) {
            endNode = false;
        }
        this.startNode = startNode;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public void setEndNode(boolean endNode) {
        if (endNode) {
            startNode = false;
        }
        this.endNode = endNode;
    }

    public Edge connectTo(Node to) {
        Edge edge = new Edge(this, to);
        edges.add(edge);
        to.edges.add(edge);
        return edge;
    }

    public static Node getStartNode() {
        for (Node node : nodeList) {
            if (node.isStartNode()) {
                return node;
            }
        }
        return null;
    }

    public static Node getEndNode() {
        for (Node node : nodeList) {
            if (node.isEndNode()) {
                return node;
            }
        }
        return null;
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> result = new ArrayList<>();
        for (Edge edge : edges) {
            final Node from = edge.getFrom();
            final Node to = edge.getTo();
            if (from.equals(this)) {
                result.add(to);
            } else if (to.equals(this)) {
                result.add(from);
            }
        }
        return result;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        Node lastHighlightedNode = null;
        if (highlightedNodes.size() > 0) {
            lastHighlightedNode = highlightedNodes.get(highlightedNodes.size() - 1);
        }
        if (highlight) {
            for (Edge edge : edges) {
                final Node from = edge.getFrom();
                final Node to = edge.getTo();
                if (from.equals(this) && to.equals(lastHighlightedNode) ||
                        to.equals(this) && from.equals(lastHighlightedNode)) {
                    edge.setHighlight(true);
                }
            }
            highlightedNodes.add(this);
        } else {
            for (Edge edge : edges) {
                edge.setHighlight(false);
            }
            highlightedNodes.remove(this);
        }
        try {
            Thread.sleep(Controller.SOLVE_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isHighlight() {
        return highlight;
    }
}
