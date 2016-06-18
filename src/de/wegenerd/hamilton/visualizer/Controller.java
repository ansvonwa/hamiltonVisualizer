package de.wegenerd.hamilton.visualizer;

import de.wegenerd.hamilton.visualizer.solvers.SimpleSolver;
import de.wegenerd.hamilton.visualizer.solvers.Solver;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    public static double SIZE_FACTOR = 96;
    public static long MAX_SOLVE_DELAY = 300;
    public static long SOLVE_DELAY = 100;
    public static long MIN_SOLVE_DELAY = 0;

    @FXML
    public Slider delaySlider;
    @FXML
    private Canvas canvas;

    @FXML
    private StackPane stackPane;

    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        loadGraph("graph06a");
        gc = canvas.getGraphicsContext2D();

        delaySlider.setMax(MAX_SOLVE_DELAY);
        delaySlider.setValue(SOLVE_DELAY);
        delaySlider.setMin(MIN_SOLVE_DELAY);
        delaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SOLVE_DELAY = newValue.longValue();
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        }.start();
        Node startNode = Node.getStartNode();
        Node endNode = Node.getEndNode();
        Solver solver = new SimpleSolver();
        solver.setStartNode(startNode);
        solver.setEndNode(endNode);
        solver.addPropertyChangeListener(evt -> {
            System.out.println(evt.getPropertyName());
            System.out.println("Number of hamiltonian paths: " + evt.getNewValue());
        });
        solver.start();
    }

    private void loadGraph(String graphName) {
        URL graphUrl = getClass().getResource("./graphs/plaindot/" + graphName + ".dot.txt");
        if (graphUrl == null) {
            System.err.println("No such graph '" + graphName + "'");
            return;
        }
        File graphFile = null;
        try {
            graphFile = new File(graphUrl.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (graphFile == null) {
            return;
        }
        List<String> lines = null;
        try {
            lines = Files.readAllLines(graphFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines == null) {
            return;
        }
        for (String line : lines) {
            final String[] data = line.split(" ");
            if (data[0].equals("node")) {
                Node node = Node.create(data[1], data[2], data[3]);
                if (data[10].equals("green")) {
                    node.setStartNode(true);
                } else if (data[10].equals("yellow")) {
                    node.setEndNode(true);
                }
            } else if (data[0].equals("edge")) {
                Node from = Node.get(data[1]);
                Node to = Node.get(data[2]);
                if (from == null || to == null) {
                    System.err.println("Edge definition with non existing node. Was the edge defined before the node?");
                    continue;
                }
                Edge edge = from.connectTo(to);
                ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data));
                int n = new Integer(data[3]);
                final List<String> edgeCoordinates = dataList.subList(4, n * 2);
                edge.setCoordinates(edgeCoordinates);
            }
        }

    }

    private double getWidth() {
        return canvas.getWidth();
    }

    private double getHeight() {
        return canvas.getHeight();
    }

    private void draw() {
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, getWidth(), getHeight());
        for (Edge edge : Edge.getAll()) {
            edge.draw(gc);
        }
        for (Node node : Node.getAll()) {
            node.draw(gc);
        }
    }
}
