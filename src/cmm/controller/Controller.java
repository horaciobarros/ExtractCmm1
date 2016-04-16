package cmm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmm.service.ExtractorService;

public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {

		System.out.println("Limpando o banco...");

		/*
		 * List<String> entidades = Arrays.asList("Guias", "Competencias",
		 * "Tomadores", "Prestadores", "GuiasNotasFiscais", "NotasFiscais",
		 * "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
		 * "NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores",
		 * "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores",
		 * "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades", "" +
		 * "PrestadoresOptanteSimples");
		 */

		List<String> entidades = Arrays.asList("GuiasNotasFiscais", 
				"NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores",
				"NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades", "" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais");

		// limpando o banco
		for (String nomeEntidade : entidades) {
			try {
				extractorService.excluiDados(nomeEntidade);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Leitura de arquivos txt - In�cio");
		List<String> dadosList;

		/*
		 * // Ajustando tomadores e prestadores atrav�s do dadosCadastro.txt
		 * System.out.println("Ajustando contribuintes"); dadosList =
		 * lerArquivo("dados_cadastro");
		 * extractorService.processaDadosCadastro(dadosList);
		 * 
		 * System.out.println("Lendo prestador"); // prestador dadosList =
		 * lerArquivo("dados_livro_prestador");
		 * extractorService.processaDadosLivroPrestador(dadosList);
		 * 
		 * // tomador System.out.println("Lendo tomador"); dadosList =
		 * lerArquivo("dados_livro_tomador");
		 * extractorService.processaDadosLivroTomador(dadosList);
		 */

		// competencias e guias
		System.out.println("Lendo competencias e guias");
		dadosList = lerArquivo("dados_guia");
		extractorService.processaDadosGuiaCompetencias(dadosList);

		// notas fiscais
		System.out.println("Lendo notas fiscais");
		dadosList = lerArquivo("dados_livro_prestador");
		extractorService.processaDadosNotasFiscais(dadosList);

		System.out.println("--- Fim do processo ---");

	}

	public List<String> lerArquivo(String arquivoIn) {
		File file;
		file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			List<String> dadosList = new ArrayList<String>();
			try {
				br.readLine(); // cabe�alho
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
	
}
