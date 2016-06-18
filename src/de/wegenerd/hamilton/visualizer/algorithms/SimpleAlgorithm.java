package de.wegenerd.hamilton.visualizer.algorithms;

import de.wegenerd.hamilton.visualizer.Node;
import de.wegenerd.hamilton.visualizer.Solver;
import javafx.beans.property.StringProperty;

import java.math.BigInteger;
import java.util.ArrayList;

public class SimpleAlgorithm extends Algorithm {

    private ArrayList<Node> visitedNodes;

    public void run() {
        visitedNodes = new ArrayList<>();
        startNode.setHighlight(true);
        visitedNodes.add(startNode);
        BigInteger result = solve(startNode.getNeighbours());
        publishResult(result);
    }

    private BigInteger solve(ArrayList<Node> neighbours) {
        BigInteger result = BigInteger.ZERO;
        if (cancel) {
            return result;
        }
        for (Node node : neighbours) {
            if (visitedNodes.contains(node)) {
                continue;
            }
            if (node.equals(endNode)) {
                node.setHighlight(true);
                node.setHighlight(false);
                if (visitedNodes.size() + 1 == Node.getAll().size()) {
                    return BigInteger.ONE;
                }
                continue;
            }
            visitedNodes.add(node);
            node.setHighlight(true);
            result = result.add(solve(node.getNeighbours()));
            node.setHighlight(false);
            visitedNodes.remove(node);
        }
        return result;

    }

    @Override
    public String getAlgorithmName() {
        return "Simple brute force";
    }
}
