package br.com.unisul.grafos.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

import br.com.unisul.grafos.impl.Seta;
import br.com.unisul.grafos.impl.Utils;

/*
 * Classe Aresta
 * Controla as informações sobre cada aresta
 * e a desenha na tela.
 */
public class Aresta {

	private Vertice _inicio;
    private Vertice _fim;
    private Double _peso;
    private boolean _direcionada;
    private boolean _valorado;
    private Seta _seta;
    private boolean _isMesmoVertice;
    
    private QuadCurve2D.Double _conexaoEmCurva;
	private int _curvatura = 0;
	private Rectangle2D _areaDoTexto;
	private Point2D.Double _controleDeCurva;
	private Point2D.Double _posicaoFinal;
	private Ellipse2D.Double _conexaoEmAutoLaco;
	private Line2D.Double _conexaoEmLinha;
	
	private double _larguraVerticeDeInicial = Vertice.LARGURA;
//	private double _larguraVerticeFinal = Vertice.LARGURA;
	private Point2D _centroVerticeInicial;
	private Point2D _centroVerticeFinal;
	private Shape _conexaoEntreOsVertices;
    
    /*
     * Constante com um angulo padrão de cada vertice
     */
    public static final double ANGULO = Math.PI / 25d;

    /*
     * Construtor da classe.
     * Recebe um vertice inicial e um final para criação da aresta.
     * Calcula o angulo de cada vertice para o desenho da aresta.
     */
    public Aresta(Vertice inicio, Vertice fim, boolean direcionada, Double peso, boolean valorado) {
        this._inicio = inicio;
        this._fim = fim;
        this._peso = peso;
        this._direcionada = direcionada;
        this._valorado = valorado;
        this._areaDoTexto = new Rectangle2D.Double();
        this._seta = new Seta();
        this._isMesmoVertice = inicio == fim;
        
		this._conexaoEmCurva = new QuadCurve2D.Double();
		this._conexaoEmAutoLaco = new Ellipse2D.Double();
		this._conexaoEmLinha = new Line2D.Double();
		
		this._controleDeCurva = new Point2D.Double();
		this._posicaoFinal = new Point2D.Double();
		
		this._centroVerticeInicial = this._inicio.getCentroVertice();
		this._centroVerticeFinal = this._fim.getCentroVertice();
    }
    
    /*
     * Metodo que desenha a aresta na tela.
     */
    public void desenharAresta(Graphics2D graphics2D) {
    	graphics2D.setStroke(new BasicStroke());
    	graphics2D.setPaint(Color.BLACK);
    	graphics2D.draw(_conexaoEntreOsVertices);
    	
    	if (_direcionada) {
    		_seta.desenhar(graphics2D, _isMesmoVertice);
		}
    	
    	if (_valorado) {
    		desenharTexto(graphics2D);
		}
    }
    
    private void desenharTexto(Graphics2D graphics2D) {
    	graphics2D.setPaint(Color.WHITE);
    	graphics2D.fill(_areaDoTexto);
    	
		graphics2D.setFont(new Font("Serif", Font.BOLD, 12));
    	graphics2D.setPaint(Color.BLUE);
		
		final FontMetrics fonteMetrics = graphics2D.getFontMetrics();
		graphics2D.drawString(String.valueOf(_peso), (int) _areaDoTexto.getX(), (int) _areaDoTexto.getY() + fonteMetrics.getHeight());
	}
    
	private void calcularODesenhoDaAresta() {
		double anguloInicial, anguloFinal = 0D;
		Point2D pontoInicial, pontoFinal = null;
		
		if (_isMesmoVertice){
			_conexaoEmAutoLaco.x = _centroVerticeInicial.getX();
			_conexaoEmAutoLaco.y = _centroVerticeInicial.getY() - _larguraVerticeDeInicial;
			_conexaoEmAutoLaco.width = _larguraVerticeDeInicial;
			_conexaoEmAutoLaco.height = _larguraVerticeDeInicial;
			
		} else if (!_direcionada) {
			anguloInicial = Utils.getAngulo(_centroVerticeInicial, _centroVerticeFinal);
			anguloFinal = Utils.getAngulo(_centroVerticeFinal, _centroVerticeInicial);
			
			pontoInicial = Utils.getPontoNoVertice(_centroVerticeInicial, anguloInicial);
			pontoFinal = Utils.getPontoNoVertice(_centroVerticeFinal, anguloFinal);
			
			_conexaoEmLinha = new Line2D.Double(pontoInicial, pontoFinal);
			
		} else {
			
			double distanciaX, distanciaY, centroX, centroY, distancia, fatorX, fatorY;
			
			int contador = 1;
			Point2D.Double inicio = null;
			
			while(contador <= 2 ) {
				anguloInicial = Utils.getAngulo(_centroVerticeInicial, _controleDeCurva);
				anguloFinal = Utils.getAngulo(_controleDeCurva, _centroVerticeFinal);
				
				pontoInicial = Utils.getPontoNoVertice(_centroVerticeInicial, anguloInicial - ANGULO);
				pontoFinal = Utils.getPontoNoVertice(_centroVerticeFinal, anguloFinal - Math.PI + ANGULO);
				
				inicio = new Point2D.Double(pontoInicial.getX(), pontoInicial.getY());
				_posicaoFinal.setLocation(pontoFinal.getX(), pontoFinal.getY());
				
				distanciaX = _posicaoFinal.x - inicio.x;
				distanciaY = _posicaoFinal.y - inicio.y;
				
				centroX = (inicio.x + _posicaoFinal.x) / 2.0;
				centroY = (inicio.y + _posicaoFinal.y) / 2.0;
				
				distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
				fatorX = distancia == 0D ? 0D : distanciaX / distancia;
				fatorY = distancia == 0D ? 0D : distanciaY / distancia;
				
				_controleDeCurva.x = (centroX + _curvatura * _larguraVerticeDeInicial * fatorY);
				_controleDeCurva.y = (centroY - _curvatura * _larguraVerticeDeInicial * fatorX);
				
				contador++;
			}
			
			_conexaoEmCurva.setCurve(inicio.x, inicio.y, _controleDeCurva.x, _controleDeCurva.y, _posicaoFinal.x, _posicaoFinal.y);
		}
	}
	
	/*
	 * Calcula o desenho do texto da aresta
	 */
	private void calcularAreaTexto(FontMetrics fonteMetrics) {
		if (_isMesmoVertice) {
			_areaDoTexto.setRect((Math.abs(_centroVerticeInicial.getX() + (_larguraVerticeDeInicial / 2)) - (fonteMetrics.stringWidth(String.valueOf(_peso)) / 2)), 
					(Math.abs(_centroVerticeInicial.getY() - _larguraVerticeDeInicial) - fonteMetrics.getHeight()), 
					fonteMetrics.stringWidth(String.valueOf(_peso)), 
					fonteMetrics.getHeight());
		
		} else {
			Point2D ponto2D = Utils.getPontoTextoAresta(_centroVerticeInicial, _centroVerticeFinal, _controleDeCurva);
			_areaDoTexto.setRect(ponto2D.getX() - (fonteMetrics.stringWidth(String.valueOf(_peso)) / 2),
					ponto2D.getY() - fonteMetrics.getHeight(), fonteMetrics.stringWidth(String.valueOf(_peso)), fonteMetrics.getHeight());
			
		}
		
	}
	
	/* 
	 * Recalcula o desenho da aresta e do texto dela 
	 */
	public void desenhaAresta(Graphics2D graphics2D) {
		calcularODesenhoDaAresta();

		if (_direcionada) {
			_seta.calcular(_controleDeCurva, _posicaoFinal, _isMesmoVertice);
		}

		calcularAreaTexto(graphics2D.getFontMetrics());
		
		if (!_direcionada) {
			_conexaoEntreOsVertices = _conexaoEmLinha;
		} else if (_isMesmoVertice) {
			_conexaoEntreOsVertices = _conexaoEmAutoLaco;
		} else {
			_conexaoEntreOsVertices = _conexaoEmCurva;
		}

		desenharAresta(graphics2D);
	}
    

	public Vertice getInicio() {
		return _inicio;
	}

	public Vertice getFim() {
		return _fim;
	}
	
	public void setCurvatura(int curvatura){
		this._curvatura = curvatura;
	}

	public Double getPeso() {
		return _peso;
	}

	public void setPeso(Double peso) {
		this._peso = peso;
	}

	public boolean isDirecionada() {
		return _direcionada;
	}

	public void setDirecionada(boolean direcionada) {
		this._direcionada = direcionada;
	}

	public boolean isValorado() {
		return _valorado;
	}

	public void setValorado(boolean valorado) {
		this._valorado = valorado;
	}

}
