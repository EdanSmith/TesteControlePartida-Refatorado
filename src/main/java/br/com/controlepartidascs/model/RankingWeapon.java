package br.com.controlepartidascs.model;

public class RankingWeapon {

	private int posicao;
	private String weaponNome;
	private int kills = 0;

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public String getWeaponNome() {
		return weaponNome;
	}

	public void setWeaponNome(String weaponNome) {
		this.weaponNome = weaponNome;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void adicionarKill() {
		kills++;
	}

	public RankingWeapon() {
	}

	public RankingWeapon(String weaponNome, int kills) {
		this.weaponNome = weaponNome;
		this.kills = kills;
	}

}
