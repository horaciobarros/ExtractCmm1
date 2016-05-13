package cmm.controller;

import java.util.List;

import cmm.service.ExtractorService;

/**
 * 
 * @author jway
 *
 */
public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {

		int nivelProcessamento = 2;

		System.out.println("Limpando o banco...");

		List<String> entidades;
		if (nivelProcessamento == 2) {
			entidades = extractorService.excluiParaProcessarNivel2();
		} else if (nivelProcessamento == 3) {
			entidades = extractorService.excluiParaProcessarNivel3();
		} else if (nivelProcessamento == 4) {
			entidades = extractorService.excluiParaProcessarNivel4();
		} else {
			entidades = extractorService.excluiParaProcessarNivel1();
		}

		// limpando o banco
		for (String nomeEntidade : entidades) {
			try {
				extractorService.excluiDados(nomeEntidade);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Leitura de arquivos txt - Início");
		List<String> dadosList;

		if (nivelProcessamento == 1) {
			// Ajustando tomadores e prestadores atrav�s do dadosCadastro.txt
			System.out.println("Ajustando contribuintes");
			dadosList = extractorService.lerArquivo("dados_cadastro");
			extractorService.processaDadosCadastro(dadosList);
			System.out.println("--- Fim de ajustes ---");

			System.out.println("Lendo prestador"); // prestador
			dadosList = extractorService.lerArquivo("dados_livro_prestador", 64);
			extractorService.processaDadosLivroPrestador(dadosList);
			System.out.println("--- Fim de prestador ---");

		}
		
		if (nivelProcessamento <= 2) {
			System.out.println("Lendo tomador");
			dadosList = extractorService.lerArquivo("dados_livro_tomador", 66);
			extractorService.processaDadosLivroTomador(dadosList);
			System.out.println("--- Fim de tomador ---");
			
		}

		if (nivelProcessamento <= 3) {
			// competencias e guias
			System.out.println("Lendo competencias e guias");
			dadosList = extractorService.lerArquivo("dados_guia");
			extractorService.processaDadosGuiaCompetencias(dadosList);
			System.out.println("--- Fim de competencias e guias ---");

			// atividades prestador
			System.out.println("Lendo atividades prestador");
			dadosList = extractorService.lerArquivo("dados_cadastro_atividade");
			extractorService.processaDadosCadastroAtividade(dadosList);
			System.out.println("--- Fim de atividades prestador ---");

		}

		// notas fiscais
		System.out.println("Lendo notas fiscais");
		dadosList = extractorService.lerArquivo("dados_livro_prestador", 64);
		System.out.println("Processando notas fiscais");
		extractorService.processaDadosNotasFiscais(dadosList);
		System.out.println("--- Fim de notas fiscais ---");
	}

}
