package br.com.controlepartidascs.model;

import java.time.ZonedDateTime;
import java.util.List;

public class RankingPorPartida {

	private List<RankingJogadorMinimo> rankingJogadorMinimo;

	private ZonedDateTime dataIni;

	private ZonedDateTime dataFim;

	public RankingPorPartida() {
	}

	public RankingPorPartida(List<RankingJogadorMinimo> rankingJogadorMinimo, ZonedDateTime dataIni,
			ZonedDateTime dataFim) {
		this.rankingJogadorMinimo = rankingJogadorMinimo;
		this.dataIni = dataIni;
		this.dataFim = dataFim;
	}

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

	public List<RankingJogadorMinimo> getRankingJogadorMinimo() {
		return rankingJogadorMinimo;
	}

	public void setRankingJogadorMinimo(List<RankingJogadorMinimo> rankingJogadorMinimo) {
		this.rankingJogadorMinimo = rankingJogadorMinimo;
	}

}
