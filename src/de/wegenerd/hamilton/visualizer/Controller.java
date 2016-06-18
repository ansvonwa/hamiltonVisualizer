package de.wegenerd.hamilton.visualizer;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane stackPane;

    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        loadGraph("graph44b");
        gc = canvas.getGraphicsContext2D();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        }.start();
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
//        String firstLine = lines.remove(0);
//        Node startNode = Node.create(firstLine.split(" ")[0]);
//        Node endNode = Node.create(firstLine.split(" ")[1]);
        for (String line : lines) {
            final String[] data = line.split(" ");
            if (data[0].equals("node")) {
                Node node = Node.create(data[1], data[2], data[3]);
                if (data[10].equals("green")) {
                    node.setStartNode(true);
                } else if (data[10].equals("yellow")) {
                    node.setEndNode(true);
                }
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
        for (Node node : Node.getAll()) {
            node.draw(gc);
        }

    }
}
