package br.com.controlepartidascs.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.dados.PopulaBancoComJson;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.ListaPartidaGanhador;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.Ranking;
import br.com.controlepartidascs.model.RankingJogadorMinimo;
import br.com.controlepartidascs.model.RankingPorPartida;
import br.com.controlepartidascs.repository.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	PartidaRepository partidaRepository;

	@Autowired
	JogadorService jogadorService;

	@Autowired
	PartidaDetalheService partidaDetalheService;

	/**
	 * Popula o banco de dados com o Json de partidas. "matches.json"
	 */
	public void popularBanco() {
		PopulaBancoComJson pbcj = new PopulaBancoComJson();
		List<Partida> partida = pbcj.getPartidasFromJsonFile();
		for (Partida match : partida) {
			Set<Jogador> jogadores = match.getJogador();
			jogadorService.salvarJogadoresInexistentes(jogadores); // Caso o banco não esteja populado ainda, o método
																	// irá
																	// adicionar jogadores inexistentes

			salvarPartidaComSomenteNomeDeJogadores(match);
		}
	}
	
	public Iterable<Partida> findAll(){
		return partidaRepository.findAll();
	}
	
	public List<Partida> findByInicioBetween(ZonedDateTime zdtInicio, ZonedDateTime zdtFim){
		return partidaRepository.findByInicioBetween(zdtInicio, zdtFim);
	}

	public Partida findByNumeroControle(int numeroControle) {
		return partidaRepository.findByNumeroControle(numeroControle);
	}
	
	public void salvar(Partida partida) {
		partidaRepository.save(partida);
	}

	public void salvarPartidaComSomenteNomeDeJogadores(Partida partida) {
		Set<Jogador> jogador = partida.getJogador();
		Set<Jogador> jogadorNovo = new HashSet<Jogador>();
		Jogador jogadorTemp;

		Iterator itr = jogador.iterator();
		while (itr.hasNext()) {
			jogadorTemp = (Jogador) itr.next();
			jogadorNovo.add(jogadorService.findByNome(jogadorTemp.getNome()));
		}

		partida.setJogador(jogadorNovo);

		salvar(partida);
	}

	// Utilizado Java8
	public String getRankingByDate(String date) {
		ZonedDateTime zdtInicio;
		ZonedDateTime zdtFim;

		if (date == null) {
			zdtInicio = null;
			zdtFim = null;
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			// convert String to LocalDate
			LocalDate localDate = LocalDate.parse(date, formatter);

			zdtInicio = localDate.atStartOfDay(ZoneOffset.UTC);
			zdtFim = localDate.atTime(23, 59, 59).atZone(ZoneOffset.UTC);
		}

		List<Partida> partidas = new ArrayList<Partida>();
		Iterable<Jogador> jogadores;
		List<Jogador> jogador = new ArrayList<Jogador>();

		jogadores = jogadorService.findAll();

		jogadores.forEach(jogador::add);
		Collections.sort(jogador, Comparator.comparingInt(Jogador::getId));

		if (zdtInicio == null || zdtFim == null) {
			Iterable<Partida> partidasTemp = partidaRepository.findAll();
			partidasTemp.forEach(partidas::add);
		} else {
			partidas = partidaRepository.findByInicioBetween(zdtInicio, zdtFim);
		}

		for (Partida partida : partidas) {
			List<PartidaDetalhe> partidasDetalhe = partidaDetalheService.getPartidasDetalhePorPartida(partida);
			for (PartidaDetalhe partidaDetalhe : partidasDetalhe) {
				jogador.get(jogador.indexOf(partidaDetalhe.getKiller())).getJogadorPontuacao().adicionarKill();
				jogador.get(jogador.indexOf(partidaDetalhe.getKilled())).getJogadorPontuacao().adicionarDeath();
			}
		}

		List<Ranking> ranking = new ArrayList<Ranking>();
		for (Jogador player : jogador) {
			ranking.add(new Ranking(player.getNome(), player.getJogadorPontuacao().getKills(),
					player.getJogadorPontuacao().getDeaths(), player.getJogadorPontuacao().getPontuacao()));
		}

		Collections.sort(ranking, Comparator.comparingInt(Ranking::getPontuacao));
		Collections.reverse(ranking);
		for (int i = 0; i < ranking.size(); i++) {
			ranking.get(i).setPosicao(i + 1);
		}

		String json = new Gson().toJson(ranking);
		return json;
	}

	public String findTodasPartidas() {

		Iterable<Partida> partidas = partidaRepository.findAll();
		List<Partida> partida = new ArrayList<Partida>();
		partidas.forEach(partida::add);

		List<ListaPartidaGanhador> lpg = new ArrayList<>();

		for (Partida match : partida) {

			try {
				List<Jogador> jogadores = partidaRepository.findJogadoresDaPartida(match.getNumeroControle());

				Integer quantidadeJogadores = jogadores.size();

				List<PartidaDetalhe> partidasDetalhe = partidaDetalheService.getPartidasDetalhePorPartida(match);
				for (PartidaDetalhe partidaDetalhe : partidasDetalhe) {
					jogadores.get(jogadores.indexOf(partidaDetalhe.getKiller())).getJogadorPontuacao().adicionarKill();
					jogadores.get(jogadores.indexOf(partidaDetalhe.getKilled())).getJogadorPontuacao().adicionarDeath();
				}

				Jogador jogador = Collections.max(jogadores, new Comparator<Jogador>() {
					public int compare(Jogador p1, Jogador p2) {
						if (p1.getJogadorPontuacao().getPontuacao() > p2.getJogadorPontuacao().getPontuacao())
							return 1;
						if (p1.getJogadorPontuacao().getPontuacao() < p2.getJogadorPontuacao().getPontuacao())
							return -1;
						if (p1.getJogadorPontuacao().getKills() > p2.getJogadorPontuacao().getKills())
							return 1;
						if (p1.getJogadorPontuacao().getKills() < p2.getJogadorPontuacao().getKills())
							return -1;

						return 0;
					}
				});

				lpg.add(new ListaPartidaGanhador(match.getInicio(), match.getFim(), quantidadeJogadores,
						jogador.getNome()));
			} catch (Exception e) {
				e.printStackTrace();
				LogController.log(
						"Erro com informações no banco de dados. Algum dado foi colocado manualmente e as partidas estão em conflito.");
				continue;
			}
		}

		String json = new Gson().toJson(lpg);
		return json.toString();
	}

	public String getRankingPartidaByNumeroControle(int numeroControle) {
		Partida partida = partidaRepository.findByNumeroControle(numeroControle);

		List<Jogador> jogadores = new ArrayList<Jogador>();

		jogadores = partidaRepository.findJogadoresDaPartida(partida.getNumeroControle());

		List<PartidaDetalhe> partidasDetalhe = partidaDetalheService.getPartidasDetalhePorPartida(partida);
		for (PartidaDetalhe partidaDetalhe : partidasDetalhe) {
			jogadores.get(jogadores.indexOf(partidaDetalhe.getKiller())).getJogadorPontuacao().adicionarKill();
			jogadores.get(jogadores.indexOf(partidaDetalhe.getKilled())).getJogadorPontuacao().adicionarDeath();
		}

		RankingPorPartida rankingPorPartida = new RankingPorPartida();
		List<RankingJogadorMinimo> rankingJogadorMinimo = new ArrayList<RankingJogadorMinimo>();

		for (Jogador jogador : jogadores) {
			rankingJogadorMinimo.add(new RankingJogadorMinimo(jogador));
		}

		rankingJogadorMinimo.sort(new Comparator<RankingJogadorMinimo>() {
			public int compare(RankingJogadorMinimo p1, RankingJogadorMinimo p2) {
				if (p1.getJogador().getJogadorPontuacao().getPontuacao() > p2.getJogador().getJogadorPontuacao()
						.getPontuacao())
					return 1;
				if (p1.getJogador().getJogadorPontuacao().getPontuacao() < p2.getJogador().getJogadorPontuacao()
						.getPontuacao())
					return -1;
				return 0;
			}
		});

		Collections.reverse(rankingJogadorMinimo);

		for (int i = 0; i < rankingJogadorMinimo.size(); i++) {
			rankingJogadorMinimo.get(i).setPosicao(i + 1);
			rankingJogadorMinimo.get(i).setJogadorNome(rankingJogadorMinimo.get(i).getJogador().getNome());
			rankingJogadorMinimo.get(i)
					.setPontuacao(rankingJogadorMinimo.get(i).getJogador().getJogadorPontuacao().getPontuacao());
		}

		rankingPorPartida.setRankingJogadorMinimo(rankingJogadorMinimo);
		rankingPorPartida.setDataIni(partida.getInicio());
		rankingPorPartida.setDataFim(partida.getFim());

		String json = new Gson().toJson(rankingPorPartida);
		return json;
	}
}
