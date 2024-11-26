package com.grafo;

import java.util.*;

public class FluxoMaximo {
    private final int V;
    private Map<Integer, Map<Integer, Integer>> capacidade; 
    private Map<Integer, Map<Integer, Integer>> fluxo;
    private List<Integer>[] adj; //

    @SuppressWarnings("unchecked")
    public FluxoMaximo(int vertices) {
        this.V = vertices;
        capacidade = new HashMap<>();
        fluxo = new HashMap<>();
        adj = new List[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
            capacidade.put(i, new HashMap<>());
            fluxo.put(i, new HashMap<>());
        }
    }

    public void addEdge(int u, int v, int cap) {
        adj[u].add(v);
        adj[v].add(u);
        capacidade.get(u).put(v, capacidade.getOrDefault(u, new HashMap<>()).getOrDefault(v, 0) + cap);
    }

    private boolean bfs(int s, int t, int[] parent) {
        boolean[] visitado = new boolean[V];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visitado[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj[u]) {
                if (!visitado[v] && capacidade.get(u).getOrDefault(v, 0) - fluxo.get(u).getOrDefault(v, 0) > 0) {
                    queue.add(v);
                    visitado[v] = true;
                    parent[v] = u;
                    if (v == t) return true;
                }
            }
        }
        return false;
    }

    public int edmondsKarp(int s, int t) {
        int[] parent = new int[V];
        int fluxoMaximo = 0;

        while (bfs(s, t, parent)) {
            int caminhoFluxo = Integer.MAX_VALUE;
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                caminhoFluxo = Math.min(caminhoFluxo, capacidade.get(u).get(v) - fluxo.get(u).getOrDefault(v, 0));
            }

            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                fluxo.get(u).put(v, fluxo.get(u).getOrDefault(v, 0) + caminhoFluxo);
                fluxo.get(v).put(u, fluxo.get(v).getOrDefault(u, 0) - caminhoFluxo);
            }

            fluxoMaximo += caminhoFluxo;
        }

        return fluxoMaximo;
    }
}
