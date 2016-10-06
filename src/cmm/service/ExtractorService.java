package cmm.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cmm.dao.CompetenciasDao;
import cmm.dao.DadosLivroPrestadorDao;
import cmm.dao.Dao;
import cmm.dao.GuiasDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.PrestadoresOptanteSimplesDao;
import cmm.dao.ServicosDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.entidadesOrigem.Servicos;
import cmm.model.Competencias;
import cmm.model.Guias;
import cmm.model.Prestadores;
import cmm.model.Tomadores;
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
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private PessoaDao pessoaDao = new PessoaDao();
	private GuiasDao guiasDao = new GuiasDao();
	private DadosLivroPrestadorDao dadosLivroPrestadorDao = new DadosLivroPrestadorDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro_atividade");
		ExecutorService executor = Executors.newFixedThreadPool(100);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			CadastroAtividadeThread thread = new CadastroAtividadeThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		Util.pausar(3000);
		log.close();

	}

	public void processaDadosCadastro(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro");
		ExecutorService executor = Executors.newFixedThreadPool(1);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			ContribuinteThread thread = new ContribuinteThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		Util.pausar(3000);
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
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		Util.pausar(3000);
		log.close();
	}

	public void processaDadosLivroPrestador(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador");
		ExecutorService executor = Executors.newFixedThreadPool(250);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			PrestadorThread thread = new PrestadorThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		Util.pausar(3000);
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
		ExecutorService executor = Executors.newFixedThreadPool(250);
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
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream("c:/TEMP/lagoa/tratados/" + arquivoIn + ".txt"), "UTF-8"));

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
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores", "Prestadores",
				"Pessoa");

	}

	public List<String> excluiParaProcessarNivel2() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores");

	}

	public List<String> excluiParaProcessarNivel3() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores",
				"Pagamentos", "Guias", "Competencias");
	}

	public List<String> excluiParaProcessarNivel4() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores",
				"PrestadoresAtividades", "Servicos");
	}

	public List<String> excluiParaProcessarNivel5() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores",
				"Servicos");
	}

	public List<String> excluiParaProcessarNivel6() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Servicos");
	}

	public void processaExclusaoPrestadoresSemNotas() {
		System.out.println("Excluindo Prestadores Atividades");
		new PrestadoresAtividadesDao().excluiPrestadoresSemNotas();
		System.out.println("Excluindo Prestadores optante simples");
		new PrestadoresOptanteSimplesDao().excluiPrestadoresSemNotas();
		System.out.println("Excluindo Prestadores");
		prestadoresDao.excluiPrestadoresSemNotas();
		System.out.println("Excluindo Pessoas");
		pessoaDao.excluiPrestadoresSemNotas();
	}

	public void excluiGuiasSemNotas() {

		GuiasDao guiasDao = new GuiasDao();
		Util.pausar(3000);

		System.out.println("Excluindo guias retidas");
		GuiasNotasFiscaisDao dao = new GuiasNotasFiscaisDao();
		dao.deleteGuiasRetidas();

		Util.pausar(3000);
		System.out.println("Excluindo guias sem notas");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		for (Guias guias : guiasDao.findAll()) {
			ExcluirGuiasThread thread = new ExcluirGuiasThread(guias);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Guias excluidas");
	}

	public Long count(String nomeEntidade) {
		return dao.count(nomeEntidade);
	}

	// tomadores
	public void processaDadosTomadores(List<String> dadosList) {

		FileLog log = new FileLog("dados_livro_prestador_tomadores");

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			String[] arrayLinha = linha.split("@@\\|");

			DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2],
					arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8],
					arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14],
					arrayLinha[15], arrayLinha[16], arrayLinha[17], util.corrigeDouble(arrayLinha[18]),
					util.corrigeDouble(arrayLinha[19]), util.corrigeDouble(arrayLinha[20]),
					util.corrigeDouble(arrayLinha[21]), util.corrigeDouble(arrayLinha[22]),
					util.corrigeDouble(arrayLinha[23]), util.corrigeDouble(arrayLinha[24]), arrayLinha[25],
					util.corrigeDouble(arrayLinha[26]), util.corrigeDouble(arrayLinha[27]),
					util.corrigeDouble(arrayLinha[28]), util.corrigeDouble(arrayLinha[29]),
					util.corrigeDouble(arrayLinha[30]), util.corrigeDouble(arrayLinha[31]),
					util.corrigeDouble(arrayLinha[32]), util.corrigeDouble(arrayLinha[33]), arrayLinha[34],
					arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40],
					arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46],
					arrayLinha[47], arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52],
					arrayLinha[53], arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58],
					arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[62], arrayLinha[63], arrayLinha[64]);

			if (!dadosLivroPrestadorDao.exists(dlp.getIdCodigo())) {
				dlp = dadosLivroPrestadorDao.save(dlp);
			}

			String inscricaoPrestador = util.getCpfCnpj(dlp.getCnpjPrestador());
			String inscricaoTomador = util.getCpfCnpj(dlp.getCnpjTomador());
			if (util.isEmptyOrNull(inscricaoTomador)){
				inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
			}
			if (util.getTipoPessoa(inscricaoTomador).equals("F")){
				if (!Util.validarCpf(inscricaoTomador)){
					inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
				}
			} else if (util.getTipoPessoa(inscricaoTomador).equals("J")){
				if (!Util.validarCnpj(inscricaoTomador)){
					inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
				}
			}
			else{
				inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
			}
			Prestadores pr = prestadoresDao.findByInscricao(inscricaoPrestador);
			Tomadores t = null;

			t = tomadoresDao.findByInscricao(inscricaoTomador, pr.getId());
			if (t == null || t.getId() == null) {
				try {
					t = new Tomadores();
					t.setOptanteSimples(util.getOptantePeloSimplesNacional("N"));
					if (!util.isEmptyOrNull(dlp.getRazaoSocialTomador())){
						t.setNome(dlp.getRazaoSocialTomador());
						t.setNomeFantasia(dlp.getRazaoSocialTomador());
					}
					else{
						t.setNome("Não informado");
						t.setNomeFantasia("Não informado");
					}
					t.setPrestadores(pr);
					t.setTipoPessoa(util.getTipoPessoa(inscricaoTomador));
					t.setInscricaoTomador(inscricaoTomador);
					t.setBairro(util.getNullIfEmpty(dlp.getEnderecoBairroTomador()));
					t.setCep(util.trataCep(dlp.getCepTomador()));
					t.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoTomador()));
					t.setEmail(util.trataEmail(dlp.getEmailTomador()));
					if (!util.isEmptyOrNull(t.getEmail()) && t.getEmail().length() > 80) {
						t.setEmail(t.getEmail().substring(0, 80));
					}
					t.setEndereco(util.getNullIfEmpty(dlp.getEnderecoTomador()));
					t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
					t.setInscricaoMunicipal(dlp.getInscricaoMunicipalTomador());
					t.setMunicipio(dlp.getMunicipioTomador());
					t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
					t.setDataAtualizacao(util.getStringToDate(dlp.getDataCompetencia()));
					try {
						t.setMunicipioIbge(Long.valueOf(
								municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador())));
					} catch (Exception e) {
						// log.fillError(linha,"Nota Fiscal Tomadores ", e);
						// e.printStackTrace();
					}
					util.trataNumerosTelefones(t);
					util.anulaCamposVazios(t);

					// Sï¿½ salvar tomador se a inscriï¿½ï¿½o nï¿½o for =
					// 00000000000;
					if (!"".equals(t.getInscricaoTomador().replace("0", "").trim())) {
						t = tomadoresDao.save(t);
					}
					} catch (Exception e) {
						e.printStackTrace();
						log.fillError(linha, "Tomadores ", e);
						t = null;
					}

				} else {

					if (util.getStringToDate(dlp.getDataCompetencia()).getTime() > t.getDataAtualizacao().getTime()) {
						try {
							t.setBairro(util.getNullIfEmpty(dlp.getEnderecoBairroTomador()));
							t.setCep(util.trataCep(dlp.getCepTomador()));
							t.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoTomador()));
							t.setEmail(util.trataEmail(dlp.getEmailTomador()));
							if (!util.isEmptyOrNull(t.getEmail()) && t.getEmail().length() > 80) {
								t.setEmail(t.getEmail().substring(0, 80));
							}
							t.setEndereco(util.getNullIfEmpty(dlp.getEnderecoTomador()));
							t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
							t.setInscricaoMunicipal(dlp.getInscricaoMunicipalTomador());
							t.setMunicipio(dlp.getMunicipioTomador());
							t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));

							util.trataNumerosTelefones(t);
							util.anulaCamposVazios(t);

							t.setDataAtualizacao(util.getStringToDate(dlp.getDataCompetencia()));

							if (!"".equals(t.getInscricaoTomador().replace("0", "").trim())) {
								t = tomadoresDao.update(t);
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.fillError(linha, "Tomadores - update ", e);
							t = null;
						}
					}
				}
			
		}
	}

	public void processaAjustesEmServicos() {

		try {
			Map<String, Servicos> mapServicos = new HashMap<String, Servicos>();
			ServicosDao dao = new ServicosDao();

			for (Servicos servico : dao.findAll()) {
				try{
					if (!mapServicos.containsKey(servico.getCodigo() + "-" + servico.getCnaes())) {
						mapServicos.put(servico.getCodigo() + "-" + servico.getCnaes(), servico);
					} else {
						Servicos servAux = mapServicos.get(servico.getCodigo() + "-" + servico.getCnaes());
						if (servico.getDataAtualizacao().getTime() > servAux.getDataAtualizacao().getTime()) {
							mapServicos.put(servico.getCodigo() + "-" + servico.getCnaes(), servico);
						}
					}
				}
				catch(Exception e){
					
				}
			}

			Dao daoMae = new Dao();
			daoMae.excluiDados("Servicos");

			for (String key : mapServicos.keySet()) {
				Servicos s = mapServicos.get(key);
				dao.save(s);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}
