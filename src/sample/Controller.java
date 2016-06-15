package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
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
//        AnchorPane.setTopAnchor(bidView, DEFAULT_PADDING);
//		AnchorPane.setBottomAnchor(bidView, DEFAULT_PADDING);
//		AnchorPane.setRightAnchor(bidView, DEFAULT_PADDING);
        System.out.println(stackPane.widthProperty());
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());
        new AnimationTimer() {
			@Override
			public void handle(long now) {
				draw();
			}
		}.start();
        gc = canvas.getGraphicsContext2D();

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
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
}
