package br.com.controlepartidascs.controller;

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

	@RequestMapping(value = "/gravar", method = RequestMethod.POST, consumes = "application/json")
	public String gravarPartida(@RequestBody Partida partida) {

		partidaService.salvarPartidaComSomenteNomeDeJogadores(partida);
		
		LogController.log("Gravando Partida");
		
		return partida.toString();
	}

	@RequestMapping(value = "/rankingJogadores", method = RequestMethod.GET, produces = "application/json")
	public String rankingJogadores(@RequestParam(required = false, name = "data") String date) {
		String ranking = partidaService.getRankingByDate(date);
		
		LogController.log("Ranking de Jogadores Visualizado");
		
		return ranking;
	}
	
	@RequestMapping(value = "/rankingArmas", method = RequestMethod.GET, produces = "application/json")
	public String rankingWeapons(@RequestParam(required = false, name = "dataIni") String dateIni,
			@RequestParam(required = false, name = "dataFim") String dateFim) {
		
		String ranking = weaponService.getWeaponRankingByDate(dateIni, dateFim);
		
		LogController.log("Ranking de armas visualizado");
		
		return ranking;
	}
	
	@RequestMapping(value = "/listarPartidas", method = RequestMethod.GET, produces = "application/json")
	public String listarPartida() {
		
		String partidaComDetalhes = partidaService.findTodasPartidas();
		
		LogController.log("Lista de todas as partidas visualizada");
		
		return partidaComDetalhes;
	}
	
	@RequestMapping(value = "/listarPartidaDetalhada", method = RequestMethod.GET, produces = "application/json")
	public String listarPartidaDetalhada(@RequestParam(required = false, name = "matchId") String id) {
		
		String partidaComDetalhes = partidaService.findPartidaByNumeroControle(Integer.parseInt(id));
		
		LogController.log("Lista de uma partida detalhada foi visualizada");
		
		return partidaComDetalhes;
	}
}
