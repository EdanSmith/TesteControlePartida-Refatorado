package br.com.controlepartidascs.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;

@Repository
public interface PartidaRepository extends CrudRepository<Partida, Integer> {
	Partida findByNumeroControle(Integer numeroControle);
	
	List<Partida> findByInicioBetween(ZonedDateTime inicio, ZonedDateTime fim);
	
	@Query("SELECT DISTINCT p.jogador "
			+ " FROM "
			+ "Partida p "
			+ "WHERE p.numeroControle = :p1")
	List<Jogador> findJogadoresDaPartida(@Param("p1") Integer numeroControle);
}
