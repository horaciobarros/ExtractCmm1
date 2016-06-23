package cmm.controller;

import java.util.Date;
import java.util.List;

import cmm.dao.NotasFiscaisDao;
import cmm.service.ExtractorService;
import cmm.util.Util;

/**
 * 
 * @author jway Lagoa da Prata
 *
 */
public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {

		int nivelProcessamento = 4;
		boolean txtsTratados = true;

		System.out.println("Lagoa da Prata - Limpando o banco...");

		List<String> entidades = null;
		if (nivelProcessamento == 2) {
			entidades = extractorService.excluiParaProcessarNivel2();
		} else if (nivelProcessamento == 3) {
			entidades = extractorService.excluiParaProcessarNivel3();
		} else if (nivelProcessamento == 4) {
			entidades = extractorService.excluiParaProcessarNivel4();
		} else if (nivelProcessamento == 1) {
			entidades = extractorService.excluiParaProcessarNivel1();
		} else if (nivelProcessamento == 5) {
			entidades = extractorService.excluiGuiasNotasFiscais();
		}

		// limpando o banco
		for (String nomeEntidade : entidades) {
			try {
				extractorService.excluiDados(nomeEntidade);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println(
				"Lagoa da Prata - Leitura de arquivos txt - Início - Processando no nível: " + nivelProcessamento);
		List<String> dadosList;

		if (nivelProcessamento == 1) {

			System.out.println("Gravando contribuintes");
			dadosList = extractorService.lerArquivo("dados_cadastro");
			extractorService.processaDadosCadastro(dadosList);
			System.out.println("--- Fim de ajustes ---");

			System.out.println("Lendo prestador"); // prestador
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando prestador"); // prestador
			extractorService.processaDadosLivroPrestador(dadosList);
			System.out.println("--- Fim de prestador ---");

		}

		if (nivelProcessamento <= 3) {
			// competencias e guias
			System.out.println("Lendo competencias e guias");
			dadosList = extractorService.lerArquivo("dados_guia");
			System.out.println("Gravando competencias e guias");
			extractorService.processaDadosGuiaCompetencias(dadosList);
			System.out.println("--- Fim de competencias e guias ---");

			// atividades prestador
			System.out.println("Lendo atividades prestador");
			dadosList = extractorService.lerArquivo("dados_cadastro_atividade");
			System.out.println("Gravando atividades prestador");
			extractorService.processaDadosCadastroAtividade(dadosList);
			System.out.println("--- Fim de atividades prestador ---");

		}

		if (nivelProcessamento <= 4) {
			// notas fiscais
			System.out.println("Lendo notas fiscais - " + Util.getDateHourMinutes(new Date()));
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando notas fiscais - " + Util.getDateHourMinutes(new Date()));
			extractorService.processaDadosNotasFiscais(dadosList);
			System.out.println("--- Fim de notas fiscais ---" + Util.getDateHourMinutes(new Date()));
		}

	}

}
