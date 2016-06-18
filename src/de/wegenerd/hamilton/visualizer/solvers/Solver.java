package de.wegenerd.hamilton.visualizer.solvers;

import de.wegenerd.hamilton.visualizer.Node;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigInteger;

public abstract class Solver extends Thread {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setStartNode(Node startNode) {

    }

    public void setEndNode(Node endNode) {

    }

    protected void publishResult(BigInteger value) {
        this.pcs.firePropertyChange("result", null, value);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
         pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         pcs.removePropertyChangeListener(listener);
     }
}
