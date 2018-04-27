package br.com.controlepartidascs.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.repository.JogadorRepository;

@Service
public class JogadorService {

	@Autowired
	JogadorRepository jogadorRepository;

	public void salvar(Jogador jogador) {
		jogadorRepository.save(jogador);
	}

	public Jogador findByNome(String nome) {
		return jogadorRepository.findByNome(nome);
	}

	public Iterable<Jogador> findAll() {
		return jogadorRepository.findAll();
	}

	public boolean verificarExistenciaJogadorPorNome(String nomeJogador) {
		Jogador jogador = jogadorRepository.findByNome(nomeJogador);
		if (jogador == null) {
			return false;
		}
		return true;
	}

	public void salvarJogadoresInexistentes(Set<Jogador> jogadores) {
		Jogador jogadorTemp;
		Boolean jogadorExistente;

		Iterator<Jogador> itr = jogadores.iterator();
		while (itr.hasNext()) {
			jogadorTemp = (Jogador) itr.next();
			jogadorExistente = verificarExistenciaJogadorPorNome(jogadorTemp.getNome());

			if (!jogadorExistente) {
				jogadorRepository.save(jogadorTemp);
				LogController.log("Jogador adicionado: " + jogadorTemp.getNome());
			}
		}
	}

}
