package cmm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmm.dao.CompetenciasDao;
import cmm.dao.Dao;
import cmm.dao.GuiasDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.PagamentosDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.PrestadoresOptanteSimplesDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosCadastro;
import cmm.entidadesOrigem.DadosCadastroAtividade;
import cmm.entidadesOrigem.DadosGuia;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Competencias;
import cmm.model.Guias;
import cmm.model.NotasFiscais;
import cmm.model.Pagamentos;
import cmm.model.Pessoa;
import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
import cmm.model.PrestadoresOptanteSimples;
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
	private final PrestadoresDao prestadoresDao = new PrestadoresDao();
	private final Dao dao = new Dao();
	private final GuiasDao guiasDao = new GuiasDao();
	private final NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private final PagamentosDao pagamentosDao = new PagamentosDao();
	private final PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private final PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();
	private final PessoaDao pessoaDao = new PessoaDao();
	private int linhasMil = 0;
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private NotasFiscais nf;
	private NotaMaeThreadService notaMaeThread;

	public void processaPlanoConta(List<String> dadosList) {/*
		FileLog log = new FileLog("plano_conta");

		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				PlanoConta pc = new PlanoConta(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14]);
			} catch (Exception e) {
				fillErrorLog(linha, e, log.getBw());
				e.printStackTrace();
			}

		}
		log.close();*/

	}

	public void processaDadosContador(List<String> dadosList) {/*
		FileLog log = new FileLog("dados_contador");
		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosContador dc = new DadosContador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8],
						arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14],
						arrayLinha[15], arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19]);
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha, e);
			}

		}
		log.close();
	*/
	}

	public void processaDadosCadastroAcesso(List<String> dadosList) {/*
		FileLog log = new FileLog("dados_cadastro_acesso");
		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosCadastroAcesso dca = new DadosCadastroAcesso(arrayLinha[0], arrayLinha[1]);
			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}

		}
		log.close();/*/

	}

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		final FileLog log = new FileLog("dados_cadastro_atividade");
		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosCadastroAtividade dca = new DadosCadastroAtividade(arrayLinha[0], arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], util.corrigeDouble(arrayLinha[5]), arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12]);

				// atividade prestador{
				String inscricaoPrestador = dca.getCnpj().trim();
				Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
				if (p != null && p.getId() != 0) {
					try {
						PrestadoresAtividades pa = new PrestadoresAtividades();
						pa.setAliquota(BigDecimal.valueOf(dca.getAliquota()));
						// pa.setIcnaes(null);
						pa.setIlistaservicos(util.completarZerosEsquerda(dca.getGrupoAtividade(), 4));
						pa.setInscricaoPrestador(dca.getCnpj());
						pa.setPrestadores(p);
						dca.setAtividadeFederal(dca.getAtividadeFederal().replace(".", ""));
						pa.setCodigoAtividade(dca.getAtividadeFederal());
						prestadoresAtividadesDao.save(pa);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha, e);
			}

		}
		log.close();

	}

	public void processaDadosCadastro(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro");
		int linhas = 0;
		linhasMil = 0;
		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			if (linhasMil >= 1000) {
				mostraProgresso(linhas, dadosList.size());
			}
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosCadastro dc = new DadosCadastro(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15],
						arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21],
						arrayLinha[22]);
				// incluindo pessoa
				String cnpjCpf = dc.getCnpj().trim();
				Pessoa pessoa = pessoaDao.findByCnpjCpf(cnpjCpf);
				try {
					if (pessoa == null || pessoa.getId() == null) {
						pessoa = new Pessoa();
						pessoa.setPessoaId(Long.valueOf(dc.getIdCodigo()));
						pessoa.setEmail(dc.getEmail() != null && dc.getEmail().length() >= 80
								? dc.getEmail().substring(0, 80) : dc.getEmail());
						pessoa.setCnpjCpf(cnpjCpf);
						pessoa.setBairro(dc.getEnderecoBairro());
						pessoa.setEndereco(dc.getEndereco());
						pessoa.setCep(dc.getEnderecoCep());
						pessoa.setComplemento(dc.getEnderecoComplemento());
						pessoa.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						pessoa.setMunicipio(dc.getMunicipio());
						pessoa.setNome(dc.getRazaoSocial());
						pessoa.setNomeFantasia(dc.getNomeFantasia());
						if (dc.getNomeFantasia() == null || dc.getNomeFantasia().isEmpty()) {
							pessoa.setNomeFantasia(pessoa.getNome());
						}
						pessoa.setNumero(dc.getEnderecoNumero());
						pessoa.setOptanteSimples(util.getOptantePeloSimplesNacional(dc.getOptanteSimplesNacional()));

						dc.setTelefone(util.getLimpaTelefone(dc.getTelefone()));
						if (dc.getTelefone() != null && dc.getTelefone().trim().length() > 11) {
							pessoa.setTelefone(dc.getTelefone().trim().substring(0, 11));
							pessoa.setCelular(dc.getTelefone().trim().substring(0, 11));
						} else {
							if (dc.getTelefone() != null) {
								pessoa.setTelefone(dc.getTelefone().trim());
								pessoa.setCelular(dc.getTelefone().trim());
							}
						}
						pessoa.setTipoPessoa(util.getTipoPessoa(dc.getCnpj().trim()));
						if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
						} else {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual());
						}
						pessoa = util.trataNumerosTelefones(pessoa);
						pessoa.setUf(dc.getEnderecoUf());
						pessoa.setMunicipioIbge(
								Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));

						if ("F".equals(pessoa.getTipoPessoa())) {
							pessoa.setSexo("M");
						}
						pessoa = util.anulaCamposVazios(pessoa);
						pessoa = pessoaDao.save(pessoa);
					} else {

						if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
						} else {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual());
						}
						pessoa.setUf(dc.getEnderecoUf());
						if (pessoa.getMunicipio() == null) {
							pessoa.setMunicipio(dc.getMunicipio());
						}
						pessoa.setMunicipioIbge(
								Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
						pessoa.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						if ((pessoa.getTelefone() == null || pessoa.getTelefone().isEmpty())) {
							pessoa.setTelefone(util.getLimpaTelefone(dc.getTelefone()));
							if (pessoa.getCelular() == null) {
								pessoa.setCelular(util.getLimpaTelefone(dc.getTelefone()));
							}
						}

						pessoa = util.trataNumerosTelefones(pessoa);
						pessoa = util.anulaCamposVazios(pessoa);
						pessoa = pessoaDao.update(pessoa);

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.fillError(linha, e);
				}

				// ajustando prestadores
				Prestadores p = prestadoresDao.findByInscricao(dc.getCnpj().trim());
				try {
					if (p == null || p.getId() == 0 || p.getId() == null) {
						try {
							p = new Prestadores();
							p.setAutorizado("N");
							dc.setTelefone(util.getLimpaTelefone(dc.getTelefone()));
							if (dc.getTelefone() != null && dc.getTelefone().trim().length() >= 11) {
								p.setCelular(dc.getTelefone().trim().substring(0, 11));
								p.setTelefone(dc.getTelefone().trim().substring(0, 11));
							} else {
								p.setCelular(dc.getTelefone().trim());
								p.setTelefone(dc.getTelefone().trim());
							}
							p.setEmail(dc.getEmail());
							p.setEnquadramento("N");
							p.setInscricaoPrestador(dc.getCnpj());
							p = util.trataNumerosTelefones(p);
							p = util.anulaCamposVazios(p);
							p = prestadoresDao.save(p);
						} catch (Exception e) {
							log.fillError(linha, e);
							e.printStackTrace();
						}
					}

					// prestadores optantes simples
					if ("S".equalsIgnoreCase(dc.getOptanteSimplesNacional().substring(0, 1))) {
						try {
							PrestadoresOptanteSimples pos = new PrestadoresOptanteSimples();
							String inicioAtividade = dc.getDtInicioAtividade();
							if (inicioAtividade == null || inicioAtividade.trim().isEmpty()) {
								inicioAtividade = dc.getDataInclusaoRegistro();

							}
							pos.setDataEfeito(util.getStringToDate(inicioAtividade));
							pos.setDataInicio(pos.getDataEfeito());
							pos.setInscricaoPrestador(dc.getCnpj());
							pos.setDescricao(dc.getRegimeTributacao());
							pos.setMei("N"); // ver com cmm
							pos.setMotivo("Opção do Contribuinte");
							pos.setOptante("S");
							pos.setOrgao("M");
							pos.setPrestadores(p);
							prestadoresOptanteSimplesDao.save(pos);
						} catch (Exception e) {
							log.fillError(linha, e);
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					log.fillError(linha, e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}

		}
		log.close();

	}

	public void processaDadosGuiaCompetencias(List<String> dadosList) {
		FileLog log = new FileLog("dados_guia");

		int linhas = 0;
		linhasMil = 0;

		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			mostraProgresso(linhas, dadosList.size());

			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosGuia dg = new DadosGuia(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
						arrayLinha[5], arrayLinha[6], util.corrigeDouble(arrayLinha[7]),
						util.corrigeDouble(arrayLinha[8]), util.corrigeDouble(arrayLinha[9]),
						util.corrigeDouble(arrayLinha[10]), util.corrigeDouble(arrayLinha[11]), arrayLinha[12],
						arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], arrayLinha[18],
						arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22], arrayLinha[23], arrayLinha[24],
						arrayLinha[25], arrayLinha[26], arrayLinha[27], arrayLinha[28],
						util.corrigeDouble(arrayLinha[29]), util.corrigeDouble(arrayLinha[30]), arrayLinha[31],
						arrayLinha[32], arrayLinha[33], arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37],
						arrayLinha[38], arrayLinha[39], arrayLinha[40], arrayLinha[41]);
				String descricao = util.getNomeMes(dg.getMes()) + "/" + dg.getAno();
				Competencias cp = competenciasDao.findByDescricao(descricao);
				try {
					if (cp == null || cp.getId() == 0) { // acertar datas
						cp = new Competencias();
						cp.setDescricao(descricao.trim());
						cp.setDataInicio(util.getFirstDayOfMonth(dg.getAno(), dg.getMes()));
						cp.setDataFim(util.getLastDayOfMonth(dg.getAno(), dg.getMes()));
						cp.setDataVencimento(util.getDecimoDiaMesPosterior(cp.getDataFim()));

						competenciasDao.save(cp);
					}
				} catch (Exception e) {
					log.fillError(linha, e);
					e.printStackTrace();
				}

				try {
					if ("GUIA_PRESTADOR".equals(dg.getTipoGuia().trim())) {
						Guias guias = new Guias();
						guias.setCompetencias(cp);
						guias.setDataVencimento(util.getStringToDateHoursMinutes(dg.getDataVencimento()));
						guias.setInscricaoPrestador(dg.getCnpj());
						guias.setIntegrarGuia("N"); // TODO sanar d�vida

						String numeroGuia = dg.getNossoNumero().substring(3);
						int proximoNumeroGuia = 60000000 + Integer.parseInt(numeroGuia);
						guias.setNumeroGuia(Long.valueOf(proximoNumeroGuia));
						guias.setNumeroGuiaOrigem(dg.getNossoNumero());

						Prestadores prestadores = prestadoresDao.findByInscricao(dg.getCnpj().trim());
						if (prestadores == null) {
							log.fillError(linha, "Prestador n�o encontrado:" + dg.getInscMunicipal());
						} else {
							guias.setPrestadores(prestadores);
						}
						String situacao = "A";
						if (dg.getValorPago() != null && dg.getValorPago() != util.corrigeDouble(0)) {
							situacao = "P";
						}
						if (dg.getDataCancelamento() != null && !dg.getDataCancelamento().trim().isEmpty()) {
							situacao = "C";
						}
						guias.setSituacao(situacao);

						guias.setTipo(dg.getTipoGuia().substring(5, 6));

						guias.setValorDesconto(BigDecimal.valueOf(0.00));
						guias.setValorGuia(BigDecimal.valueOf(dg.getValorTotal()));
						guias.setValorImposto(BigDecimal.valueOf(dg.getImposto()));
						guiasDao.save(guias);

						// pagamentos
						if ("P".equals(guias.getSituacao()) && dg.getValorPago() > 0) {
							try {
								Pagamentos p = new Pagamentos();
								p.setDataPagamento(util.getStringToDateHoursMinutes(dg.getDataPagamento()));
								p.setGuias(guias);
								p.setNumeroGuia(guias.getNumeroGuia());
								p.setNumeroPagamento(guias.getNumeroGuia());
								p.setTipoPagamento("N");
								p.setValorCorrecao(BigDecimal.valueOf(dg.getCorrecaoMonetaria()));
								p.setValorJuro(BigDecimal.valueOf(dg.getJuros()));
								p.setValorMulta(BigDecimal.valueOf(dg.getMulta()));
								p.setValorPago(BigDecimal.valueOf(dg.getValorPago()));
								pagamentosDao.save(p);
							} catch (Exception e) {
								System.out.println(dg.getNossoNumero().trim());
								e.printStackTrace();
								log.fillError(linha, e);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.fillError(linha, e);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha, e);
			}

		}

		log.close();

	}

	public void processaDadosLivroPrestador(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador");

		int linhas = 0;
		linhasMil = 0;

		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			if (linhasMil == 1000) {
				mostraProgresso(linhas, dadosList.size());
			}

			try {

				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("@@#");
				DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1],
						arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13],
						arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17],
						util.corrigeDouble(arrayLinha[18]), util.corrigeDouble(arrayLinha[19]),
						util.corrigeDouble(arrayLinha[20]), util.corrigeDouble(arrayLinha[21]),
						util.corrigeDouble(arrayLinha[22]), util.corrigeDouble(arrayLinha[23]),
						util.corrigeDouble(arrayLinha[24]), arrayLinha[25], util.corrigeDouble(arrayLinha[26]),
						util.corrigeDouble(arrayLinha[27]), util.corrigeDouble(arrayLinha[28]),
						util.corrigeDouble(arrayLinha[29]), util.corrigeDouble(arrayLinha[30]),
						util.corrigeDouble(arrayLinha[31]), util.corrigeDouble(arrayLinha[32]),
						util.corrigeDouble(arrayLinha[33]), arrayLinha[34], arrayLinha[35], arrayLinha[36],
						arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40], arrayLinha[41], arrayLinha[42],
						arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46], arrayLinha[47], arrayLinha[48],
						arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52], arrayLinha[53], arrayLinha[54],
						arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58], arrayLinha[59], arrayLinha[60],
						arrayLinha[61], arrayLinha[62], arrayLinha[63], arrayLinha[64]);

				String inscricaoPrestador = dlp.getCnpjPrestador().trim();
				Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
				try {
					if (p == null || p.getId() == 0 || p.getId() == null) {
						// na hora de processar dados_cadastro estas
						// informa��es
						// tem que ser verificadas
						p = new Prestadores();
						p.setAutorizado("N");
						dlp.setTelefonePrestador(util.getLimpaTelefone(dlp.getTelefonePrestador()));
						p.setCelular(dlp.getTelefonePrestador());
						p.setEmail(dlp.getEmailPrestador());
						p.setEnquadramento("N");
						p.setInscricaoPrestador(inscricaoPrestador.trim());
						p.setTelefone(dlp.getTelefonePrestador());
						p = util.trataNumerosTelefones(p);
						p = util.anulaCamposVazios(p);
						p = prestadoresDao.save(p);
					} else { // preencher campos vazios

						if (p.getCelular() == null || p.getCelular().isEmpty()) {
							p.setCelular(dlp.getTelefonePrestador());
						}

						if (p.getTelefone() == null || p.getTelefone().isEmpty()) {
							p.setTelefone(dlp.getTelefonePrestador());
						}

						if (p.getEmail() == null || p.getEmail().isEmpty()) {
							p.setEmail(dlp.getEmailPrestador());
						} else {
							if (!p.getEmail().contains("@")) {
								p.setEmail(dlp.getEmailPrestador());
							}
						}
						p = util.trataNumerosTelefones(p);
						p = util.anulaCamposVazios(p);
						p = prestadoresDao.update(p);

					}

				} catch (Exception e) {
					log.fillError(linha, e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.fillError(linha, e);

				e.printStackTrace();
			}

		}

		log.close();

	}

	public void mostraProgresso(int linhas, int dadosSize) {
		if (linhasMil == 1000) {
			double linhasDouble = linhas;
			double perc = (linhasDouble / dadosSize);
			perc = perc * 100;
			perc = Math.round(perc);
			System.out.println("Percentual registros processados: " + perc + "% - total de linhas:" + dadosSize
					+ " total lido:" + linhas);
			linhasMil = 0;
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

	private void fillErrorLog(String linha, Exception e, BufferedWriter bw) {
		String linhaAux = linha.replaceAll("#", "|");
		try {
			bw.write("erro --> " + " conteudo da linha:" + linhaAux + "\n" + e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public void excluiDados(String nomeEntidade) {

		dao.excluiDados(nomeEntidade);
	}

	public void processaDadosNotasFiscais(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador_notas_fiscais");

		linhasMil = 0;
		
		long contaArrays = 0;
		int contaLinhas = 0;
		Map<Long, List<String>> mapListas = new HashMap<Long, List<String>>();
	    List<String> listaAux = new ArrayList<String>();
	    
		for (String linha : dadosList) {
			contaLinhas++;
			listaAux.add(linha);
			if (contaLinhas > 1000) {
				mapListas.put(contaArrays, listaAux);
				listaAux = new ArrayList<String>();
				contaArrays++;
				contaLinhas = 0;
			}
		}
		
		if (listaAux.size() > 0) {
			mapListas.put(contaArrays, listaAux);
		}
		
		Set<Long> chave = new HashSet<Long>(mapListas.keySet());
		int threadsCount = 0;
		for(Long indice : chave) {
			NotaMaeThreadService notaMaeThread = new NotaMaeThreadService(mapListas.get(indice), log);
			Thread thread = new Thread(notaMaeThread);
			thread.start();
			threadsCount++;
		}
		
		System.out.println("Total de threads criadas:" + threadsCount);
			
		

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
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Pagamentos", "Guias",
				"Competencias");
	}

	public List<String> excluiParaProcessarNivel4() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais",
				"PrestadoresAtividades");
	}

	public List<String> excluiParaProcessarNivel5() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais");
	}
	
	public Long getNotasFiscaisGravadas() {
		return notasFiscaisDao.count();
		
	}

	

}
