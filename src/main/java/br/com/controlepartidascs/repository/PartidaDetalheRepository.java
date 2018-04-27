package br.com.controlepartidascs.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;

@Repository
public interface PartidaDetalheRepository extends CrudRepository<PartidaDetalhe, Integer> {
	List<PartidaDetalhe> findByPartida(Partida partida);

	@Query("SELECT pd FROM PartidaDetalhe pd join pd.partida p join pd.jogadorKiller jk join pd.jogadorKilled jkd join pd.weapon w "
			+ "where p.numeroControle = :p1 AND pd.killTime = :p2 AND jk.id = :p3 AND jkd.id = :p4 AND w.id = :p5")
	PartidaDetalhe findByAllAttributes(@Param("p1") Integer idPartida, @Param("p2") LocalTime killtime,
			@Param("p3") Integer idKiller, @Param("p4") Integer idKilled, @Param("p5") Integer idWeapon);
}
