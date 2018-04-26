package br.com.controlepartidascs.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.RankingWeapon;
import br.com.controlepartidascs.model.Weapon;
import br.com.controlepartidascs.repository.PartidaRepository;
import br.com.controlepartidascs.repository.WeaponRepository;

@Service
public class WeaponService {

	@Autowired
	WeaponRepository weaponRepository;

	@Autowired
	PartidaRepository partidaRepository;

	@Autowired
	PartidaDetalheService partidaDetalheService;

	public Weapon getWeaponByName(String nome) {
		return weaponRepository.findByNome(nome);
	}

	public String getWeaponRankingByDate(String date1, String date2) {
		ZonedDateTime zdtInicio;
		ZonedDateTime zdtFim;

		if (date1 == null) {
			zdtInicio = null;
			zdtFim = null;
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			// convert String to LocalDate
			LocalDate localDate1 = LocalDate.parse(date1, formatter);
			LocalDate localDate2 = LocalDate.parse(date2, formatter);

			zdtInicio = localDate1.atStartOfDay(ZoneOffset.UTC);
			zdtFim = localDate2.atTime(23, 59, 59).atZone(ZoneOffset.UTC);
		}

		List<Partida> partidas = new ArrayList<Partida>();

		if (zdtInicio == null || zdtFim == null) {
			Iterable<Partida> partidasTemp = partidaRepository.findAll();
			partidasTemp.forEach(partidas::add);
		} else {
			partidas = partidaRepository.findByInicioBetween(zdtInicio, zdtFim);
		}

		Iterable<Weapon> weapons;
		List<Weapon> weapon = new ArrayList<Weapon>();

		weapons = weaponRepository.findAll();

		weapons.forEach(weapon::add);

		Collections.sort(weapon, Comparator.comparingInt(Weapon::getId));

		for (Partida partida : partidas) {
			List<PartidaDetalhe> partidasDetalhe = partidaDetalheService.getPartidasDetalhePorPartida(partida);
			for (PartidaDetalhe partidaDetalhe : partidasDetalhe) {
				weapon.get(weapon.indexOf(partidaDetalhe.getWeapon())).getRankingWeapon().adicionarKill();
			}
		}

		List<RankingWeapon> ranking = new ArrayList<RankingWeapon>();
		for (Weapon wep : weapon) {
			ranking.add(new RankingWeapon(wep.getNome(), wep.getRankingWeapon().getKills()));
		}

		Collections.sort(ranking, Comparator.comparingInt(RankingWeapon::getKills));
		Collections.reverse(ranking);

		for (int i = 0; i < ranking.size(); i++) {
			ranking.get(i).setPosicao(i + 1);
		}

		String json = new Gson().toJson(ranking);

		return json.toString();
	}

}
