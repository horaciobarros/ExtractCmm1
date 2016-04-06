package cmm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmm.dao.CompetenciasDao;
import cmm.entidadesOrigem.DadosCadastro;
import cmm.entidadesOrigem.DadosCadastroAcesso;
import cmm.entidadesOrigem.DadosCadastroAtividade;
import cmm.entidadesOrigem.DadosContador;
import cmm.entidadesOrigem.DadosGuia;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.entidadesOrigem.DadosLivroTomador;
import cmm.entidadesOrigem.PlanoConta;
import cmm.model.Competencias;
import cmm.util.Util;

public class ExtractorService {
	File file = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	Map<Long, PlanoConta> planoContaMap;
	Map<Long, DadosContador> dadosContadorMap;
	Map<Long, DadosLivroPrestador>  dadosLivroPrestadorMap;
	Map<Long, DadosLivroTomador> dadosLivroTomadorMap;
	Map<String, DadosCadastro> dadosCadastroMap;
	Map<String, DadosCadastroAcesso> dadosCadastroAcessoMap;
	Map<String, DadosCadastroAtividade> dadosCadastroAtividadeMap;
	Map<String, DadosGuia> dadosGuiaMap;
	Util util = new Util();
	CompetenciasDao competenciasDao = new CompetenciasDao();

	public void processaPlanoConta(List<String> dadosList) {
		ativaFileLog("plano_conta");

		planoContaMap = new HashMap<Long, PlanoConta>();
		
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				PlanoConta pc = new PlanoConta(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

		}
		closeFileLog();

	}

	public void processaDadosContador(List<String> dadosList) {
		ativaFileLog("dados_contador");
		dadosContadorMap = new HashMap<Long, DadosContador>();
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosContador dc = new DadosContador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8],
						arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14],
						arrayLinha[15], arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

		}
		closeFileLog();

	}

	public void processaDadosCadastroAcesso(List<String> dadosList) {
		ativaFileLog("dados_cadastro_acesso");
		dadosCadastroAcessoMap = new HashMap<String, DadosCadastroAcesso>();
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastroAcesso dca = new DadosCadastroAcesso(arrayLinha[0], arrayLinha[1]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

		}
		closeFileLog();

	}

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		ativaFileLog("dados_cadastro_atividade");
		dadosCadastroAtividadeMap = new HashMap<String, DadosCadastroAtividade>();
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastroAtividade dca = new DadosCadastroAtividade(arrayLinha[0], arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], Double.valueOf(arrayLinha[5]), arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

		}
		closeFileLog();

	}

	public void processaDadosCadastro(List<String> dadosList) {
		ativaFileLog("dados_cadastro");
		dadosCadastroMap = new HashMap<String, DadosCadastro>();
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastro dc = new DadosCadastro(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15],
						arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21],
						arrayLinha[22]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

		}
		closeFileLog();

	}

	public void processaDadosGuia(List<String> dadosList) {
		ativaFileLog("dados_guia");
		dadosGuiaMap = new HashMap<String, DadosGuia>();
		Map<String, Competencias> competenciasMap = new HashMap<String, Competencias>();
		int linhaArquivo = 2;

		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosGuia dg = new DadosGuia(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
						arrayLinha[5], arrayLinha[6], Double.valueOf(arrayLinha[7]), Double.valueOf(arrayLinha[8]),
						Double.valueOf(arrayLinha[9]), Double.valueOf(arrayLinha[10]), Double.valueOf(arrayLinha[11]),
						arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17],
						arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22], arrayLinha[23],
						arrayLinha[24], arrayLinha[25], arrayLinha[26], arrayLinha[27], arrayLinha[28],
						Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]), arrayLinha[31], arrayLinha[32],
						arrayLinha[33], arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38],
						arrayLinha[39], arrayLinha[40], arrayLinha[41]);
				dadosGuiaMap.put(dg.getCodigo(), dg);
				String descricao = util.getMes(dg.getMes()) + "/" + dg.getAno();
				Competencias cp = competenciasDao.findByDescricao(descricao);
				if (cp == null) {
					cp = new Competencias();
					cp.setDescricao(descricao);
					cp.setDataInicio(util.getStringToDate(dg.getDataBoleto()));
					cp.setDataInicio(util.getStringToDate(dg.getDataBoleto()));
					cp.setDataVencimento(util.getStringToDate(dg.getDataBoleto()));
					competenciasDao.save(cp);
				} else {
					
				}
				
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

			linhaArquivo++;

		}
		
		closeFileLog();

	}

	

	public void processaDadosLivroTomador(List<String> dadosList) {
		ativaFileLog("dados_livro_tomador");
		dadosLivroTomadorMap = new HashMap<Long, DadosLivroTomador>();
		int linhaArquivo = 2;

		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroTomador dlt = new DadosLivroTomador(
						Long.valueOf(arrayLinha[0]),                   
						arrayLinha[1], 
						arrayLinha[2],
						arrayLinha[3], 
						arrayLinha[4], 
						arrayLinha[5], 
						arrayLinha[6], 
						arrayLinha[7], 
						arrayLinha[8],
						arrayLinha[9], 
						arrayLinha[10], 
						arrayLinha[11], 
						arrayLinha[12], 
						arrayLinha[13], 
						arrayLinha[14],
						arrayLinha[15], 
						arrayLinha[16], 
						arrayLinha[17], 
						Double.valueOf(arrayLinha[18]),
						Double.valueOf(arrayLinha[19]), 
						Double.valueOf(arrayLinha[20]), 
						Double.valueOf(arrayLinha[21]),
						Double.valueOf(arrayLinha[22]), 
						Double.valueOf(arrayLinha[23]), 
						Double.valueOf(arrayLinha[24]),
						arrayLinha[25], 
						Double.valueOf(arrayLinha[26]), 
						Double.valueOf(arrayLinha[27]),
						Double.valueOf(arrayLinha[28]), 
						Double.valueOf(arrayLinha[29]), 
						Double.valueOf(arrayLinha[30]),
						Double.valueOf(arrayLinha[31]), 
						Double.valueOf(arrayLinha[32]), 
						Double.valueOf(arrayLinha[33]),
						arrayLinha[34], 
						arrayLinha[35], 
						arrayLinha[36], 
						arrayLinha[37], 
						arrayLinha[38], 
						arrayLinha[39],
						arrayLinha[40], 
						arrayLinha[41], 
						arrayLinha[42], 
						arrayLinha[43], 
						arrayLinha[44], 
						arrayLinha[45],
						arrayLinha[46], 
						arrayLinha[47], 
						arrayLinha[48], 
						arrayLinha[49], 
						arrayLinha[50], 
						arrayLinha[51],
						arrayLinha[52], 
						arrayLinha[53], 
						arrayLinha[54], 
						arrayLinha[55], 
						arrayLinha[56], 
						arrayLinha[57],
						arrayLinha[58], 
						arrayLinha[59], 
						arrayLinha[60], 
						arrayLinha[61], 
						arrayLinha[62], 
						arrayLinha[63],
						arrayLinha[64], 
						arrayLinha[65]);
			} catch (Exception e) {
				fillErrorLog(linha, e);
			}

			linhaArquivo++;
		}
		closeFileLog();

	}

	public void processaDadosLivroPrestador(List<String> dadosList) {
		ativaFileLog("dados_livro_prestador");
		dadosLivroPrestadorMap = new HashMap<Long, DadosLivroPrestador>();
		int linhaArquivo = 2;

		for (String linha : dadosList) {
			try {

				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroPrestador dlp = new DadosLivroPrestador(
						Long.valueOf(arrayLinha[0]),              
						arrayLinha[1],							
						arrayLinha[2], 
						arrayLinha[3], 
						arrayLinha[4], 
						arrayLinha[5], 
						arrayLinha[6], 
						arrayLinha[7],
						arrayLinha[8], 
						arrayLinha[9], 
						arrayLinha[10], 
						arrayLinha[11], 
						arrayLinha[12], 
						arrayLinha[13],
						arrayLinha[14], 
						arrayLinha[15], 
						arrayLinha[16], 
						arrayLinha[17], 
						Double.valueOf(arrayLinha[18]),
						Double.valueOf(arrayLinha[19]), 
						Double.valueOf(arrayLinha[20]), 
						Double.valueOf(arrayLinha[21]),
						Double.valueOf(arrayLinha[22]), 
						Double.valueOf(arrayLinha[23]), 
						Double.valueOf(arrayLinha[24]),
						arrayLinha[25], 
						Double.valueOf(arrayLinha[26]), 
						Double.valueOf(arrayLinha[27]),
						Double.valueOf(arrayLinha[28]), 
						Double.valueOf(arrayLinha[29]), 
						Double.valueOf(arrayLinha[30]),
						Double.valueOf(arrayLinha[31]), 
						Double.valueOf(arrayLinha[32]), 
						arrayLinha[33], 
						arrayLinha[34],
						arrayLinha[35], 
						arrayLinha[36], 
						arrayLinha[37], 
						arrayLinha[38], 
						arrayLinha[39], 
						arrayLinha[40],
						arrayLinha[41], 
						arrayLinha[42], 
						arrayLinha[43], 
						arrayLinha[44], 
						arrayLinha[45], 
						arrayLinha[46],
						arrayLinha[47], 
						arrayLinha[48], 
						arrayLinha[49], 
						arrayLinha[50], 
						arrayLinha[51], 
						arrayLinha[52],
						arrayLinha[53], 
						arrayLinha[54], 
						arrayLinha[55], 
						arrayLinha[56], 
						arrayLinha[57], 
						arrayLinha[58],
						arrayLinha[59], 
						arrayLinha[60], 
						arrayLinha[61], 
						arrayLinha[61], 
						arrayLinha[63]);

			} catch (Exception e) {
				fillErrorLog(linha, e);
			}
			linhaArquivo++;

		}
		closeFileLog();

	}

	private String preparaParaSplit(String linha) {
		linha = linha.replaceAll("\\|", "#");
		while (linha.contains("\\|")) {
			linha = linha.replaceAll("\\|", "#");
		}
		linha = linha.replaceAll("##", "# #");
		while (linha.contains("##")) {
			linha = linha.replaceAll("##", "# #");
		}
		String ultCarac = linha.substring(linha.length() - 1);
		if (ultCarac.equals("#")) {
			linha = linha + " ";
		}
		return linha;
	}

	private void closeFileLog() {
		try {
			bw.write(" -- fim de arquivo -- \n");
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void ativaFileLog(String fileLog) {
		file = new File("c:/TEMP/lagoa/zzz_" + fileLog + "_err.txt");
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write("-- Arquivo " + fileLog + " - inicio \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void fillErrorLog(String linha, Exception e) {
		String linhaAux = linha.replaceAll("#", "|");
		try {
			bw.write("erro --> " + e + " conteudo da linha:" + linhaAux + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
