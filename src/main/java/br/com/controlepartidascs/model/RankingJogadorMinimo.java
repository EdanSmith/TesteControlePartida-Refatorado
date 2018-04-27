package br.com.controlepartidascs.model;

public class RankingJogadorMinimo {

	private int posicao;
	private String jogadorNome;
	private int pontuacao;
	private transient Jogador jogador;

	public RankingJogadorMinimo() {
	}

	public RankingJogadorMinimo(Jogador jogador) {
		this.jogador = jogador;
	}

	public RankingJogadorMinimo(int posicao, Jogador jogador) {
		this.posicao = posicao;
		this.jogador = jogador;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public String getJogadorNome() {
		return jogadorNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.jogadorNome = jogadorNome;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

}
