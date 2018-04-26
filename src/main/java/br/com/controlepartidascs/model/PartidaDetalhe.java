package br.com.controlepartidascs.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "partida_detalhe")
public class PartidaDetalhe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonProperty("match")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_partida", nullable = false)
	private Partida partida;

	@JsonProperty("killtime")
	@Column(name = "killtime")
	private LocalTime killTime;

	@ManyToOne
	@JoinColumn(name = "id_jogador_killer", nullable = false)
	private Jogador jogadorKiller;

	@ManyToOne
	@JoinColumn(name = "id_jogador_killed", nullable = false)
	private Jogador jogadorKilled;
	
	@ManyToOne
	@JoinColumn(name = "id_weapon", nullable = false)
	private Weapon weapon;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalTime getKillTime() {
		return killTime;
	}

	public void setKillTime(LocalTime killTime) {
		this.killTime = killTime;
	}

	public Jogador getKiller() {
		return jogadorKiller;
	}

	public void setKiller(Jogador killer) {
		this.jogadorKiller = killer;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Jogador getKilled() {
		return jogadorKilled;
	}

	public void setKilled(Jogador killed) {
		this.jogadorKilled = killed;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
	

}
