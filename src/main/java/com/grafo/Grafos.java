package com.grafo;

import java.util.*;

public class Grafos {
    private final int contagemVertices;
    private final List<Edge> edges;
    private final List<List<Edge>> listaAdjacente;

    public Grafos(int contagemVertices) {
        this.contagemVertices = contagemVertices;
        this.edges = new ArrayList<>();
        this.listaAdjacente = new ArrayList<>();
        for (int i = 0; i < contagemVertices; i++) {
            listaAdjacente.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, double weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        listaAdjacente.get(from).add(edge);
    }

    public int getVerticesCount() {
        return contagemVertices;
    }

    public List<Edge> getEdgesFrom(int vertex) {
        return listaAdjacente.get(vertex);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public static class Edge {
        private final int from;
        private final int to;
        private final double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "(" + from + " -> " + to + ", " + weight + ")";
        }
    }
}
