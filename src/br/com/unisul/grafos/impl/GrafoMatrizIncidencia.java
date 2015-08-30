package br.com.unisul.grafos.impl;

import java.util.List;

import br.com.unisul.grafos.entity.Aresta;
import br.com.unisul.grafos.entity.Vertice;

/*
 * Classe Grafo Matriz de Incidencia.
 */
public class GrafoMatrizIncidencia extends Grafo {
	
	List<Vertice> _vertices;
    List<Aresta> _arestas;
	
    /*
     * Construtor da classe.
     * Inicializa as listas de vertices e arestas.
     */
    public GrafoMatrizIncidencia(Grafo grafo) {
    	_vertices = grafo._vertices;
        _arestas = grafo._arestas;
    }

    /*
     * Metodo que monta e exibi a representação do grafo
     * no painel de saida.
     */
    public String exibiGrafo() {
		final StringBuilder grafo = new StringBuilder();
    	
		grafo.append("#### GRAFO MATRIZ DE INCIDENCIA ####\n");
		montaCabecalhoGrafo(grafo);
    	for (Vertice vertice : _vertices) {
    		grafo.append(vertice.getId());
    		for (Aresta aresta : _arestas) {
				if (vertice.getId() == aresta.getInicio().getId()) {
					grafo.append("|    1   |");
					continue;
				}
				
				if (vertice.getId() == aresta.getFim().getId()) {
					grafo.append("|   -1   |");
					continue;
				}
				
				grafo.append("|    0   |");
			}
    		
    		grafo.append("\n");
        }
    	
    	grafo.append("-------------------------------------------------------------------\n");
    	
        return grafo.toString();
	}
	
    /*
     * Metodo que monta o cabeçalho do grafo
     * apartir das arestas.
     */
    private void montaCabecalhoGrafo(StringBuilder grafo) {
    	for (int i = 1; i <= _arestas.size(); i++) {
    		if (i == 1) {
    			grafo.append("  |   ");
    		} else  if (i <= 9){
    			grafo.append("|   ");
    		} else {
    			grafo.append("| ");
    		}
    		grafo.append("E").append(i);
    		
    		if (i > 9) {
    			grafo.append("  |");
    		} else {
    			grafo.append("  |");
    		}
		}
    	grafo.append("\n");
    }

}
