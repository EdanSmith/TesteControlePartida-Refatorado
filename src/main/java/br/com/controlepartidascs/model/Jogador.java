package br.com.controlepartidascs.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "jogador")
public class Jogador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonProperty("name")
	private String nome;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "jogador")
	private Set<Partida> partidas = new HashSet<Partida>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jogadorKiller")
	private List<PartidaDetalhe> partidaDetalheKiller;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jogadorKilled")
	private List<PartidaDetalhe> partidaDetalheKilled;

	@JsonIgnore
	@Transient
	private JogadorPontuacao jogadorPontuacao = new JogadorPontuacao();

	public Jogador() {
	}

	public Jogador(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public JogadorPontuacao getJogadorPontuacao() {
		return jogadorPontuacao;
	}

	public void setJogadorPontuacao(JogadorPontuacao jogadorPontuacao) {
		this.jogadorPontuacao = jogadorPontuacao;
	}

	@Override
	public String toString() {
		return "[nome=" + nome + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogador other = (Jogador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
