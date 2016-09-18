package cmm.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cmm.dao.CompetenciasDao;
import cmm.dao.Dao;
import cmm.model.Competencias;
import cmm.util.FileLog;
import cmm.util.Util;

/**
 * 
 * @author jway
 *
 */
public class ExtractorService {
	private final Util util = new Util();
	private final CompetenciasDao competenciasDao = new CompetenciasDao();	
	private final Dao dao = new Dao();

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		final FileLog log = new FileLog("dados_cadastro_atividade");
		ExecutorService executor = Executors.newFixedThreadPool(350);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			CadastroAtividadeThread thread = new CadastroAtividadeThread(linha, util, log);
			executor.execute(thread);
		}
		Util.pausar(3000);
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();

	}

	public void processaDadosCadastro(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro");
		ExecutorService executor = Executors.newFixedThreadPool(600);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			ContribuinteThread thread = new ContribuinteThread(linha, util, log);
			executor.execute(thread);
		}
		Util.pausar(3000);
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosGuiaCompetencias(List<String> dadosList) {
		FileLog log = new FileLog("dados_guia");
		ExecutorService executor = Executors.newFixedThreadPool(350);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			GuiaThread thread = new GuiaThread(linha, util, log);
			executor.execute(thread);
		}
		Util.pausar(3000);
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosLivroPrestador(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador");
		ExecutorService executor = Executors.newFixedThreadPool(300);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			PrestadorThread thread = new PrestadorThread(linha, util, log);
			executor.execute(thread);
		}
		Util.pausar(3000);
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void incluiCompetencias() {

		for (int ano = 2010; ano < 2017; ano++) {

			for (int mes = 1; mes <= 12; mes++) {
				String descricao = util.getNomeMes(Integer.toString(mes)) + "/" + ano;
				Competencias cp = competenciasDao.findByDescricao(descricao);

				try {
					if (cp == null || cp.getId() == 0) { // acertar datas
						cp = new Competencias();
						cp.setDescricao(descricao.trim());
						cp.setDataInicio(util.getFirstDayOfMonth(Integer.toString(ano), Integer.toString(mes)));
						cp.setDataFim(util.getLastDayOfMonth(Integer.toString(ano), Integer.toString(mes)));
						cp.setDataVencimento(util.getVencimentoCompetencia(cp.getDataFim()));

						competenciasDao.save(cp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String preparaParaSplit(String linha) {
		linha = linha.replaceAll("\\|", "#");
		while (linha.contains("\\|")) {
			linha = linha.replaceAll("\\|", "#");
		}
		linha = linha.replaceAll("##", "# #");
		while (linha.contains("##")) {
			linha = linha.replaceAll("##", "# #");
		}
		try {
			String ultCarac = linha.substring(linha.length() - 1);
			if ("#".equals(ultCarac)) {
				linha = linha + " ";
			}
		} catch (Exception e) {

		}
		return linha;
	}

	public void excluiDados(String nomeEntidade) {

		dao.excluiDados(nomeEntidade);
	}

	public void processaDadosNotasFiscais(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador_notas_fiscais");
		Util.pausar(5000);
		ExecutorService executor = Executors.newFixedThreadPool(300);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			NotasMaeThread thread = new NotasMaeThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		Util.pausar(3000);
		while (!executor.isTerminated()) {
		}
		Util.pausar(20000);
		log.close();

	}

	public List<String> lerArquivo(String arquivoIn) {
		BufferedReader br;
		List<String> dadosList = new ArrayList<String>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("c:/TEMP/lagoa/tratados/" + arquivoIn + ".txt"), "UTF-8"));

			while (br.ready()) {
				String linha = br.readLine();
				dadosList.add(linha);
			}
			br.close();
			return dadosList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<String> excluiParaProcessarNivel1() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos",
				"PrestadoresAtividades", "" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores", "Prestadores", "Pessoa");

	}

	public List<String> excluiParaProcessarNivel2() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos",
				"PrestadoresAtividades", "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores");

	}

	public List<String> excluiParaProcessarNivel3() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores",
				"Pagamentos", "Guias", "Competencias");
	}

	public List<String> excluiParaProcessarNivel4() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores",
				"PrestadoresAtividades");
	}

	public List<String> excluiParaProcessarNivel5() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos", "NotasFiscaisEmails", "NotasFiscaisObras",
				"NotasFiscaisPrestadores", "NotasFiscaisServicos", "NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores");
	}

}
