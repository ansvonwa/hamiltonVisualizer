package de.wegenerd.hamilton.visualizer.algorithms;

import de.wegenerd.hamilton.visualizer.Node;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigInteger;

public abstract class Algorithm extends Thread {
    protected boolean cancel;
    protected Node endNode;
    protected Node startNode;

    private StringProperty algorithmName;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    protected void publishResult(BigInteger value) {
        this.pcs.firePropertyChange("result", null, value);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void cancel() {
        cancel = true;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public abstract String getAlgorithmName();

    public StringProperty algorithmNameProperty() {
        if (algorithmName == null) {
            algorithmName = new SimpleStringProperty(this, "algorithmName", getAlgorithmName());
        }
        return algorithmName;
    }
}
