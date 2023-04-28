package project_1;

import java.util.*;

public class RailwayGraph {
    private Map<RailwayStation, Map<RailwayStation, Integer>> adjacencyList;
    public RailwayGraph() {
        this.adjacencyList = new HashMap<>();
    }
    public void addEdge(RailwayStation source, RailwayStation destination, int distance) {
        adjacencyList.putIfAbsent(source, new HashMap<>());
        adjacencyList.putIfAbsent(destination, new HashMap<>());
        adjacencyList.get(source).put(destination, distance);
        adjacencyList.get(destination).put(source, distance);
    }
    public void addStation(RailwayStation station) {
        adjacencyList.putIfAbsent(station, new HashMap<>());
    }
    public List<RailwayStation> generateRoute(RailwayStation source, RailwayStation destination) {
        if (!adjacencyList.containsKey(source) || !adjacencyList.containsKey(destination)) {
            return new ArrayList<>();
        }
        if (source.equals(destination)) {
            return List.of(source);
        }
        Map<RailwayStation, Integer> distances = new HashMap<>();
        Map<RailwayStation, Integer> finalDistances = distances;
        PriorityQueue<RailwayStation> pq = new PriorityQueue<>(Comparator.comparingInt(s -> finalDistances.get(s)));
        Map<RailwayStation, RailwayStation> previous = new HashMap<>();
        for (RailwayStation station : adjacencyList.keySet()) {
            if (station.equals(source)) {
                distances.put(station, 0);
            } else {
                distances.put(station, Integer.MAX_VALUE);
            }
            pq.offer(station);
        }
        boolean destinationFound = false;
        while (!pq.isEmpty() && !destinationFound) {
            RailwayStation current = pq.poll();
            int currentDistance = distances.get(current);

            for (Map.Entry<RailwayStation, Integer> entry : adjacencyList.get(current).entrySet()) {
                RailwayStation neighbor = entry.getKey();
                int edgeWeight = entry.getValue();
                int newDistance = currentDistance + edgeWeight;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    pq.remove(neighbor);
                    pq.offer(neighbor);
                }

                if (neighbor.equals(destination)) {
                    destinationFound = true;
                    break;
                }
            }
        }
        List<RailwayStation> route = new ArrayList<>();
        RailwayStation current = destination;
        while (current != null) {
            route.add(current);
            current = previous.get(current);
        }
        Collections.reverse(route);
        return route;
    }
    public int calculateTotalDistance(List<RailwayStation> route) {
        int totalDistance = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            RailwayStation current = route.get(i);
            RailwayStation next = route.get(i + 1);
            int distance = adjacencyList.get(current).get(next);
            totalDistance += distance;
        }

        return totalDistance;
    }
    public int calculateDistanceBetweenStations(RailwayStation station1, RailwayStation station2) {
        if (adjacencyList.containsKey(station1) && adjacencyList.get(station1).containsKey(station2)) {
            return adjacencyList.get(station1).get(station2);
        } else {
            throw new IllegalArgumentException("The given stations are not directly connected.");
        }
    }
    public Set<RailwayStation> getStations() {
        return adjacencyList.keySet();
    }
}

