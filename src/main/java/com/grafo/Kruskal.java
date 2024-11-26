package com.grafo;

import java.util.*;

public class Kruskal {
    private Grafos graph;
    private List<Grafos.Edge> agm;

    public Kruskal(Grafos graph) {
        this.graph = graph;
        this.agm = new ArrayList<>();
    }

    public void geraAGM() {

        List<Grafos.Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingDouble(Grafos.Edge::getWeight));

        UnionFind uf = new UnionFind(graph.getVerticesCount());

        for (Grafos.Edge edge : edges) {
            int u = edge.getFrom();
            int v = edge.getTo();

            if (!uf.connected(u, v)) {
                uf.union(u, v);
                agm.add(edge);

            }
        }
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

    public static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }

        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }
}
