package cmm.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

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

		int nivelProcessamento = 1; // a partir de notas

		String msg = "Confirma Extract de Lagoa da Prata no n�vel " + nivelProcessamento + "?";
		int op = JOptionPane.showConfirmDialog(null, msg, "", JOptionPane.YES_NO_OPTION);
		if (op != JOptionPane.YES_OPTION) {
			System.exit(0);
		}

		System.out.println("Lagoa da Prata - Limpando o banco no n�vel " + nivelProcessamento);

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
			entidades = extractorService.excluiParaProcessarNivel5();
		} else if (nivelProcessamento == 6) {
			entidades = extractorService.excluiParaProcessarNivel6();
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
				"Lagoa da Prata - Leitura de arquivos txt - In�cio Processando no n�vel: " + nivelProcessamento);
		List<String> dadosList;

		// pessoa e prestadores
		if (nivelProcessamento == 1) {

			System.out.println("Lendo contribuintes " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("dados_cadastro");
			System.out.println("Gravando contribuintes " + Util.getDataHoraAtual());
			extractorService.processaDadosCadastro(dadosList);
			System.out.println("--- Fim de contribuintes ---" + Util.getDataHoraAtual());
			Util.pausar(5000);
			System.out.println("Lendo prestador " + Util.getDataHoraAtual()); // prestador
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando prestador " + Util.getDataHoraAtual()); // prestador
			extractorService.processaDadosLivroPrestador(dadosList);
			System.out.println("--- Fim de prestador --- " + Util.getDataHoraAtual());Util.pausar(5000);

		}

		// competencias e guias
		if (nivelProcessamento <= 3) {
			// competencias e guias
			System.out.println("Gravando competencias " + Util.getDataHoraAtual());
			extractorService.incluiCompetencias();Util.pausar(5000);
			System.out.println("--- Fim de competencias ---");
			System.out.println("Lendo guias " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("dados_guia");
			System.out.println("Gravando guias " + Util.getDataHoraAtual());
			extractorService.processaDadosGuiaCompetencias(dadosList);Util.pausar(5000);
			System.out.println("--- Fim de guias --- " + Util.getDataHoraAtual());

		}

		// atividades prestador
		if (nivelProcessamento <= 4) {
			// atividades prestador
			System.out.println("Lendo atividades prestador " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("dados_cadastro_atividade");
			System.out.println("Gravando atividades prestador " + Util.getDataHoraAtual());
			extractorService.processaDadosCadastroAtividade(dadosList);Util.pausar(5000);
			System.out.println("--- Fim de atividades prestador ---");

		}

		// tomadores
		if (nivelProcessamento <= 5) { // s� tomadores
			// notas fiscais
			System.out.println("Lendo dados de tomadores - " + Util.getDateHourMinutes(new Date()));
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando tomadores - " + Util.getDateHourMinutes(new Date()));
			extractorService.processaDadosTomadores(dadosList);Util.pausar(5000);
		}

		// notas fiscais e todos os relacionamentos
		if (nivelProcessamento <= 6) {
			System.out.println("Lendo notas fiscais - " + Util.getDateHourMinutes(new Date()));
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando notas fiscais - " + Util.getDateHourMinutes(new Date()));
			extractorService.processaDadosNotasFiscais(dadosList);Util.pausar(5000);
			System.out.println("--- Fim de notas fiscais ---" + Util.getDateHourMinutes(new Date()));
		}

		// ajustes na base j� gravada
		if (nivelProcessamento <= 7) {

			System.out.println("Iniciando processo de excluir guias sem notas... ");
			extractorService.excluiGuiasSemNotas();

			System.out.println("Limpando Prestadores Sem Notas");
			extractorService.processaExclusaoPrestadoresSemNotas();
			
			System.out.println("Ajustando Servicos");
			extractorService.processaAjustesEmServicos();
		}

		System.out.println("--- Processo encerrado. " + Util.getDataHoraAtual() + " Registros gravados: ");

		for (String nomeEntidade : entidades) {
			try {
				System.out.println(nomeEntidade + " -->" + extractorService.count(nomeEntidade));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// desligarComputador();
	}

	/**
	 * M�todo para desligar o computador ATEN��O: Para cancelar, entrar no
	 * cmd e digitar: shutdown -a
	 */
	public static void desligarComputador() {
		try {
			Runtime.getRuntime().exec("shutdown -s -t 480");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
