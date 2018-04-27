package br.com.controlepartidascs.dados;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;
import br.com.controlepartidascs.model.PartidaDetalhe;
import br.com.controlepartidascs.model.Weapon;

public class PopulaBancoComJson {

	private List<JsonElement> getJsonFile(String path) {

		String fileName = path;
		List<String> lines = new ArrayList<String>();
		List<JsonElement> jsonElement = new ArrayList<>();
		JsonParser jp = new JsonParser();
		
		URI uri;
		try {
			uri = this.getClass().getResource(fileName).toURI();

			lines = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
			
            for (String line : lines) {
            	
                jsonElement.add( jp.parse(line));
            }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonElement;
	}

	private List<Partida> transformarJsonEmListaDePartidas(List<JsonElement> jsonElement) {

		List<Partida> partidas = new ArrayList<>();

		for (JsonElement je : jsonElement) {
			Partida partida = new Partida();
			try {
				JsonObject jo = je.getAsJsonObject();

				partida.setNumeroControle(jo.get("match").getAsInt());

				ZonedDateTime zdtIni = ZonedDateTime.parse(jo.get("begin").getAsString());
				partida.setInicio(zdtIni);

				ZonedDateTime zdtFim = ZonedDateTime.parse(jo.get("end").getAsString());
				partida.setFim(zdtFim);

				Set<Jogador> jogador = new HashSet<>();

				partida.setJogador(jogador);

				JsonArray ja = jo.get("players").getAsJsonArray();

				// LogController.logDebug(ja.toString());

				for (JsonElement jeJogador : ja) {
					JsonObject joJogador = jeJogador.getAsJsonObject();
					jogador.add(new Jogador(joJogador.get("name").getAsString()));
				}

				partida.setJogador(jogador);
				partidas.add(partida);

				LogController.logDebug("Salvando Partida" + partida.toString());

			} catch (IllegalStateException e) {
				// LogController.logWarning("Campo nulo encontrado em �ndices de listas no Json.
				// O campo foi ignorado");
				continue;
			}

		}

		return partidas;
	}

	private List<PartidaDetalhe> transformarJsonEmListaDePartidaDetalhe(List<JsonElement> jsonElement) {

		List<PartidaDetalhe> partidasDetalhe = new ArrayList<>();

		for (JsonElement je : jsonElement) {
			PartidaDetalhe partidaDetalhe = new PartidaDetalhe();

			try {
				JsonObject jo = je.getAsJsonObject();

				Partida partida = new Partida();
				partida.setNumeroControle(jo.get("match").getAsInt());

				partidaDetalhe.setPartida(partida);

				LocalTime killtime = LocalTime.parse(jo.get("killtime").getAsString());
				partidaDetalhe.setKillTime(killtime);

				Jogador jogadorKiller = new Jogador();
				jogadorKiller.setNome(jo.get("killer").getAsString());
				partidaDetalhe.setKiller(jogadorKiller);

				Jogador jogadorKilled = new Jogador();
				jogadorKilled.setNome(jo.get("killed").getAsString());
				partidaDetalhe.setKilled(jogadorKilled);

				try {
					Weapon weapon = new Weapon();
					weapon.setNome(jo.get("weapon").getAsString());
					partidaDetalhe.setWeapon(weapon);
				} catch (NullPointerException e) {
					LogController.logError("O Campo Weapon est� vazio, essa linha foi ignorada. Detalhes: Partida: "
							+ partida.getNumeroControle() + " Killtime: " + killtime + " Killer: " + jogadorKiller
							+ " Killed: " + jogadorKilled);
					continue;
				}

				partidasDetalhe.add(partidaDetalhe);

				LogController.logDebug("Salvando Partida" + partida.toString());

			} catch (IllegalStateException e) {
				// LogController.logWarning("Campo nulo encontrado em �ndices de listas no Json.
				// O campo foi ignorado");
				continue;
			}
		}

		return partidasDetalhe;
	}

	public List<Partida> getPartidasFromJsonFile() {
		PopulaBancoComJson populaBancoJson = new PopulaBancoComJson();
		List<JsonElement> je = populaBancoJson.getJsonFile("/json/matches.json");
		return populaBancoJson.transformarJsonEmListaDePartidas(je);
	}

	public List<PartidaDetalhe> getPartidasDetalheFromJsonFile() {
		PopulaBancoComJson populaBancoJson = new PopulaBancoComJson();
		List<JsonElement> je = populaBancoJson.getJsonFile("/json/wrangle.json");
		return populaBancoJson.transformarJsonEmListaDePartidaDetalhe(je);
	}

}
