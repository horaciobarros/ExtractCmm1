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

		List<String> arquivoList = new ArrayList<>(
				Arrays.asList("plano_conta", "dados_contador", "dados_cadastro_acesso", "dados_cadastro_atividade",
						"dados_cadastro", "dados_guia", "dados_livro_tomador", "dados_livro_prestador"));

		File file;
		for (String arquivoIn : arquivoList) {
			file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				System.out.println("Lendo --> " + arquivoList.indexOf(arquivoIn) + " - " + arquivoIn);
				List<String> dadosList = new ArrayList<String>();
				try {
					br.readLine(); // cabeçalho
					while (br.ready()) {
						String linha = br.readLine();
						dadosList.add(linha);
					}
					br.close();
					fr.close();
					processaDados(arquivoList.indexOf(arquivoIn), dadosList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			

		}

		System.out.println("Leitura de arquivos txt - Final");
		
		System.out.println("Gravação de banco de dados para exportação - inicio");
		
		gravaDados();
		
		System.out.println("Gravação de banco de dados para exportação - fim");
	}

	private void gravaDados() {
		
		
	}

	private void processaDados(int index, List<String> dadosList) {
		if (index == 0) {
			extractorService.processaPlanoConta(dadosList);
		}
		if (index == 1) {
			extractorService.processaDadosContador(dadosList);
		}
		if (index == 2) {
			extractorService.processaDadosCadastroAcesso(dadosList);
		}
		if (index == 3) {
			extractorService.processaDadosCadastroAtividade(dadosList);
		}
		if (index == 4) {
			extractorService.processaDadosCadastro(dadosList);
		}
		if (index == 5) {
			extractorService.processaDadosGuia(dadosList);
		}
		if (index == 6) {
			extractorService.processaDadosLivroTomador(dadosList);
		}
		if (index == 7) {
			extractorService.processaDadosLivroPrestador(dadosList);
		}
		
		
	}

}
