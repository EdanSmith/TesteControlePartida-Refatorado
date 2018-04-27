package br.com.controlepartidascs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.dados.PopulaBancoComJson;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.RankingJogadorMinimo;
import br.com.controlepartidascs.model.RankingPorPartida;
import br.com.controlepartidascs.model.Weapon;
import br.com.controlepartidascs.repository.PartidaDetalheRepository;

@Service
public class PartidaDetalheService {

	@Autowired
	PartidaDetalheRepository partidaDetalheRepository;

	@Autowired
	PartidaService partidaService;

	@Autowired
	JogadorService jogadorService;

	@Autowired
	WeaponService weaponService;

	LogController log = new LogController();

	/**
	 * Popula o banco de dados com o Json de partidas detalhadas. "wrangle.json" Se
	 * o campo da arma estiver em branco, a partida detalhe será recusada. Caso a
	 * arma ainda não exista, ela será criada. Comportamento proposital para
	 * facilitar no teste.
	 */
	public void popularBanco() {
		PopulaBancoComJson pbcj = new PopulaBancoComJson();
		List<PartidaDetalhe> partidaDetalhe = pbcj.getPartidasDetalheFromJsonFile();

		Boolean validacaoPropriedades;

		for (PartidaDetalhe matchDetail : partidaDetalhe) {

			validacaoPropriedades = validarPropriedades(matchDetail);
			if (!validacaoPropriedades) {
				continue;
			}

			try {
			salvarFromServidorJogo(matchDetail);
			}catch(Exception e) {
				LogController.logError("Erro ao tentar salvar Partida Detalhe");
				e.printStackTrace();
			}
		}
	}

	private boolean validarPropriedades(PartidaDetalhe partidaDetalhe) {
		Boolean jogadorKillerExistente;
		Boolean jogadorKilledExistente;
		Boolean weaponExistente;

		jogadorKillerExistente = jogadorService.verificarExistenciaJogadorPorNome(partidaDetalhe.getKiller().getNome());
		jogadorKilledExistente = jogadorService.verificarExistenciaJogadorPorNome(partidaDetalhe.getKilled().getNome());

		weaponExistente = weaponService.verificarExistenciaPorNome(partidaDetalhe.getWeapon().getNome());

		if (!jogadorKillerExistente) {
			mensagemErroJogadorInexistente(partidaDetalhe, partidaDetalhe.getKiller().getNome());
			return false;
		} else if (!jogadorKilledExistente) {
			mensagemErroJogadorInexistente(partidaDetalhe, partidaDetalhe.getKilled().getNome());
			return false;
		}
		if (!weaponExistente) {
			weaponService.salvar(partidaDetalhe.getWeapon());
			mensagemInfoWeaponCriada(partidaDetalhe);
			return false;
		}

		return true;
	}

	private void mensagemErroJogadorInexistente(PartidaDetalhe partidaDetalhe, String nomeJogador) {
		LogController.logError("Jogador " + nomeJogador
				+ " não encontrado. Partida Detalhe não salva. Detalhes: Numero de Controle: "
				+ partidaDetalhe.getPartida().getNumeroControle() + " killtime: " + partidaDetalhe.getKillTime());
	}

	private void mensagemInfoWeaponCriada(PartidaDetalhe partidaDetalhe) {
		LogController.log("Weapon adicionada " + partidaDetalhe.getWeapon().getNome());
	}

	public void salvar(PartidaDetalhe partidaDetalhe) {
		partidaDetalheRepository.save(partidaDetalhe);
	}

	public void salvarFromServidorJogo(PartidaDetalhe partidaDetalhe) throws Exception {
		
		Partida partida;
		partida = partidaService.findByNumeroControle(partidaDetalhe.getPartida().getNumeroControle());
		partidaDetalhe.setPartida(partida);

		Jogador jogadorKiller;
		jogadorKiller = jogadorService.findByNome(partidaDetalhe.getKiller().getNome());
		partidaDetalhe.setKiller(jogadorKiller);

		Jogador jogadorKilled;
		jogadorKilled = jogadorService.findByNome(partidaDetalhe.getKilled().getNome());
		partidaDetalhe.setKilled(jogadorKilled);

		Weapon weapon;
		weapon = weaponService.getWeaponByName(partidaDetalhe.getWeapon().getNome());
		partidaDetalhe.setWeapon(weapon);

		PartidaDetalhe partidaDetalheTemp = partidaDetalheRepository.findByAllAttributes(partida.getNumeroControle(),
				partidaDetalhe.getKillTime(), jogadorKiller.getId(), jogadorKilled.getId(), weapon.getId());
		
		if(partidaDetalheTemp != null) {
			LogController.logWarning("Essa Partida Detalhe já foi salva anteriormente");
			throw new Exception("Essa partida já foi salva anteriormente");
		}
		
		partidaDetalheRepository.save(partidaDetalhe);

	}

	public List<PartidaDetalhe> getPartidasDetalhePorPartida(Partida partida) {
		return partidaDetalheRepository.findByPartida(partida);
	}

	public String getRankingPartidaDetalheByNumeroControle(int numeroControle) throws NullPointerException{
		Partida partida = partidaService.findByNumeroControle(numeroControle);
		
		if(partida == null) {
			throw new NullPointerException("Nenhuma partida encontrada");
		}

		List<Jogador> jogadores = new ArrayList<Jogador>();

		jogadores = partidaService.findJogadoresDaPartida(partida.getNumeroControle());

		List<PartidaDetalhe> partidasDetalhe = getPartidasDetalhePorPartida(partida);
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
