package cmm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import cmm.entidadesOrigem.DadosCadastroAcesso;
import cmm.entidadesOrigem.DadosCadastroAtividade;
import cmm.entidadesOrigem.DadosContador;
import cmm.entidadesOrigem.DadosGuia;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.entidadesOrigem.DadosLivroTomador;
import cmm.entidadesOrigem.PlanoConta;
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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 
 * @author jway
 *
 */
public class ExtractorService {
	private Util util = new Util();
	private CompetenciasDao competenciasDao = new CompetenciasDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private Dao dao = new Dao();
	private GuiasDao guiasDao = new GuiasDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();
	private Map<String, Tomadores> mapTomadores = new Hashtable<String, Tomadores>();
	private Map<String, Prestadores> mapPrestadores = new Hashtable<String, Prestadores>();
	private PessoaDao pessoaDao = new PessoaDao();
	private int linhasMil = 0;
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public void processaPlanoConta(List<String> dadosList) {
		FileLog log = new FileLog("plano_conta");

		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				PlanoConta pc = new PlanoConta(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14]);
			} catch (Exception e) {
				fillErrorLog(linha, e, log.getBw());
				e.printStackTrace();
			}

		}
		log.close();

	}

	public void processaDadosContador(List<String> dadosList) {
		FileLog log = new FileLog("dados_contador");
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
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

	}

	public void processaDadosCadastroAcesso(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro_acesso");
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastroAcesso dca = new DadosCadastroAcesso(arrayLinha[0], arrayLinha[1]);
			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}

		}
		log.close();

	}

	public void processaDadosCadastroAtividade(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro_atividade");
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastroAtividade dca = new DadosCadastroAtividade(arrayLinha[0], arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], Double.valueOf(arrayLinha[5]), arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12]);

				// atividade prestador{
				String inscricaoPrestador = dca.getCnpj().trim();
				Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
				if (p != null && p.getId() != 0) {
					try {
						PrestadoresAtividades pa = new PrestadoresAtividades();
						pa.setAliquota(BigDecimal.valueOf(dca.getAliquota()));
						// pa.setIcnaes(null);
						pa.setIlistaservicos(dca.getGrupoAtividade());
						pa.setInscricaoPrestador(dca.getCnpj());
						pa.setPrestadores(p);
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
				String[] arrayLinha = linha.split("\\|");
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
						pessoa.setCelular(dc.getTelefone());
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
						pessoa = trataNumerosTelefones(pessoa);
						pessoa.setUf(dc.getEnderecoUf());
						pessoa.setMunicipioIbge(
								Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
						
						if (pessoa.getTipoPessoa().equals("F")) {
							pessoa.setSexo("M");
						}
						pessoa = anulaCamposVazios(pessoa);
						pessoaDao.save(pessoa);
					} else {
						if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
						} else {
							pessoa.setInscricaoEstadual(dc.getInscricaoEstadual());
						}
						pessoa.setUf(dc.getEnderecoUf());
						pessoa.setMunicipioIbge(
								Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
						pessoa.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						pessoa = trataNumerosTelefones(pessoa);
						pessoa = anulaCamposVazios(pessoa);
						pessoaDao.update(pessoa);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				// ajustando prestadores
				Prestadores p = prestadoresDao.findByInscricao(dc.getCnpj().trim());
				try {
					if (p == null || p.getId() == 0 || p.getId() == null) {
						try {
							p = new Prestadores();
							p.setAutorizado("S");
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
							p = trataNumerosTelefones(p);
							p = anulaCamposVazios(p);
							prestadoresDao.save(p);
						} catch (Exception e) {
							log.fillError(linha, e);
							e.printStackTrace();
						}
					}

					// prestadores optantes simples
					if (dc.getOptanteSimplesNacional().substring(0, 1).equalsIgnoreCase("S")) {
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
					e.printStackTrace();
				}

				// ajustando tomadores
				String inscricaoTomador = dc.getCnpj().trim();
				Tomadores t = tomadoresDao.findByInscricao(inscricaoTomador);
				try {
					if (t == null || t.getId() == null || t.getId() == 0) {
						t = new Tomadores();
						t.setCelular(dc.getTelefone());
						t.setEmail(dc.getEmail() != null && dc.getEmail().length() >= 80
								? dc.getEmail().substring(0, 80) : dc.getEmail());
						t.setInscricaoTomador(inscricaoTomador.trim());
						t.setBairro(dc.getEnderecoBairro());
						t.setEndereco(dc.getEndereco());
						t.setCep(dc.getEnderecoCep());
						t.setComplemento(dc.getEnderecoComplemento());
						t.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						t.setMunicipio(dc.getMunicipio());
						t.setNome(dc.getRazaoSocial());
						t.setNomeFantasia(dc.getNomeFantasia());
						if (dc.getNomeFantasia() == null || dc.getNomeFantasia().isEmpty()) {
							t.setNomeFantasia(t.getNome());
						}
						t.setNumero(dc.getEnderecoNumero());
						t.setOptanteSimples(util.getOptantePeloSimplesNacional(dc.getOptanteSimplesNacional()));
						if (dc.getTelefone() != null && dc.getTelefone().trim().length() > 11) {
							t.setTelefone(dc.getTelefone().trim().substring(0, 11));
							t.setCelular(dc.getTelefone().trim().substring(0, 11));
						} else {
							t.setTelefone(dc.getTelefone().trim());
							t.setCelular(dc.getTelefone().trim());
						}
						p = prestadoresDao.findByInscricao(dc.getCnpj().trim());
						t.setPrestadores(p);
						t.setTipoPessoa(util.getTipoPessoa(dc.getCnpj().trim()));
						if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
							t.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
						} else {
							t.setInscricaoEstadual(dc.getInscricaoEstadual());
						}
						t = trataNumerosTelefones(t);
						t = anulaCamposVazios(t);
						try {
							t.setMunicipioIbge(Long
									.valueOf(municipiosIbgeDao.getCodigoIbge(t.getMunicipio(), dc.getEnderecoUf())));
						} catch (Exception e) {
							t.setMunicipioIbge(null);
						}

						tomadoresDao.save(t);
					} else {
						if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
							t.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
						} else {
							t.setInscricaoEstadual(dc.getInscricaoEstadual());
						}

						t.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						p = prestadoresDao.findByInscricao(dc.getCnpj().trim());
						t.setPrestadores(p);
						t = trataNumerosTelefones(t);
						t = anulaCamposVazios(t);
						try {
							t.setMunicipioIbge(Long
									.valueOf(municipiosIbgeDao.getCodigoIbge(t.getMunicipio(), dc.getEnderecoUf())));
						} catch (Exception e) {
							t.setMunicipioIbge(null);
						}

						tomadoresDao.update(t);
					}

				} catch (Exception e) {
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
					Guias guias = new Guias();
					guias.setCompetencias(cp);
					guias.setDataVencimento(util.getStringToDateHoursMinutes(dg.getDataVencimento()));
					guias.setInscricaoPrestador(dg.getCnpj());
					guias.setIntegrarGuia("S"); // TODO sanar d�vida
					guias.setNumeroGuia(Long.valueOf(dg.getNossoNumero()));
					Prestadores prestadores = prestadoresDao.findByInscricao(dg.getCnpj().trim());
					if (prestadores == null) {
						log.fillError(linha, "Prestador n�o encontrado:" + dg.getInscMunicipal());
					} else {
						guias.setPrestadores(prestadores);
					}
					String situacao = "A";
					if (dg.getValorPago() != null && dg.getValorPago() != Double.valueOf(0)) {
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
					if (guias.getSituacao().equals("P")) {
						try {
							Pagamentos p = new Pagamentos();
							p.setDataPagamento(util.getStringToDateHoursMinutes(dg.getDataPagamento()));
							p.setGuias(guias);
							p.setNumeroGuia(guias.getId());
							p.setNumeroPagamento(guias.getId());
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

				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha, e);
			}

		}

		log.close();

	}

	public void processaDadosLivroTomador(List<String> dadosList) {

		buscaPrestadores();
		buscaTomadores();

		Long proximoPessoaIdGravado = null;
		Pessoa ultimaPessoa = null;
		try {
			ultimaPessoa = pessoaDao.ultimoPessoaIdGravado();
			proximoPessoaIdGravado = ultimaPessoa.getPessoaId();
			proximoPessoaIdGravado++;
		} catch (Exception e) {
			try {
				throw new Exception("Erro fatal: ultimo pessoa-id gravado não recuperado!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		FileLog log = new FileLog("dados_livro_tomador");
		if (mapPrestadores == null || mapTomadores == null || mapPrestadores.isEmpty() || mapTomadores.isEmpty()) {
			try {
				throw new Exception(
						"Erro fatal: Dados de prestadores ou tomadores não foram devidamente alocados na memória!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int linhas = 0;
		linhasMil = 0;

		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			mostraProgresso(linhas, dadosList.size());
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroTomador dlt = new DadosLivroTomador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8],
						arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14],
						arrayLinha[15], arrayLinha[16], arrayLinha[17],
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[18])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[19])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[20])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[21])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[22])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[23])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[24])), arrayLinha[25],
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[26])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[27])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[28])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[29])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[30])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[31])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[32])),
						Double.valueOf(util.trataSeTiverVazio(arrayLinha[33])), arrayLinha[34], arrayLinha[35],
						arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40], arrayLinha[41],
						arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46], arrayLinha[47],
						arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52], arrayLinha[53],
						arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58], arrayLinha[59],
						arrayLinha[60], arrayLinha[61], arrayLinha[62], arrayLinha[63], arrayLinha[64], arrayLinha[65]);
				String inscricaoTomador = dlt.getCnpjTomador().trim();
				Tomadores t = null;
				try {
					t = mapTomadores.get(inscricaoTomador.trim());
				} catch (Exception e) {
					e.printStackTrace();

				}
				try {
					if (t == null || t.getId() == null || t.getId() == 0) {
						// na hora de processar dados_cadastro estas informa��es
						// tem que ser verificadas
						t = new Tomadores();
						t.setCelular(dlt.getTelefoneTomador());
						t.setEmail(dlt.getEmailTomador() != null && dlt.getEmailTomador().length() >= 80
								? dlt.getEmailTomador().substring(0, 80) : dlt.getEmailTomador());
						t.setTelefone(dlt.getTelefoneTomador());
						t.setInscricaoTomador(inscricaoTomador.trim());
						t.setBairro(dlt.getEnderecoBairroTomador());
						t.setCelular(dlt.getTelefoneTomador());
						t.setCep(dlt.getCepTomador());
						t.setComplemento(dlt.getEnderecoComplementoTomador());
						t.setInscricaoEstadual(dlt.getInscricaoEstadualTomador());
						t.setInscricaoMunicipal(dlt.getInscricaoMunicipalTomador());
						t.setMunicipio(dlt.getCidadeTomador());
						t.setNome(dlt.getRazaoSocialTomador());
						t.setNomeFantasia(dlt.getNomeFantasiaTomador());

						t.setNumero(dlt.getEnderecoNumeroTomador());
						t.setOptanteSimples(util.getOptantePeloSimplesNacional(dlt.getOptantePeloSimplesNacional()));
						t.setTelefone(dlt.getTelefoneTomador());

						Prestadores p = null;
						try {
							p = prestadoresDao.findByInscricao(dlt.getCnpjPrestador().trim());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (p == null || p.getId() == null || p.getId() == 0) {
							throw new Exception("Prestador não encontrado:" + dlt.getCnpjPrestador().trim());

						}

						t.setPrestadores(p);

						t.setTipoPessoa(util.getTipoPessoa(dlt.getCnpjTomador().trim()));
						if (t.getNomeFantasia() == null || t.getNomeFantasia().isEmpty()) {
							t.setNomeFantasia(t.getNome());
						}
						if (t.getInscricaoEstadual() != null && t.getInscricaoEstadual().isEmpty()) {
							t.setInscricaoEstadual(null);
						}
						t = trataNumerosTelefones(t);
						t = anulaCamposVazios(t);
						try {
							t.setMunicipioIbge(Long
									.valueOf(municipiosIbgeDao.getCodigoIbge(t.getMunicipio(), dlt.getUfTomador())));
						} catch (Exception e) {
							t.setMunicipioIbge(null);
						}

						tomadoresDao.save(t);
					} else { // registro j� existe, atualizar informa��es n�o
								// preenchidas
						if (t.getInscricaoEstadual() == null || t.getInscricaoEstadual().trim().isEmpty()) {
							try {
								t.setInscricaoEstadual(dlt.getInscricaoEstadualTomador().trim());
							} catch (Exception e) {

							}
						}
						if (t.getNomeFantasia() == null || t.getNomeFantasia().trim().isEmpty()) {
							t.setNomeFantasia(dlt.getNomeFantasiaTomador());
						}

						if (t.getComplemento() == null || t.getComplemento().trim().isEmpty()) {
							t.setComplemento(dlt.getEnderecoComplementoTomador());
						}
						t = trataNumerosTelefones(t);
						t = anulaCamposVazios(t);
						try {
							t.setMunicipioIbge(Long
									.valueOf(municipiosIbgeDao.getCodigoIbge(t.getMunicipio(), dlt.getUfTomador())));
						} catch (Exception e) {
							t.setMunicipioIbge(null);
						}
						tomadoresDao.update(t);

					}
					mapTomadores.put(t.getInscricaoTomador().trim(), t);

					// incluindo pessoa
					String cnpjCpf = dlt.getCnpjTomador().trim();
					Pessoa pessoa = pessoaDao.findByCnpjCpf(cnpjCpf);
					try {
						if (pessoa == null || pessoa.getId() == null) {
							proximoPessoaIdGravado++;
							pessoa = new Pessoa();
							pessoa.setPessoaId(proximoPessoaIdGravado);
							pessoa.setCelular(t.getTelefone());
							pessoa.setEmail(t.getEmail() != null && t.getEmail().length() >= 80
									? t.getEmail().substring(0, 80) : t.getEmail());
							pessoa.setCnpjCpf(cnpjCpf);
							pessoa.setBairro(t.getBairro());
							pessoa.setEndereco(t.getEndereco());
							pessoa.setCep(t.getCep());
							pessoa.setComplemento(t.getComplemento());
							pessoa.setInscricaoMunicipal(t.getInscricaoMunicipal());
							pessoa.setMunicipio(t.getMunicipio());
							pessoa.setNome(t.getNome());
							pessoa.setNomeFantasia(t.getNomeFantasia());
							if (t.getNomeFantasia() == null || t.getNomeFantasia().isEmpty()) {
								pessoa.setNomeFantasia(pessoa.getNome());
							}
							pessoa.setNumero(t.getNumero());
							pessoa.setOptanteSimples(util.getOptantePeloSimplesNacional(t.getOptanteSimples()));
							if (t.getTelefone() != null && t.getTelefone().trim().length() > 11) {
								pessoa.setTelefone(t.getTelefone().trim().substring(0, 11));
								pessoa.setCelular(t.getTelefone().trim().substring(0, 11));
							} else {
								if (t.getTelefone() != null) {
									pessoa.setTelefone(t.getTelefone().trim());
									pessoa.setCelular(t.getTelefone().trim());
								}
							}
							pessoa.setTipoPessoa(util.getTipoPessoa(pessoa.getCnpjCpf().trim()));
							if (t.getInscricaoEstadual() != null && t.getInscricaoEstadual().length() >= 15) {
								pessoa.setInscricaoEstadual(t.getInscricaoEstadual().trim().substring(0, 14));
							} else {
								pessoa.setInscricaoEstadual(t.getInscricaoEstadual());
							}
							pessoa = trataNumerosTelefones(pessoa);
							pessoa.setUf(dlt.getUfTomador());
							try {
								pessoa.setMunicipioIbge(Long.valueOf(
										municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (pessoa.getTipoPessoa().equals("F")) {
								pessoa.setSexo("M");
							}
							
							pessoa = anulaCamposVazios(pessoa);
							pessoaDao.save(pessoa);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (org.hibernate.exception.ConstraintViolationException e) {
					log.fillError(linha, e);
					e.printStackTrace();
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

	public void processaDadosLivroPrestador(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador");

		int linhas = 0;
		linhasMil = 0;

		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			mostraProgresso(linhas, dadosList.size());

			try {

				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1],
						arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13],
						arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], Double.valueOf(arrayLinha[18]),
						Double.valueOf(arrayLinha[19]), Double.valueOf(arrayLinha[20]), Double.valueOf(arrayLinha[21]),
						Double.valueOf(arrayLinha[22]), Double.valueOf(arrayLinha[23]), Double.valueOf(arrayLinha[24]),
						arrayLinha[25], Double.valueOf(arrayLinha[26]), Double.valueOf(arrayLinha[27]),
						Double.valueOf(arrayLinha[28]), Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]),
						Double.valueOf(arrayLinha[31]), Double.valueOf(arrayLinha[32]), arrayLinha[33], arrayLinha[34],
						arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40],
						arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46],
						arrayLinha[47], arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52],
						arrayLinha[53], arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58],
						arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[61], arrayLinha[63]);

				String inscricaoPrestador = dlp.getCnpjPrestador().trim();
				Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
				try {
					if (p == null || p.getId() == 0 || p.getId() == null) {
						// na hora de processar dados_cadastro estas informa��es
						// tem que ser verificadas
						p = new Prestadores();
						p.setAutorizado("S");
						p.setCelular(dlp.getTelefonePrestador());
						p.setEmail(dlp.getEmailPrestador());
						p.setEnquadramento("N");
						p.setInscricaoPrestador(inscricaoPrestador.trim());
						p.setTelefone(dlp.getTelefonePrestador());
						p = trataNumerosTelefones(p);
						p = anulaCamposVazios(p);
						prestadoresDao.save(p);
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
						p = trataNumerosTelefones(p);
						p = anulaCamposVazios(p);
						prestadoresDao.update(p);

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

	private void mostraProgresso(int linhas, int dadosSize) {
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
			if (ultCarac.equals("#")) {
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
		if (this.mapTomadores == null || this.mapTomadores.size() == 0) {
			buscaTomadores();
		}
		if (this.mapPrestadores == null || this.mapPrestadores.size() == 0) {
			buscaPrestadores();
		}

		int linhas = 0;
		linhasMil = 0;

		for (String linha : dadosList) {
			linhas++;
			linhasMil++;
			mostraProgresso(linhas, dadosList.size());

			try {

				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1],
						arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7],
						arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13],
						arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], Double.valueOf(arrayLinha[18]),
						Double.valueOf(arrayLinha[19]), Double.valueOf(arrayLinha[20]), Double.valueOf(arrayLinha[21]),
						Double.valueOf(arrayLinha[22]), Double.valueOf(arrayLinha[23]), Double.valueOf(arrayLinha[24]),
						arrayLinha[25], Double.valueOf(arrayLinha[26]), Double.valueOf(arrayLinha[27]),
						Double.valueOf(arrayLinha[28]), Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]),
						Double.valueOf(arrayLinha[31]), Double.valueOf(arrayLinha[32]), arrayLinha[33], arrayLinha[34],
						arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40],
						arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46],
						arrayLinha[47], arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52],
						arrayLinha[53], arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58],
						arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[61], arrayLinha[63]);

				String inscricaoPrestador = dlp.getCnpjPrestador().trim();
				Prestadores p = mapPrestadores.get(inscricaoPrestador);
				try {
					if (p == null || p.getId() == 0 || !inscricaoPrestador.trim().equals(p.getInscricaoPrestador())) {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				NotasFiscais nf = new NotasFiscais();
				nf.setDataHoraEmissao(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
				nf.setInscricaoPrestador(dlp.getCnpjPrestador());
				nf.setInscricaoTomador(dlp.getCnpjTomador());
				nf.setNaturezaOperacao(dlp.getNaturezaOperacao());
				nf.setNomePrestador(dlp.getRazaoSocialPrestador());
				nf.setNomeTomador(dlp.getRazaoSocialTomador());
				nf.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().substring(0, 1));
				nf.setPrestadores(p);
				nf.setValorCofins(BigDecimal.valueOf(dlp.getValorCofins()));
				nf.setValorCsll(BigDecimal.valueOf(dlp.getValorCsll()));
				nf.setValorInss(BigDecimal.valueOf(dlp.getValorInss()));
				nf.setValorIr(BigDecimal.valueOf(dlp.getValorIr()));
				nf.setValorLiquido(BigDecimal.valueOf(dlp.getValorTotalNfse()));
				nf.setValorOutrasRetencoes(BigDecimal.valueOf(dlp.getValorOutrasRetencoes()));
				nf.setValorTotalIssOptante(BigDecimal.valueOf(dlp.getValorIss()));
				nf.setValorTotalServico(BigDecimal.valueOf(dlp.getValorTotalNfse()));
				nf.setValorTotalIss(BigDecimal.valueOf(dlp.getValorIss()));
				nf.setSituacao(dlp.getStatusNota().trim().substring(0, 1));
				nf.setSituacaoTributaria(util.getSituacaoTributaria(dlp));
				nf.setDataHoraEmissao(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
				if (dlp.getCodigoVerificacao() != null) {
					if (dlp.getCodigoVerificacao().length() > 9) {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim().substring(0, 9));
					} else {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim());
					}
				} else {
					nf.setNumeroVerificacao("000000000");
				}
				nf.setNaturezaOperacao("1"); // TODO resolver
				nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().substring(0, 1));
				nf.setValorTotalBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
				List<BigDecimal> lista = Arrays.asList(nf.getValorCofins(), nf.getValorCsll(), nf.getValorInss(),
						nf.getValorIr());
				BigDecimal descontos = util.getSumOfBigDecimal(lista);
				nf.setValorLiquido(util.getSubtract(nf.getValorTotalBaseCalculo(), descontos));
				nf.setValorTotalDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
				nf.setServicoPrestadoForaPais("N");
				nf.setDataHoraRps(nf.getDataHoraEmissao());

				notasFiscaisDao.save(nf);

				Tomadores t = mapTomadores.get(nf.getInscricaoTomador().trim());

				processaDemaisTiposNotas(p, nf, dlp, log, linha, t);

			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}

		}
		log.close();

	}

	private void buscaPrestadores() {
		for (Prestadores p : prestadoresDao.findAll()) {
			mapPrestadores.put(p.getInscricaoPrestador(), p);
		}

	}

	private void buscaTomadores() {
		if (tomadoresDao.findAll() != null) {
			for (Tomadores t : tomadoresDao.findAll()) {
				mapTomadores.put(t.getInscricaoTomador().trim(), t);
			}
		}

	}

	private void processaDemaisTiposNotas(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, Tomadores t) {
		// -- serviços
		NotasThreadService nfServico = new NotasThreadService(p, nf, dlp, log, linha, "S");
		Thread s = new Thread(nfServico);
		s.start();

		// -- canceladas
		if (dlp.getStatusNota().substring(0, 1).equals("C")) {
			NotasThreadService nfCanceladas = new NotasThreadService(p, nf, dlp, log, linha, "C");
			Thread c = new Thread(nfCanceladas);
			c.start();
		}

		// email
		if (dlp.getEmailPrestador() != null && !dlp.getEmailPrestador().isEmpty()) {
			NotasThreadService nfEmail = new NotasThreadService(p, nf, dlp, log, linha, "E");
			Thread e = new Thread(nfEmail);
			e.start();
		}

		// notas-fiscais-cond-pagamentos ??

		// notas-fiscais-obras ??

		// notas-fiscais-prestadores
		NotasThreadService nfPrestadores = new NotasThreadService(p, nf, dlp, log, linha, "P");
		Thread prestadoresThread = new Thread(nfPrestadores);
		prestadoresThread.start();

		// guias x notas fiscais
		Guias g = guiasDao.findByNumeroGuia(dlp.getNossoNumero());
		if (g != null) {
			NotasThreadService nfGuias = new NotasThreadService(p, nf, dlp, log, linha, "G", g);
			Thread gThread = new Thread(nfGuias);
			gThread.start();

		}

		if (t != null && t.getId() != null) {
			NotasThreadService nfTomadores = new NotasThreadService(p, nf, dlp, log, linha, "T", g, t);
			Thread nftThread = new Thread(nfTomadores);
			nftThread.start();
		}

	}

	public List<String> lerArquivo(String arquivoIn) {
		BufferedReader br;
		List<String> dadosList = new ArrayList<String>();
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream("c:/TEMP/lagoa/tratados/" + arquivoIn + ".txt"), "UTF-8"));

			br.readLine(); // cabe�alho
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
				"" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores", "Prestadores");

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
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Guias",
				"Competencias");
	}

	public List<String> excluiParaProcessarNivel4() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais");
	}

	private Prestadores trataNumerosTelefones(Prestadores p) {

		if (p.getCelular() != null) {
			p.setCelular(p.getCelular().replaceAll("\\(", ""));
			p.setCelular(p.getCelular().replaceAll("\\)", ""));
			p.setCelular(p.getCelular().replaceAll("-", ""));
		}
		if (p.getTelefone() != null) {
			p.setTelefone(p.getTelefone().replaceAll("\\(", ""));
			p.setTelefone(p.getTelefone().replaceAll("\\)", ""));
			p.setTelefone(p.getTelefone().replaceAll("\\-", ""));
		}

		return p;
	}

	private Prestadores anulaCamposVazios(Prestadores p) {
		if (p.getEmail() != null && p.getEmail().trim().isEmpty()) {
			p.setEmail(null);
		}
		if (p.getTelefone() != null && p.getTelefone().trim().isEmpty()) {
			p.setTelefone(null);
		}
		if (p.getCelular() != null && p.getCelular().trim().isEmpty()) {
			p.setCelular(null);
		}

		return p;
	}

	private Tomadores anulaCamposVazios(Tomadores t) {
		if (t.getEmail() != null && t.getEmail().trim().isEmpty()) {
			t.setEmail(null);
		}
		if (t.getTelefone() != null && t.getTelefone().trim().isEmpty()) {
			t.setTelefone(null);
		}
		if (t.getCelular() != null && t.getCelular().trim().isEmpty()) {
			t.setCelular(null);
		}
		if (t.getInscricaoEstadual() != null && t.getInscricaoEstadual().isEmpty()) {
			t.setInscricaoEstadual(null);
		}
		if (t.getMunicipioIbge() != null && t.getMunicipioIbge().toString().trim().isEmpty()) {
			t.setMunicipioIbge(null);
		}
		if (t.getCep() != null && t.getCep().trim().isEmpty()) {
			t.setCep(null);
		}
		if (t.getComplemento() != null && t.getComplemento().trim().isEmpty()) {
			t.setComplemento(null);
		}

		return t;
	}

	private Tomadores trataNumerosTelefones(Tomadores t) {

		if (t.getCelular() != null) {
			t.setCelular(t.getCelular().replaceAll("\\(", ""));
			t.setCelular(t.getCelular().replaceAll("\\)", ""));
			t.setCelular(t.getCelular().replaceAll("-", ""));
		}
		if (t.getTelefone() != null) {
			t.setTelefone(t.getTelefone().replaceAll("\\(", ""));
			t.setTelefone(t.getTelefone().replaceAll("\\)", ""));
			t.setTelefone(t.getTelefone().replaceAll("\\-", ""));
		}

		return t;
	}

	public Pessoa anulaCamposVazios(Pessoa pessoa) {
		if (pessoa.getEmail() != null && pessoa.getEmail().trim().isEmpty()) {
			pessoa.setEmail(null);
		}
		if (pessoa.getTelefone() != null && pessoa.getTelefone().trim().isEmpty()) {
			pessoa.setTelefone(null);
		}
		if (pessoa.getCelular() != null && pessoa.getCelular().trim().isEmpty()) {
			pessoa.setCelular(null);
		}
		if (pessoa.getInscricaoEstadual() != null && pessoa.getInscricaoEstadual().isEmpty()) {
			pessoa.setInscricaoEstadual(null);
		}
		if (pessoa.getMunicipioIbge() != null && pessoa.getMunicipioIbge().toString().trim().isEmpty()) {
			pessoa.setMunicipioIbge(null);
		}
		if (pessoa.getCep() != null && pessoa.getCep().trim().isEmpty()) {
			pessoa.setCep(null);
		}
		if (pessoa.getComplemento() != null && pessoa.getComplemento().trim().isEmpty()) {
			pessoa.setComplemento(null);
		}

		return pessoa;
	}

	public Pessoa trataNumerosTelefones(Pessoa pessoa) {

		if (pessoa.getCelular() != null) {
			pessoa.setCelular(pessoa.getCelular().replaceAll("\\(", ""));
			pessoa.setCelular(pessoa.getCelular().replaceAll("\\)", ""));
			pessoa.setCelular(pessoa.getCelular().replaceAll("-", ""));
		}
		if (pessoa.getTelefone() != null) {
			pessoa.setTelefone(pessoa.getTelefone().replaceAll("\\(", ""));
			pessoa.setTelefone(pessoa.getTelefone().replaceAll("\\)", ""));
			pessoa.setTelefone(pessoa.getTelefone().replaceAll("\\-", ""));
		}

		return pessoa;
	}

}
