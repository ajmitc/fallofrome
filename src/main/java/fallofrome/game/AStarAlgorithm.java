package fallofrome.game;

import fallofrome.game.board.Area;
import fallofrome.game.board.AreaConnection;
import fallofrome.game.board.Board;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AStarAlgorithm {
    public static List<AreaConnection> findShortestPath(Area start, Area end, Board board){
        Map<Area, Node> locationNodes = new HashMap<>();
        board.getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(l -> locationNodes.put(l, new Node(l)));
        });

        // Initialize the open list
        Queue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.f < o2.f? 1: (o1.f > o2.f? -1: 0);
            }
        });
        // Initialize the closed list (visited)
        List<Node> visited = new ArrayList<>();

        // put the starting node on the open list (you can leave its f at zero)
        Node startNode = new Node(start);
        startNode.f = 0;
        openList.add(startNode);

        // while the open list is not empty
        while (!openList.isEmpty()) {
            Node current = openList.remove();

            if (!visited.contains(current)){
                visited.add(current);

                if (current.location == end)
                    return reconstructPath(startNode, current, board);

                List<Node> neighbors =
                        board.getConnections().stream()
                                .filter(r -> r.hasArea(current.location))
                                .map(r -> r.getFromArea() != current.location? locationNodes.get(r.getFromArea()): locationNodes.get(r.getToArea()))
                                .collect(Collectors.toList());

                for (Node neighbor : neighbors) {
                    if (!visited.contains(neighbor)){
                        // increment hops from start
                        neighbor.hopsFromStart = current.hopsFromStart + 1;

                        // calculate predicted distance to the end node
                        int predictedDistance = calcEstimatedCostToMove(neighbor.location, end, board);

                        // calculate distance to neighbor. 2. calculate dist from start node
                        int totalDistance = calcActualCostToMove(startNode, neighbor) + predictedDistance;

                        // update n's distance
                        neighbor.f = totalDistance;

                        // if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
                        Optional<Node> betterNode = openList.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
                        betterNode = visited.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // otherwise, add  the node to the open list
                        neighbor.parent = current;
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static List<AreaConnection> reconstructPath(Node startNode, Node endNode, Board board){
        List<AreaConnection> path = new ArrayList<>();
        Node current = endNode;
        while (current.location != startNode.location){
            Node parent = current.parent;
            final Area currentLocation = current.location;
            AreaConnection road = board.getConnections().stream().filter(r -> r.hasArea(currentLocation) && r.hasArea(parent.location)).findFirst().get();
            path.add(road);
            current = parent;
        }
        Collections.reverse(path);
        return path;
    }

    // Return g
    private static int calcActualCostToMove(Node location1, Node location2){
        int cost = location2.hopsFromStart - location1.hopsFromStart;
        return cost;
    }

    // Return h
    private static int calcEstimatedCostToMove(Area location1, Area location2, Board board){
        if (location1 == location2)
            return 0;
        Optional<AreaConnection> connection = board.getConnections().stream().filter(r -> r.hasArea(location1) && r.hasArea(location2)).findAny();
        if (connection.isPresent()){
            return connection.get().getMovementCost();
        }
        return 10;
    }

    static class Node{
        public Area location;
        public Node parent = null;
        public int hopsFromStart = 0;
        public int f = Integer.MAX_VALUE;

        public Node(Area l){
            this.location = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }
}
