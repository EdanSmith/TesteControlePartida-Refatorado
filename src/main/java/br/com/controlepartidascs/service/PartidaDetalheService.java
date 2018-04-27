package br.com.controlepartidascs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.dados.PopulaBancoComJson;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
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
	 * Popula o banco de dados com o Json de partidas detalhadas. "wrangle.json"
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

			salvarFromServidorJogo(matchDetail);
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
		} else if (!weaponExistente) {
			mensagemErroWeaponInexistente(partidaDetalhe);
			return false;
		}

		return true;
	}

	private void mensagemErroJogadorInexistente(PartidaDetalhe partidaDetalhe, String nomeJogador) {
		LogController.logError("Jogador " + nomeJogador
				+ " não encontrado. Partida Detalhe não salva. Detalhes: Numero de Controle: "
				+ partidaDetalhe.getPartida().getNumeroControle() + " killtime: " + partidaDetalhe.getKillTime());
	}

	private void mensagemErroWeaponInexistente(PartidaDetalhe partidaDetalhe) {
		LogController.logError("Weapon " + partidaDetalhe.getWeapon().getNome()
				+ " não encontrada. Partida Detalhe não salva. Detalhes: Numero de Controle: "
				+ partidaDetalhe.getPartida().getNumeroControle() + " killtime: " + partidaDetalhe.getKillTime());
	}

	public void salvar(PartidaDetalhe partidaDetalhe) {
		partidaDetalheRepository.save(partidaDetalhe);
	}

	public void salvarFromServidorJogo(PartidaDetalhe partidaDetalhe) {

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

		partidaDetalheRepository.save(partidaDetalhe);

	}

	public List<PartidaDetalhe> getPartidasDetalhePorPartida(Partida partida) {
		return partidaDetalheRepository.findByPartida(partida);
	}

}
