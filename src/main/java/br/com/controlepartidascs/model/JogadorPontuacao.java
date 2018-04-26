package br.com.controlepartidascs.model;

public class JogadorPontuacao {

	private int kills = 0;
	private int deaths = 0;

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void adicionarKill() {
		this.kills++;
	}

	public void adicionarDeath() {
		this.deaths++;
	}

	public int getPontuacao() {
		return kills - deaths;
	}

}
