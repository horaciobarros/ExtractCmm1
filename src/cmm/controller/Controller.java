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
		
		String msg = "Confirma Extract de Lagoa da Prata?";
		int op = JOptionPane.showConfirmDialog(null, msg,"", JOptionPane.YES_NO_OPTION);
		if (op != JOptionPane.YES_OPTION){
			System.exit(0);
		}

		int nivelProcessamento = 5;

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
			entidades = extractorService.excluiParaProcessarNivel5();
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
				"Lagoa da Prata - Leitura de arquivos txt - Início Processando no nível: " + nivelProcessamento);
		List<String> dadosList;

		if (nivelProcessamento == 1) {

			System.out.println("Lendo contribuintes");
			dadosList = extractorService.lerArquivo("dados_cadastro");
			System.out.println("Gravando contribuintes");
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

		}

		if (nivelProcessamento <= 4) {
			// atividades prestador
			System.out.println("Lendo atividades prestador");
			dadosList = extractorService.lerArquivo("dados_cadastro_atividade");
			System.out.println("Gravando atividades prestador");
			extractorService.processaDadosCadastroAtividade(dadosList);
			System.out.println("--- Fim de atividades prestador ---");

		}
		
		if (nivelProcessamento <= 5) {
			// notas fiscais
			System.out.println("Lendo notas fiscais - " + Util.getDateHourMinutes(new Date()));
			dadosList = extractorService.lerArquivo("dados_livro_prestador");
			System.out.println("Gravando notas fiscais - " + Util.getDateHourMinutes(new Date()));
			extractorService.processaDadosNotasFiscais(dadosList);
			System.out.println("--- Fim de notas fiscais ---" + Util.getDateHourMinutes(new Date()));
		}
		
		//desligarComputador();
	}
	
	/**
	 * Mï¿½todo para desligar o computador
	 * ATENï¿½ï¿½O: Para cancelar, entrar no cmd e digitar: shutdown -a
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
