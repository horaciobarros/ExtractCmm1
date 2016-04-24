package cmm.controller;

import java.util.Arrays;
import java.util.List;

import cmm.service.ExtractorService;

public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {

		int nivelProcessamento = 1;

		System.out.println("Limpando o banco...");

		List<String> entidades;
		if (nivelProcessamento == 1) {
			entidades = processaTudo();
		} else {
			entidades = processaNivel2();
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
			// Ajustando tomadores e prestadores através do dadosCadastro.txt
			System.out.println("Ajustando contribuintes");
			dadosList = extractorService.lerArquivo("dados_cadastro");
			extractorService.processaDadosCadastro(dadosList);
			System.out.println("--- Fim de ajustes ---");

			System.out.println("Lendo prestador"); // prestador
			dadosList = extractorService.lerArquivo("dados_livro_prestador", 64);
			extractorService.processaDadosLivroPrestador(dadosList);
			System.out.println("--- Fim de prestador ---");

			System.out.println("Lendo tomador");
			dadosList = extractorService.lerArquivo("dados_livro_tomador", 66);
			extractorService.processaDadosLivroTomador(dadosList);
			System.out.println("--- Fim de tomador ---");
		}

		new Thread() {
			@Override
			public void run() {
				// competencias e guias
				System.out.println("Lendo competencias e guias");
				List<String> dadosList;
				dadosList = extractorService.lerArquivo("dados_guia");
				extractorService.processaDadosGuiaCompetencias(dadosList);
				System.out.println("--- Fim de competencias e guias ---");
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				// notas fiscais
				System.out.println("Lendo notas fiscais");
				List<String> dadosList;
				dadosList = extractorService.lerArquivo("dados_livro_prestador", 64);
				extractorService.processaDadosNotasFiscais(dadosList);
				System.out.println("--- Fim de notas fiscais ---");
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				List<String> dadosList;
				// atividades prestador
				System.out.println("Lendo atividades prestador");
				dadosList = extractorService.lerArquivo("dados_cadastro_atividade");
				extractorService.processaDadosCadastroAtividade(dadosList);
				System.out.println("--- Fim de atividades prestador ---");
			}
		}.start();

		
		

	}

	private List<String> processaTudo() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores", "Prestadores");

	}

	private List<String> processaNivel2() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais");

	}

}
