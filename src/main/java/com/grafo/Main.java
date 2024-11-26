package com.grafo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        
        String arquivo = "src/USA-road-d.NY.txt";

        Grafos grafo = lerGrafoDoArquivo(arquivo);

        System.out.println(grafo);

        // Dijkstra (Caminho Mínimo)
        long startTimeDijkstra = System.nanoTime();
        Dijkstra dijkstra = new Dijkstra(grafo);
        double[] dijkstraDistancia = dijkstra.shortestPath(0);
        long endTimeDijkstra = System.nanoTime();
        long dijkstraDuracao = endTimeDijkstra - startTimeDijkstra;

        // Kruskal (Árvore Geradora Mínima)
        long startTimeKruskal = System.nanoTime();
        Kruskal kruskal = new Kruskal(grafo);
        kruskal.geraAGM();
        long endTimeKruskal = System.nanoTime();
        long duracaoKruskal = endTimeKruskal - startTimeKruskal;

        // Prim (Árvore Geradora Mínima)
        long startTimePrim = System.nanoTime();
        Prim prim = new Prim(grafo);
        prim.geraAGM();
        long endTimePrim = System.nanoTime();
        long duracaoPrim = endTimePrim - startTimePrim;

        // Edmonds-Karp (Fluxo Máximo)
        FluxoMaximo fluxoMaximo = new FluxoMaximo(grafo.getVerticesCount());
        for (Grafos.Edge e : grafo.getEdges()) {
            fluxoMaximo.addEdge(e.getFrom(), e.getTo(), (int) e.getWeight());
        }
        int fonte = 0;
        int destino = grafo.getVerticesCount() - 1;
        long startTimeFluxo = System.nanoTime();
        int fluxoMax = fluxoMaximo.edmondsKarp(fonte, destino);
        long endTimeFluxo = System.nanoTime();
        double tempoExecucaoFluxo = (endTimeFluxo - startTimeFluxo) / 1_000_000_000.0;

        int n = grafo.getVerticesCount(); // vértices
        int m = grafo.getEdges().size(); // arestas
  
        System.out.println("\nTabela de Resultados:");
        System.out.printf("Grafo: %d vértices, %d arestas\n", n, m);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Resultados");

            CellStyle estiloBorda = workbook.createCellStyle();
            estiloBorda.setBorderTop(BorderStyle.THIN);
            estiloBorda.setBorderBottom(BorderStyle.THIN);
            estiloBorda.setBorderLeft(BorderStyle.THIN);
            estiloBorda.setBorderRight(BorderStyle.THIN);

            Row header = sheet.createRow(0);

            Cell celulaGrafo = header.createCell(0);
            celulaGrafo.setCellValue("Grafo");
            celulaGrafo.setCellStyle(estiloBorda);

            Cell celulaGrafoExtra = header.createCell(1);
            celulaGrafoExtra.setCellStyle(estiloBorda);

            Cell celulaCM = header.createCell(2);
            celulaCM.setCellValue("Caminho Mínimo (Dijkstra)");
            celulaCM.setCellStyle(estiloBorda);

            Cell celulaCMExtra = header.createCell(3);
            celulaCMExtra.setCellStyle(estiloBorda);

            Cell celulaAGM = header.createCell(4);
            celulaAGM.setCellValue("Árvore Geradora Mínima (Kruskal)");
            celulaAGM.setCellStyle(estiloBorda);

            Cell celulaAGMExtra = header.createCell(5);
            celulaAGMExtra.setCellStyle(estiloBorda);

            Cell celulaAGMPrim = header.createCell(6);
            celulaAGMPrim.setCellValue("Árvore Geradora Mínima (Prim)");
            celulaAGMPrim.setCellStyle(estiloBorda);

            Cell celulaAGMPrimExtra = header.createCell(7);
            celulaAGMPrimExtra.setCellStyle(estiloBorda);

            Cell celulaFM = header.createCell(8);
            celulaFM.setCellValue("Fluxo Máximo (Edmonds-Karp)");
            celulaFM.setCellStyle(estiloBorda);

            Cell celulaFMExtra = header.createCell(9);
            celulaFMExtra.setCellStyle(estiloBorda);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));

            Row cabecalho = sheet.createRow(1);

            for (int i = 0; i <= 9; i++) {
                Cell cell = cabecalho.createCell(i);
                cell.setCellStyle(estiloBorda);
                if (i == 0) cell.setCellValue("n");
                else if (i == 1) cell.setCellValue("m");
                else if (i % 2 == 0) cell.setCellValue("Custo");
                else cell.setCellValue("Tempo(s)");
            }

            Row row = sheet.createRow(2);

            String[] values = {
                String.valueOf(n), String.valueOf(m),
                String.valueOf(dijkstraDistancia[n - 1]), String.valueOf(dijkstraDuracao / 1_000_000_000.0),
                String.valueOf(kruskal.getCustoTotal()), String.valueOf(duracaoKruskal / 1_000_000_000.0),
                String.valueOf(prim.getCustoTotal()), String.valueOf(duracaoPrim / 1_000_000_000.0),
                String.valueOf(fluxoMax), String.valueOf(tempoExecucaoFluxo)
            };

            for (int i = 0; i < values.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(values[i]);
                cell.setCellStyle(estiloBorda);
            }

            for (int i = 0; i <= 9; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }

            try (FileOutputStream fileOut = new FileOutputStream("resultados.xlsx")) {
                workbook.write(fileOut);
            }

            System.out.println("Tabela de resultados exportada para 'resultados.xlsx'.");

        } catch (IOException e) {
            System.out.println("Erro ao gerar o arquivo Excel: " + e.getMessage());
        }
    }

    public static Grafos lerGrafoDoArquivo(String arquivo) {
        Grafos grafo = null;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("c")) {
                    continue;
                }

                if (linha.startsWith("p sp")) {
                    String[] parts = linha.split(" ");
                    int n = Integer.parseInt(parts[2]);
                    @SuppressWarnings("unused")
                    int m = Integer.parseInt(parts[3]);
                    grafo = new Grafos(n);
                    continue;
                }
                
                if (linha.startsWith("a")) {
                    String[] parts = linha.split(" ");
                    int u = Integer.parseInt(parts[1]) - 1;
                    int v = Integer.parseInt(parts[2]) - 1;
                    double peso = Double.parseDouble(parts[3]);
                    grafo.addEdge(u, v, peso);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grafo;
    }
}