package cmm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmm.service.ExtractorService;

public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {
		System.out.println("Leitura de arquivos txt - Início");

		/*		Arrays.asList("plano_conta", "dados_contador", "dados_cadastro_acesso", "dados_cadastro_atividade",
						"dados_cadastro", "dados_guia", "dados_livro_tomador", "dados_livro_prestador")); */
		
		// competencias
		List<String> dadosList = lerArquivo("dados_guia");
		extractorService.processaDadosGuia(dadosList);
		
		
		
		
	}

	public List<String> lerArquivo(String arquivoIn) {
		File file;
		file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			List<String> dadosList = new ArrayList<String>();
			try {
				br.readLine(); // cabeçalho
				while (br.ready()) {
					String linha = br.readLine();
					dadosList.add(linha);
				}
				br.close();
				fr.close();
				return dadosList;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}


	private void processaDados(String etapa, List<String> dadosList) {
		if (etapa == "PC") {
			extractorService.processaPlanoConta(dadosList);
		}
		if (etapa == "DC") {
			extractorService.processaDadosContador(dadosList);
		}
		if (etapa == "CA") {
			extractorService.processaDadosCadastroAcesso(dadosList);
		}
		if (etapa == "AT") {
			extractorService.processaDadosCadastroAtividade(dadosList);
		}
		if (etapa == "C") {
			extractorService.processaDadosCadastro(dadosList);
		}
		if (etapa == "DG") {
			extractorService.processaDadosGuia(dadosList);
		}
		if (etapa == "LT") {
			extractorService.processaDadosLivroTomador(dadosList);
		}
		if (etapa == "LP") {
			extractorService.processaDadosLivroPrestador(dadosList);
		}

	}

}
