package de.wegenerd.hamilton.visualizer;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private static ArrayList<Node> highlightedNodes = new ArrayList<>();
    private static Controller controller;
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

    public static Node create(int id, double x, double y, Controller controller) {
        Node.controller = controller;
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

    public static void resetHighlighting() {
        highlightedNodes = new ArrayList<>();
        for (Node node : nodeList) {
            node.setHighlight(false);
        }

    }

    public static Node get(int id) {
        return nodeMap.get(id);
    }

    public static Node get(String id) {
        return Node.get(new Integer(id));
    }

    public static Node create(String id, String x, String y, Controller controller) {
        return Node.create(new Integer(id), new Double(x), new Double(y), controller);
    }

    public static void reset() {
        nodeList = new ArrayList<>();
        nodeMap = new HashMap<>();
    }

    public double getX() {
        return x * controller.getSizeFactor();
    }

    public double getY() {
        return y * controller.getSizeFactor();
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(1);
        if (isStartNode()) {
            gc.setFill(Color.LIGHTGREEN);
        } else if (isEndNode()) {
            if (isHighlight()) {
                gc.setFill(Color.CORNFLOWERBLUE);
            } else {
                gc.setFill(Color.LIGHTBLUE);
            }
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
        gc.setFont(Font.font(controller.getSizeFactor() * 0.2));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(Integer.toString(id), getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public double getHeight() {
        return controller.getSizeFactor() * 0.5;
    }

    public double getWidth() {
        return controller.getSizeFactor() * 0.5;
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
        if (highlight) {
            controller.addStep();
        }
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
            if (isEndNode() && Node.allHighlighted()) {
                controller.addResult();
            }
        } else {
            for (Edge edge : edges) {
                edge.setHighlight(false);
            }
            highlightedNodes.remove(this);
        }
        try {
            Thread.sleep(controller.getSolveDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean allHighlighted() {
        for (Node node : nodeList) {
            if (!node.isHighlight()) {
                return false;
            }
        }
        return true;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public int getId() {
        return id;
    }
}
