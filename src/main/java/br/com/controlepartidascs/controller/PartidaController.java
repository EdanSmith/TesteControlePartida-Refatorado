package br.com.controlepartidascs.controller;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.service.JogadorService;
import br.com.controlepartidascs.service.PartidaService;
import br.com.controlepartidascs.service.WeaponService;

@RestController
@RequestMapping(value = "/partida")
public class PartidaController {

	@Autowired
	PartidaService partidaService;

	@Autowired
	JogadorService jogadorService;

	@Autowired
	WeaponService weaponService;

	/**
	 * Criado para teste de gravação de partidas
	 * @param partida
	 * @return
	 */
	@RequestMapping(value = "/gravar", method = RequestMethod.POST, consumes = "application/json")
	public Response gravarPartida(@Valid @RequestBody Partida partida) {

		try {
			partidaService.salvarPartidaComSomenteNomeDeJogadores(partida);
		} catch (Exception e) {
			LogController.logWarning("Tentativa falha em gravação de partida");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		LogController.log("Gravando Partida " + partida.getNumeroControle());
		return Response.ok("Partida número " + partida.getNumeroControle() + " Gravada com sucesso").build();
	}

	/**
	 * Ranking Jogadores - EX 2
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/rankingJogadores", method = RequestMethod.GET, produces = "application/json")
	public Response rankingJogadores(@Valid @RequestParam(required = false, name = "data") String date) {

		String ranking = "";

		try {
			ranking = partidaService.getRankingByDate(date);
		} catch (NullPointerException e) {
			return Response.ok("Nenhum ranking encontrado na data de: " + date).build();
		}

		LogController.log("Ranking de Jogadores Visualizado");
		return Response.ok(ranking).build();
	}

	/**
	 * Ranking Armas - EX 4
	 * @param dateIni
	 * @param dateFim
	 * @return
	 */
	@RequestMapping(value = "/rankingArmas", method = RequestMethod.GET, produces = "application/json")
	public Response rankingWeapons(@Valid @RequestParam(required = false, name = "dataIni") String dateIni,
			@Valid @RequestParam(required = false, name = "dataFim") String dateFim) {

		String ranking = "";

		try {
			ranking = weaponService.getWeaponRankingByDate(dateIni, dateFim);
		} catch (NullPointerException e) {
			return Response.ok("Nenhum ranking encontrado na data entre " + dateIni + " e " + dateFim).build();
		}

		LogController.log("Ranking de armas visualizado");

		return Response.ok(ranking).build();
	}

	/**
	 * Lista de Partidas - EX 5
	 * @return
	 */
	@RequestMapping(value = "/listarPartidas", method = RequestMethod.GET, produces = "application/json")
	public Response listarPartida() {

		String partidaComDetalhes = partidaService.findTodasPartidas();

		LogController.log("Lista de todas as partidas visualizada");
		return Response.ok(partidaComDetalhes).build();
	}
}
