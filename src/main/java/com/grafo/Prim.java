package com.grafo;

import java.util.*;

public class Prim {
    private Grafos graph;
    private List<Grafos.Edge> agm;

    public Prim(Grafos graph) {
        this.graph = graph;
        this.agm = new ArrayList<>();
    }

    public void geraAGM() {
        int n = graph.getVerticesCount();
        boolean[] inMST = new boolean[n];
        double[] key = new double[n];
        int[] parent = new int[n];
        Arrays.fill(key, Double.MAX_VALUE);
        key[0] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(v -> key[v]));
        pq.add(0);

        while (!pq.isEmpty()) {
            int u = pq.poll();

            if (inMST[u]) continue;
            inMST[u] = true;

            for (Grafos.Edge neighbor : graph.getEdgesFrom(u)) {
                int v = neighbor.getTo();
                double weight = neighbor.getWeight();

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight; 
                    parent[v] = u;
                    pq.add(v); 

                }
            }
        }

        for (int i = 1; i < n; i++) {
            agm.add(new Grafos.Edge(parent[i], i, key[i]));
            
        }
    }

    public List<Grafos.Edge> getAGM() {
        return agm;
    }

    public void printAGM() {
        for (Grafos.Edge edge : agm) {
            System.out.println(edge);
        }
    }

    public double getCustoTotal() {
        double total = 0;
        for (Grafos.Edge edge : agm) {
            total += edge.getWeight();
        }
        return total;
    }
}