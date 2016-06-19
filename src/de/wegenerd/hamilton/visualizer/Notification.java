package de.wegenerd.hamilton.visualizer;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Notification {
    private boolean visible = false;
    private String notificationText;
    private NotificationType notificationType;
    private double height = 50;
    private double width = 800;
    private double y = 25;
    private double x = 0;
    private Controller controller;

    Notification(Controller controller) {
        this.controller = controller;
    }

    public void show(String notificationText, NotificationType notificationType) {
        this.notificationText = notificationText;
        this.notificationType = notificationType;
        visible = true;
    }

    public void draw(GraphicsContext gc) {
        if (!visible) {
            return;
        }
        switch (notificationType) {
            case SUCCESS:
                gc.setFill(Color.GREEN);
                break;
            case ERROR:
                gc.setFill(Color.RED);
        }
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(25));
        gc.fillText(notificationText, getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return controller.getCanvas().getWidth() - 230;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void hide() {
        visible = false;
    }
}
