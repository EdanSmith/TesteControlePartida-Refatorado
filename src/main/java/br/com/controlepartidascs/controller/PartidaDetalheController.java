package br.com.controlepartidascs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.service.PartidaDetalheService;

@RestController
@RequestMapping(value = "/partidaDetalhe")
public class PartidaDetalheController {

	@Autowired
	PartidaDetalheService partidaDetalheService;

	@RequestMapping(value = "/gravar", method = RequestMethod.POST, consumes = "application/json")
	public String gravarPartida(@RequestBody PartidaDetalhe partidaDetalhe) {

		partidaDetalhe.getKiller();

		partidaDetalheService.salvarFromServidorJogo(partidaDetalhe);

		LogController.log("Partida Detalhada Salva");

		return partidaDetalhe.toString();
	}
}