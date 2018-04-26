package br.com.controlepartidascs.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.controlepartidascs.model.RankingWeapon;
import br.com.controlepartidascs.model.Weapon;

@Repository
public interface WeaponRepository extends CrudRepository<Weapon, Integer> {
	Weapon findByNome(String nome);

	@Query("SELECT COUNT(pd)"
			+ " FROM "
			+ "PartidaDetalhe pd "
			+ "inner join pd.partida p "
			+ "WHERE p.inicio BETWEEN :p1 AND :p2")
	List<RankingWeapon> getRankingWeapon(@Param("p1") ZonedDateTime data1, @Param("p2") ZonedDateTime data2);
}
