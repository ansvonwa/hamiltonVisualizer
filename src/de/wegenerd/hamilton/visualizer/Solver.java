package de.wegenerd.hamilton.visualizer;

import java.util.ArrayList;

public class Solver extends Thread {

    private Node endNode;
    private Node startNode;
    private ArrayList<Node> visitedNodes;
    private int delay;

    public void run() {
        visitedNodes = new ArrayList<>();
        startNode.setHighlight(true);
        visitedNodes.add(startNode);
        long result = solve(startNode.getNeighbours());
        System.out.println("Number of hamiltonian paths: " + result);
    }

    private long solve(ArrayList<Node> neighbours) {
        long result = 0;
        for (Node node : neighbours) {
            if (visitedNodes.contains(node)) {
                continue;
            }
            if (node.equals(endNode)) {
                if (visitedNodes.size() + 1 == Node.getAll().size()) {
                    return 1;
                }
                continue;
            }
            visitedNodes.add(node);
            node.setHighlight(true);
            result += solve(node.getNeighbours());
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
