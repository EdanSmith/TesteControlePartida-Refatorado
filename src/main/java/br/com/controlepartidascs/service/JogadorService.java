package br.com.controlepartidascs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
