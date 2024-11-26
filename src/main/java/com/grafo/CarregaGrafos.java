package com.grafo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CarregaGrafos {
    public static Grafos carregaArquivoGrafo(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            
            String[] primeiraLinha = reader.readLine().split("\\s+");
            int vertices = Integer.parseInt(primeiraLinha[0]);
            int edges = Integer.parseInt(primeiraLinha[1]);

            Grafos grafo = new Grafos(vertices);

            for (int i = 0; i < edges; i++) {
                String[] edgeData = reader.readLine().split("\\s+");
                int source = Integer.parseInt(edgeData[0]);
                int destination = Integer.parseInt(edgeData[1]);
                double weight = Double.parseDouble(edgeData[2]);
                grafo.addEdge(source, destination, weight);
            }
            return grafo;
            
        }
    }
}
