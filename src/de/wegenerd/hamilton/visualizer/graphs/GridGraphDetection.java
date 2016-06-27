package de.wegenerd.hamilton.visualizer.graphs;

import de.wegenerd.hamilton.visualizer.Node;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by anselm on 21.06.16.
 */
public class GridGraphDetection {
    private HashMap<Node, Point> locations = new HashMap<>();

    public GridGraphDetection(java.util.List<Node> nodes) {
        nodes.sort((a, b) -> a.getId() - b.getId());
        placeRec(new ArrayList<>(nodes), nodes.get(0), new Point(0, 0));
    }

    public void placeRec(List<Node> nodes, Node n, Point p) {
        if (!nodes.contains(n))
            return;
        locations.put(n, p);
        nodes.remove(n);
        n.getNeighbours().stream()
                .filter(nei -> nei.getId() == n.getId() + 1).findFirst()
                .ifPresent(nei -> placeRec(nodes, nei, new Point(p.x, p.y - 1)));
        n.getNeighbours().stream()
                .filter(nei -> nei.getId() == n.getId() - 1).findFirst()
                .ifPresent(nei -> placeRec(nodes, nei, new Point(p.x, p.y + 1)));
        n.getNeighbours().stream()
                .filter(nei -> nei.getId() < n.getId() - 1).findFirst()
                .ifPresent(nei -> placeRec(nodes, nei, new Point(p.x - 1, p.y)));
        n.getNeighbours().stream()
                .filter(nei -> nei.getId() > n.getId() + 1).findFirst()
                .ifPresent(nei -> placeRec(nodes, nei, new Point(p.x + 1, p.y)));
    }

    public void setNodePlaces() {
        Point upperLeft = new Point(locations.get(Node.get(0)));
        for (Point p : locations.values()) {
            if (p.x < upperLeft.x)
                upperLeft.x = p.x;
            if (p.y < upperLeft.y)
                upperLeft.y = p.y;
        }
        for (Node n : locations.keySet()) {
            n.setX(locations.get(n).x - upperLeft.x);
            n.setY(locations.get(n).y - upperLeft.y);
        }
    }
}
