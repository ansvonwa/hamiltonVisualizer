package de.wegenerd.hamilton.visualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getResource("ui.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.setTitle("Hamilton Visualizer");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    @Override
    public void stop() {
        Controller controller = fxmlLoader.getController();
        controller.stopAlgorithm();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
