package br.com.controlepartidascs.dados;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.controlepartidascs.controller.LogController;
import br.com.controlepartidascs.model.Jogador;
import br.com.controlepartidascs.model.Partida;

public class PopulaBancoComJson {

	public static void main(String[] args) {
		PopulaBancoComJson populaBancoJson = new PopulaBancoComJson();

		JsonElement je = populaBancoJson.getJsonArquivo();

		populaBancoJson.transformarJsonEmEntidade(je);
	}

	public JsonElement getJsonArquivo() {

		String fileName = "/json/matches.json";
		List<String> lines = new ArrayList<String>();

		URI uri;
		try {
			uri = this.getClass().getResource(fileName).toURI();

			lines = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonElement json = new Gson().toJsonTree(lines);

		LogController.logDebug(json.toString());
		return json;
	}

	public void transformarJsonEmEntidade(JsonElement jsonElement) {
		JsonArray jsonArray = (JsonArray) jsonElement;

		for (JsonElement jsonEle : jsonArray) {
			Partida partida = new Partida();

			LogController.log(jsonEle.toString());
			JsonObject jsonObject = jsonEle.getAsJsonObject();

			partida.setNumeroControle(jsonObject.get("match").getAsInt());

			Set<Jogador> jogador = new HashSet<>();

			List<String> nomeJogador = new ArrayList<>();

			JsonArray ja = jsonObject.get("players").getAsJsonArray();

			// TODO Terminar carregamento do arquivo
		}
	}

	public void salvarEmBancoDeDados() {
		// TODO Terminar gravacao do arquivo
	}

}
