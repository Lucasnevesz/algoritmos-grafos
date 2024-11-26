package com.grafo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private Grafos graph;

    public Dijkstra(Grafos graph) {
        this.graph = graph;
    }

    public double[] shortestPath(int source) {
        int n = graph.getVerticesCount();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(v -> dist[v]));
        pq.add(source);

        while (!pq.isEmpty()) {
            int u = pq.poll();

            for (Grafos.Edge neighbor : graph.getEdgesFrom(u)) {
                int v = neighbor.getTo();
                double weight = neighbor.getWeight();

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(v);
                }
            }
        }

        return dist;
    }
}
