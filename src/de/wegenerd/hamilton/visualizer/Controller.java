package de.wegenerd.hamilton.visualizer;

import de.wegenerd.hamilton.visualizer.algorithms.SimpleAlgorithm;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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
    private long SOLVE_DELAY = 100;
    public static long MIN_SOLVE_DELAY = 0;
    private Solver currentSolver;

    @FXML
    public Label resultLabel;
    @FXML
    public Label stepsLabel;
    @FXML
    public Slider delaySlider;
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<Solver> algorithmTable;
    @FXML
    public TableView graphTable;

    private GraphicsContext gc;
    private BigInteger result;
    private BigInteger steps;
    private double sizeFactor;
    private GraphFile currentGraph;
    private Notification notification;

    public long getSolveDelay() {
        if (currentSolver != null && currentSolver.isStopping()) {
            return 0;
        }
        return SOLVE_DELAY;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            updateSizeFactor();
        });
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            updateSizeFactor();
        });
        gc = canvas.getGraphicsContext2D();

        delaySlider.setMax(MAX_SOLVE_DELAY);
        delaySlider.setValue(getSolveDelay());
        delaySlider.setMin(MIN_SOLVE_DELAY);
        delaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SOLVE_DELAY = newValue.longValue();
        });

        ObservableList<Solver> algorithmData = FXCollections.observableArrayList(
                new Solver(new SimpleAlgorithm())
        );
        algorithmTable.setItems(algorithmData);
        TableColumn algorithmName = new TableColumn<Solver, String>("Algorithm");
        algorithmName.setCellValueFactory(new PropertyValueFactory("algorithmName"));
        algorithmName.setPrefWidth(200);
        algorithmTable.getColumns().addAll(algorithmName);

        notification = new Notification(this);
        loadGraphFileList();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        }.start();
    }

    private void updateSizeFactor() {
        double maxX = 0;
        double maxY = 0;
        sizeFactor = 1;
        for (Node node : Node.getAll()) {
            maxX = Math.max(node.getX(), maxX);
            maxY = Math.max(node.getY(), maxY);
        }
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double bottomPadding = 60;
        double rightPadding = 230;
        sizeFactor = Math.min(
                -2 * (rightPadding - w) / (2 * maxX + 1),
                -2 * (bottomPadding - h) / (2 * maxY + 1)
        );

    }

    private void loadGraphFileList() {
        graphTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stopAlgorithm();
                loadGraph((GraphFile) newValue);
            }
        });
        ObservableList<GraphFile> graphData = FXCollections.observableArrayList();

        URL graphFolderUrl = getClass().getResource("./graphs/plaindot/");
        if (graphFolderUrl == null) {
            System.err.println("Could not load graph files");
            return;
        }
        File folder = null;
        try {
            folder = new File(graphFolderUrl.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.err.println("Could not load graph files");
            return;
        }
        for (File file : listOfFiles) {
            if (file.isFile()) {
                graphData.add(new GraphFile(file));
            }
        }
        graphTable.setItems(graphData);
        TableColumn graphName = new TableColumn<GraphFile, String>("Graph");
        graphName.setCellValueFactory(new PropertyValueFactory("graphName"));
        graphName.setPrefWidth(200);
        graphTable.getColumns().addAll(graphName);

        graphTable.getSelectionModel().selectFirst();
        algorithmTable.getSelectionModel().selectFirst();
    }

    private void loadGraph(GraphFile graphFile) {
        notification.hide();
        File file = graphFile.getFile();
        if (file == null) {
            return;
        }
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines == null) {
            return;
        }
        Node.reset();
        Edge.reset();
        for (String line : lines) {
            final String[] data = line.split(" ");
            if (data[0].equals("node")) {
                Node node = Node.create(data[1], data[2], data[3], this);
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
        currentGraph = graphFile;
        updateSizeFactor();
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
        gc.setFont(Font.font(18));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BOTTOM);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.fillRect(10, canvas.getHeight(), 4, 10);
        gc.fillText("Steps: " + getSteps() + "\nResult: " + getResult(), 10, canvas.getHeight());
        notification.draw(gc);
    }

    public void runAlgorithm(ActionEvent event) {
        final Solver solver = algorithmTable.getSelectionModel().getSelectedItem();
        if (solver == null) {
            return;
        }
        stopAlgorithm();
        Node startNode = Node.getStartNode();
        Node endNode = Node.getEndNode();
        if (currentSolver != null) {
            while (currentSolver.isSolving()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        currentSolver = solver;
        setResult(BigInteger.ZERO);
        setSteps(BigInteger.ZERO);
        notification.hide();
        solver.start(startNode, endNode, evt -> {
            result = (BigInteger) evt.getNewValue();
            BigInteger solution = currentGraph.getSolution();
            if (result.equals(solution)) {
                notification.show("Correct solution!", NotificationType.SUCCESS);
            } else {
                notification.show("Wrong solution! Expected " + solution, NotificationType.ERROR);
            }
        });
    }

    public void stopAlgorithm() {
        if (currentSolver != null) {
            currentSolver.stop();
        }
    }

    public BigInteger getResult() {
        return result;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }

    public void setSteps(BigInteger steps) {
        this.steps = steps;
    }

    public void addStep() {
        setSteps(getSteps().add(BigInteger.ONE));
    }

    public BigInteger getSteps() {
        return steps;
    }

    public void addResult() {
        setResult(getResult().add(BigInteger.ONE));
    }

    public double getSizeFactor() {
        return sizeFactor;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
