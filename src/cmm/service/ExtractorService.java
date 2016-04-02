package cmm.service;

import java.util.List;

import cmm.entidadesOrigem.DadosCadastro;
import cmm.entidadesOrigem.DadosCadastroAcesso;
import cmm.entidadesOrigem.DadosCadastroAtividade;
import cmm.entidadesOrigem.DadosContador;
import cmm.entidadesOrigem.DadosGuia;
import cmm.entidadesOrigem.PlanoConta;

public class ExtractorService {

	public void processaPlanoConta(List<String> dadosList) {
		for (String linha : dadosList) {
			String[] arrayLinha = linha.split("\\|");
			PlanoConta pc = new PlanoConta(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
					arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10],
					arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14]);
			

		}

	}

	public void processaDadosContador(List<String> dadosList) {
		for (String linha : dadosList) {
			String[] arrayLinha = linha.split("\\|");
			DadosContador dc = new DadosContador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5],
					arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11],
					arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19]);
			

		}

	}

	public void processaDadosCadastroAcesso(List<String> dadosList) {
		for (String linha : dadosList) {
			String[] arrayLinha = linha.split("\\|");
			DadosCadastroAcesso dca = new DadosCadastroAcesso(arrayLinha[0], arrayLinha[1]);
			

		}

	}

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		for (String linha : dadosList) {
			String[] arrayLinha = linha.split("\\|");
			DadosCadastroAtividade dca = new DadosCadastroAtividade(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], Double.valueOf(arrayLinha[5]),
					arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11],
					arrayLinha[12]);
			

		}

	}

	public void processaDadosCadastro(List<String> dadosList) {
		for (String linha : dadosList) {
			String[] arrayLinha = linha.split("\\|");
			DadosCadastro dc = new DadosCadastro(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5],
					arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11],
					arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], 
					arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22]);
			

		}

	}

	public void processaDadosGuia(List<String> dadosList) {
		int linhaArquivo = 2;
		try {
		for (String linha : dadosList) {
			linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("#");
			DadosGuia dg = new DadosGuia(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5],
					arrayLinha[6], Double.valueOf(arrayLinha[7]), Double.valueOf(arrayLinha[8]), Double.valueOf(arrayLinha[9]), 
					Double.valueOf(arrayLinha[10]), Double.valueOf(arrayLinha[11]), arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16],
					arrayLinha[17], arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22], arrayLinha[23],
					arrayLinha[24], 
					arrayLinha[25], arrayLinha[26], arrayLinha[27], arrayLinha[28], Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]), arrayLinha[31],
					arrayLinha[32], arrayLinha[33], arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40], 
					arrayLinha[41]);
			
			linhaArquivo++;
		}
		} catch (Exception e) {
			System.out.println("Erro linha " + linhaArquivo + ": " + e);
		}

	}

	private String preparaParaSplit(String linha) {
		linha = linha.replaceAll("\\|", "#");
		linha = linha.replaceAll("##", "# #");
		String ultCarac = linha.substring(linha.length() - 1);
		if (ultCarac.equals("#")) {
			linha = linha + " ";
		}
		return linha;
	}

	public void processaDadosLivroTomador(List<String> dadosList) {
		// TODO Auto-generated method stub

	}

	public void processaDadosLivroPrestador(List<String> dadosList) {
		// TODO Auto-generated method stub

	}

}
