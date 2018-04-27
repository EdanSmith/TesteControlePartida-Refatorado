package br.com.controlepartidascs.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.RankingWeapon;
import br.com.controlepartidascs.model.Weapon;
import br.com.controlepartidascs.repository.WeaponRepository;

@Service
public class WeaponService {

	@Autowired
	WeaponRepository weaponRepository;

	@Autowired
	PartidaService partidaService;

	@Autowired
	PartidaDetalheService partidaDetalheService;

	public boolean verificarExistenciaPorNome(String nome) {
		Weapon weapon = weaponRepository.findByNome(nome);
		if (weapon == null) {
			return false;
		}
		return true;
	}

	public void salvar(Weapon weapon) {
		weaponRepository.save(weapon);
	}

	public Weapon getWeaponByName(String nome) {
		return weaponRepository.findByNome(nome);
	}

	public String getWeaponRankingByDate(String dateIni, String dateFim) throws NullPointerException {
		ZonedDateTime zdtInicio;
		ZonedDateTime zdtFim;
		LocalDate localDateIni = LocalDate.now();
		LocalDate localDateFim = LocalDate.now();

		if (dateIni == null) {
			zdtInicio = null;
			zdtFim = null;
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			try {
				// convert String to LocalDate
				localDateIni = LocalDate.parse(dateIni, formatter);
				localDateFim = LocalDate.parse(dateFim, formatter);
			} catch (DateTimeParseException e) {
				LogController.logWarning("Data inserida inválida ao tentar visualizar ranking de armas");
				throw new NullPointerException(
						"Data inserida inválida por usuário ao tentar visualizar o ranking de armas");
			}

			zdtInicio = localDateIni.atStartOfDay(ZoneOffset.UTC);
			zdtFim = localDateFim.atTime(23, 59, 59).atZone(ZoneOffset.UTC);
		}

		List<Partida> partidas = new ArrayList<Partida>();

		if (zdtInicio == null || zdtFim == null) {
			Iterable<Partida> partidasTemp = partidaService.findAll();
			partidasTemp.forEach(partidas::add);
		} else {
			partidas = partidaService.findByInicioBetween(zdtInicio, zdtFim);
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
