package br.com.controlepartidascs.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Table(name = "partida")
@Entity
public class Partida {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@JsonProperty("match")
	@Column(name = "numero_controle")
	private Integer numeroControle;

	@NotNull
	@JsonProperty("begin")
	private ZonedDateTime inicio;

	@NotNull
	@JsonProperty("end")
	private ZonedDateTime fim;

	@JsonProperty("players")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "partida_jogador", joinColumns = { @JoinColumn(name = "id_partida") }, inverseJoinColumns = {
			@JoinColumn(name = "id_jogador") })
	private Set<Jogador> jogador = new HashSet<Jogador>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partida")
	private List<PartidaDetalhe> partidaDetalhe;

	public Partida() {
	}

	public Partida(Integer numeroControle) {
		this.numeroControle = numeroControle;
	}

	public Partida(Integer numeroControle, ZonedDateTime inicio, ZonedDateTime fim) {
		this.numeroControle = numeroControle;
		this.inicio = inicio;
		this.fim = fim;
	}

	public Integer getId() {
		return id;
	}

	public Integer getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Integer numeroControle) {
		this.numeroControle = numeroControle;
	}

	public ZonedDateTime getInicio() {
		return inicio;
	}

	public void setInicio(ZonedDateTime inicio) {
		this.inicio = inicio;
	}

	public ZonedDateTime getFim() {
		return fim;
	}

	public void setFim(ZonedDateTime fim) {
		this.fim = fim;
	}

	public Set<Jogador> getJogador() {
		return jogador;
	}

	public void setJogador(Set<Jogador> jogador) {
		this.jogador = jogador;
	}

	@Override
	public String toString() {
		return "[numeroControle=" + numeroControle + ", inicio=" + inicio + ", fim=" + fim + ", jogador=" + jogador
				+ "]";
	}

}
