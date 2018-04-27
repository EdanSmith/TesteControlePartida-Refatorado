package br.com.controlepartidascs.controller;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.service.PartidaDetalheService;

@RestController
@RequestMapping(value = "/partidaDetalhe")
public class PartidaDetalheController {

	@Autowired
	PartidaDetalheService partidaDetalheService;

	/**
	 * @param partidaDetalhe
	 * @return
	 */
	@RequestMapping(value = "/gravar", method = RequestMethod.POST, consumes = "application/json")
	public Response gravarPartida(@RequestBody List<PartidaDetalhe> partidaDetalhe) {
		try {
			for (PartidaDetalhe pd : partidaDetalhe) {
				partidaDetalheService.salvarFromServidorJogo(pd);
			}
		} catch (Exception e) { // Melhorar tratamento para n�o salvar s� metade.
			LogController.logWarning("Tentativa falha em grava��o de partida detalhe");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		LogController.log("Partida Detalhes foram Salvas.");

		return Response.ok("Partida Detalhes foram Salvas").build();
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET, produces = "application/json")
	public Response listarPartidaDetalhada(@RequestParam(required = false, name = "matchId") String id) {
		String partidaComDetalhes;

		try {
			partidaComDetalhes = partidaDetalheService.getRankingPartidaDetalheByNumeroControle(Integer.parseInt(id));
		} catch (NullPointerException e) {
			return Response.ok("Nenhum ranking encontrado com o n�mero de partida " + id).build();
		}

		LogController.log("Lista da partida detalhada " + id.toString() + " foi visualizada.");

		return Response.ok(partidaComDetalhes).build();
	}
}