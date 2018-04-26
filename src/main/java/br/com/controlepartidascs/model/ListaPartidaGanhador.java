package br.com.controlepartidascs.model;

import java.time.ZonedDateTime;

public class ListaPartidaGanhador {

	private ZonedDateTime dataIni;
	private ZonedDateTime dataFim;
	private Integer quantidadeJogador;
	private String nomeJogadorCampeao;

	public ZonedDateTime getDataIni() {
		return dataIni;
	}

	public void setDataIni(ZonedDateTime dataIni) {
		this.dataIni = dataIni;
	}

	public ZonedDateTime getDataFim() {
		return dataFim;
	}

	public void setDataFim(ZonedDateTime dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getQuantidadeJogador() {
		return quantidadeJogador;
	}

	public void setQuantidadeJogador(Integer quantidadeJogador) {
		this.quantidadeJogador = quantidadeJogador;
	}

	public String getNomeJogadorCampeao() {
		return nomeJogadorCampeao;
	}

	public void setNomeJogadorCampeao(String nomeJogadorCampeao) {
		this.nomeJogadorCampeao = nomeJogadorCampeao;
	}

	public ListaPartidaGanhador() {
	}

	public ListaPartidaGanhador(ZonedDateTime dataIni, ZonedDateTime dataFim, Integer quantidadeJogador,
			String nomeJogadorCampeao) {
		this.dataIni = dataIni;
		this.dataFim = dataFim;
		this.quantidadeJogador = quantidadeJogador;
		this.nomeJogadorCampeao = nomeJogadorCampeao;
	}

}
