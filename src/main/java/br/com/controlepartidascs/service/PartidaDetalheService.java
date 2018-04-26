package br.com.controlepartidascs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.Weapon;
import br.com.controlepartidascs.repository.JogadorRepository;
import br.com.controlepartidascs.repository.PartidaDetalheRepository;
import br.com.controlepartidascs.repository.PartidaRepository;

@Service
public class PartidaDetalheService {

	@Autowired
	PartidaDetalheRepository partidaDetalheRepository;

	@Autowired
	PartidaRepository partidaRepository;

	@Autowired
	JogadorRepository jogadorRepository;

	@Autowired
	WeaponService weaponService;

	LogController log = new LogController();

	public void salvar(PartidaDetalhe partidaDetalhe) {
		partidaDetalheRepository.save(partidaDetalhe);
	}

	public void salvarFromServidorJogo(PartidaDetalhe partidaDetalhe) {

		Partida partida;
		partida = partidaRepository.findByNumeroControle(partidaDetalhe.getPartida().getNumeroControle());
		partidaDetalhe.setPartida(partida);

		Jogador jogadorKiller;
		jogadorKiller = jogadorRepository.findByNome(partidaDetalhe.getKiller().getNome());
		partidaDetalhe.setKiller(jogadorKiller);

		Jogador jogadorKilled;
		jogadorKilled = jogadorRepository.findByNome(partidaDetalhe.getKilled().getNome());
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
