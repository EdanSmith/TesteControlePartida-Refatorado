package br.com.controlepartidascs.model;

public class Ranking {

	private int posicao;
	private String nomeJogador;
	private int kills;
	private int deaths;
	private int pontuacao;

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public String getNomeJogador() {
		return nomeJogador;
	}

	public void setNomeJogador(String nomeJogador) {
		this.nomeJogador = nomeJogador;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Ranking() {
	}

	public Ranking(String nomeJogador, int kills, int deaths, int pontuacao) {
		this.nomeJogador = nomeJogador;
		this.kills = kills;
		this.deaths = deaths;
		this.pontuacao = pontuacao;
	}

}
