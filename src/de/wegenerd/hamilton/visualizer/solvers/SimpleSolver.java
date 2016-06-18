package de.wegenerd.hamilton.visualizer.solvers;

import de.wegenerd.hamilton.visualizer.Node;

import java.math.BigInteger;
import java.util.ArrayList;

public class SimpleSolver extends Solver {

    private Node endNode;
    private Node startNode;
    private ArrayList<Node> visitedNodes;
    private int delay;

    public void run() {
        visitedNodes = new ArrayList<>();
        startNode.setHighlight(true);
        visitedNodes.add(startNode);
        BigInteger result = solve(startNode.getNeighbours());
        publishResult(result);
    }

    private BigInteger solve(ArrayList<Node> neighbours) {
        BigInteger result = BigInteger.ZERO;
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

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
