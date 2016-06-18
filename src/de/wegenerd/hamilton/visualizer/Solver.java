package de.wegenerd.hamilton.visualizer;

import de.wegenerd.hamilton.visualizer.algorithms.Algorithm;
import de.wegenerd.hamilton.visualizer.algorithms.SimpleAlgorithm;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeListener;

public class Solver {

    private Algorithm algorithm;
    private boolean isStopping;

    Solver(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public StringProperty algorithmNameProperty() {
        return algorithm.algorithmNameProperty();
    }

    public void stop() {
        isStopping = true;
        algorithm.cancel();
    }

        public void run() {

    }

    public boolean isSolving() {
        if (algorithm != null) {
            return algorithm.isAlive();
        }
        return false;
    }

    public void start(Node startNode, Node endNode, PropertyChangeListener callback) {
        if (algorithm == null || algorithm.getState() == Thread.State.TERMINATED) {
            try {
                algorithm = algorithm.getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
//        Node.resetHighlighting();
        Edge.resetHighlighting();
        algorithm.setStartNode(startNode);
        algorithm.setEndNode(endNode);
        algorithm.addPropertyChangeListener(callback);
        isStopping = false;
        algorithm.start();
//        else {
//            System.err.println("Could not start new algorithm, is an old one still running?");
//        }
    }

    public boolean isStopping() {
        return isStopping;
    }
}
