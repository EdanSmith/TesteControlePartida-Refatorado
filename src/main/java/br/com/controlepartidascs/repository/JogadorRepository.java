package br.com.controlepartidascs.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.controlepartidascs.model.Jogador;

@Repository
public interface JogadorRepository extends CrudRepository<Jogador, Integer> {
	Jogador findByNome(String nome);
}
