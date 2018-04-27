package br.com.controlepartidascs.controller;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlepartidascs.service.PartidaDetalheService;
import br.com.controlepartidascs.service.PartidaService;

@RestController
@RequestMapping(value = "/populaBanco")
public class PopulaBancoController {

	@Autowired
	PartidaService partidaService;

	@Autowired
	PartidaDetalheService partidaDetalheService;

	/**
	 * Carregar histórico de partidas - EX 1
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public Response popularBancoComJson() {
		try {
			partidaService.popularBanco();
			partidaDetalheService.popularBanco();
		} catch (Exception e) {
			LogController.logError(
					"Erro na tentativa de popular o Banco. Lembre-se de criar a estrutura do banco com os arquivos em anexo");
			Response.status(Response.Status.BAD_REQUEST);
		}

		LogController.log("Banco populado");

		return Response.ok("Banco Populado com sucesso").build();
	}
}
