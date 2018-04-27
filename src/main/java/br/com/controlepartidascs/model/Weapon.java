package br.com.controlepartidascs.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Weapon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String nome;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "weapon")
	private List<PartidaDetalhe> partidaDetalhe;

	@Transient
	private RankingWeapon rankingWeapon = new RankingWeapon();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Weapon() {
	}

	public Weapon(String nome) {
		this.nome = nome;
	}

	public RankingWeapon getRankingWeapon() {
		return rankingWeapon;
	}

	public void setRankingWeapon(RankingWeapon rankingWeapon) {
		this.rankingWeapon = rankingWeapon;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Weapon other = (Weapon) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
