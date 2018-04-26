package br.com.controlepartidascs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;

@Repository
public interface PartidaDetalheRepository extends CrudRepository<PartidaDetalhe, Integer>{
	List<PartidaDetalhe> findByPartida(Partida partida);
}
