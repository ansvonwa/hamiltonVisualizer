package de.wegenerd.hamilton.visualizer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    private Node from;
    private Node to;
    private boolean highlight;
    private static ArrayList<Edge> edgeList = new ArrayList<>();
    private List<Double> coordinates;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        Edge.edgeList.add(this);
    }

    public static ArrayList<Edge> getAll() {
        return edgeList;
    }

    public static void resetHighlighting() {
        for (Edge edge : edgeList) {
            edge.setHighlight(false);
        }
    }

    public static void reset() {
        edgeList = new ArrayList<>();
    }

    public void draw(GraphicsContext gc) {
        if (isHighlight()) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
        } else {
            gc.setStroke(Color.DARKGREY);
            gc.setLineWidth(1);
        }
        double x1 = from.getX() + from.getWidth() / 2;
        double y1 = from.getY() + from.getHeight() / 2;
        double x2 = to.getX() + to.getWidth() / 2;
        double y2 = to.getY() + to.getHeight() / 2;
        gc.strokeLine(x1, y1, x2, y2);
//        Double[] x = {null, null};
//        Double[] y = {null, null};
//        int i = 0;
//        int xy = 0;
//        for (Double coordinate : coordinates) {
//            if (xy == 0) {
//                x[i] = coordinate;
//                xy = 1;
//            } else {
//                y[i] = coordinate;
//                xy = 0;
//                i++;
//                i %= 2;
//                if (y[1] != null) {
//                    gc.strokeLine(x[0], y[0], x[1], y[1]);
//                }
//            }
//        }
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = new ArrayList<>();
        for (String coordinate : coordinates) {
            // TODO: something is wrong here with the scaling
            this.coordinates.add(new Double(coordinate) * Controller.SIZE_FACTOR);
        }
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
