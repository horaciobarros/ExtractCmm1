package cmm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cmm.dao.CompetenciasDao;
import cmm.dao.Dao;
import cmm.dao.GuiasDao;
import cmm.dao.NotasFiscaisCanceladasDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.NotasFiscaisEmailsDao;
import cmm.dao.NotasFiscaisPrestadoresDao;
import cmm.dao.NotasFiscaisServicosDao;
import cmm.dao.PagamentosDao;
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
import cmm.model.NotasFiscaisCanceladas;
import cmm.model.NotasFiscaisEmails;
import cmm.model.NotasFiscaisPrestadores;
import cmm.model.NotasFiscaisServicos;
import cmm.model.Pagamentos;
import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
import cmm.model.PrestadoresOptanteSimples;
import cmm.model.Tomadores;
import cmm.util.FileLog;
import cmm.util.Util;

public class ExtractorService {
	private Map<String, DadosGuia> dadosGuiaMap;
	private Util util = new Util();
	private CompetenciasDao competenciasDao = new CompetenciasDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private Dao dao = new Dao();
	private GuiasDao guiasDao = new GuiasDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();

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
						pa.setIcnaes(dca.getAtividadeFederal());
						pa.setIlistaservicos(dca.getGrupoAtividade());
						pa.setInscricaoPrestador(dca.getCnpj());
						pa.setPrestadores(p);
						prestadoresAtividadesDao.save(pa);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				log.fillError(linha, e);
			}

		}
		log.close();

	}

	public void processaDadosCadastro(List<String> dadosList) {
		FileLog log = new FileLog("dados_cadastro");
		for (String linha : dadosList) {
			try {
				String[] arrayLinha = linha.split("\\|");
				DadosCadastro dc = new DadosCadastro(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3],
						arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9],
						arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15],
						arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21],
						arrayLinha[22]);
				// ajustando prestadores
				Prestadores p = prestadoresDao.findByInscricao(dc.getCnpj().trim());
				try {
					if (p == null || p.getId() == 0) {
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

						prestadoresDao.save(p);
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
					if (t == null || t.getId() == 0) {
						t = new Tomadores();
						t.setCelular(dc.getTelefone());
						t.setEmail(dc.getEmail());
						t.setInscricaoTomador(inscricaoTomador.trim());
						t.setBairro(dc.getEnderecoBairro());
						t.setCep(dc.getEnderecoCep());
						t.setComplemento(dc.getEnderecoComplemento());
						t.setInscricaoMunicipal(dc.getInscricaoMunicipal());
						t.setMunicipio(dc.getMunicipio());
						t.setNome(dc.getRazaoSocial());
						t.setNomeFantasia(dc.getNomeFantasia());
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
						tomadoresDao.update(t);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.fillError(linha, e);
			}

		}
		log.close();

	}

	public void processaDadosGuiaCompetencias(List<String> dadosList) {
		FileLog log = new FileLog("dados_guia");

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
				String descricao = util.getNomeMes(dg.getMes()) + "/" + dg.getAno();
				Competencias cp = competenciasDao.findByDescricao(descricao);
				try {
					if (cp == null || cp.getId() == 0) { // acertar datas
						cp = new Competencias();
						cp.setDescricao(descricao.trim());
						cp.setDataInicio(util.getStringToDateHoursMinutes(dg.getDataBoleto()));
						cp.setDataFim(util.getStringToDateHoursMinutes(dg.getDataBoleto()));
						cp.setDataVencimento(util.getStringToDateHoursMinutes(dg.getDataBoleto()));
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
					guias.setInscricaoPrestador(dg.getInscMunicipal());
					guias.setIntegrarGuia("S"); // TODO sanar dúvida
					guias.setNumeroGuia(Long.valueOf(dg.getNossoNumero()));
					Prestadores prestadores = prestadoresDao.findByInscricao(dg.getCnpj().trim());
					if (prestadores == null) {
						log.fillError(linha, "Prestador não encontrado:" + dg.getInscMunicipal());
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

					guias.setTipo(dg.getTipoGuia().substring(0, 1));
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
							p.setNumeroGuia(Long.valueOf(dg.getParcela()));
							p.setNumeroPagamento(Long.valueOf(dg.getParcela()));
							p.setTipoPagamento(dg.getBaixaTipo().substring(0, 1));
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
		
		FileLog log = new FileLog("dados_livro_tomador");

		for (String linha : dadosList) {
			try {
				linha = preparaParaSplit(linha);
				String[] arrayLinha = linha.split("#");
				DadosLivroTomador dlt = new DadosLivroTomador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2],
						arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8],
						arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14],
						arrayLinha[15], arrayLinha[16], arrayLinha[17], Double.valueOf(arrayLinha[18]),
						Double.valueOf(arrayLinha[19]), Double.valueOf(arrayLinha[20]), Double.valueOf(arrayLinha[21]),
						Double.valueOf(arrayLinha[22]), Double.valueOf(arrayLinha[23]), Double.valueOf(arrayLinha[24]),
						arrayLinha[25], Double.valueOf(arrayLinha[26]), Double.valueOf(arrayLinha[27]),
						Double.valueOf(arrayLinha[28]), Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]),
						Double.valueOf(arrayLinha[31]), Double.valueOf(arrayLinha[32]), Double.valueOf(arrayLinha[33]),
						arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39],
						arrayLinha[40], arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45],
						arrayLinha[46], arrayLinha[47], arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51],
						arrayLinha[52], arrayLinha[53], arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57],
						arrayLinha[58], arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[62], arrayLinha[63],
						arrayLinha[64], arrayLinha[65]);
				String inscricaoTomador = dlt.getCnpjPrestador().trim();
				Tomadores t = tomadoresDao.findByInscricao(inscricaoTomador);
				try {
					if (t == null || t.getId() == 0) {
						// na hora de processar dados_cadastro estas informações
						// tem que ser verificadas
						t = new Tomadores();
						t.setCelular(dlt.getTelefoneTomador());
						t.setEmail(dlt.getEmailTomador());
						t.setTelefone(dlt.getTelefoneTomador());
						t.setInscricaoTomador(inscricaoTomador.trim());
						t.setBairro(dlt.getEnderecoBairroTomador());
						t.setCelular(dlt.getTelefoneTomador());
						t.setCep(dlt.getCepTomador());
						t.setComplemento(dlt.getCepTomador());
						t.setInscricaoEstadual(dlt.getInscricaoEstadualTomador());
						t.setInscricaoMunicipal(dlt.getInscricaoMunicipalTomador());
						t.setMunicipio(dlt.getCidadeTomador());
						t.setNome(dlt.getNomeFantasiaTomador());
						t.setNomeFantasia(dlt.getNomeFantasiaTomador());
						t.setNumero(dlt.getEnderecoNumeroTomador());
						t.setOptanteSimples(util.getOptantePeloSimplesNacional(dlt.getOptantePeloSimplesNacional()));
						t.setTelefone(dlt.getTelefoneTomador());
						Prestadores p = prestadoresDao.findByInscricao(dlt.getCnpjPrestador().trim());
						t.setPrestadores(p);
						t.setTipoPessoa(util.getTipoPessoa(dlt.getCnpjTomador().trim()));
						tomadoresDao.save(t);
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

	public void processaDadosLivroPrestador(List<String> dadosList) {
		FileLog log = new FileLog("dados_livro_prestador");

		for (String linha : dadosList) {
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
					if (p == null || p.getId() == 0) {
						// na hora de processar dados_cadastro estas informações
						// tem que ser verificadas
						p = new Prestadores();
						p.setAutorizado("S");
						p.setCelular(dlp.getTelefonePrestador());
						p.setEmail(dlp.getEmailPrestador());
						p.setEnquadramento("N");
						p.setInscricaoPrestador(inscricaoPrestador.trim());
						p.setTelefone(dlp.getTelefonePrestador());
						prestadoresDao.save(p);
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

		for (String linha : dadosList) {
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
					if (p == null || p.getId() == 0) {
						// na hora de processar dados_cadastro estas informações
						// tem que ser verificadas
						p = new Prestadores();
						p.setAutorizado("S");
						p.setCelular(dlp.getTelefonePrestador());
						p.setEmail(dlp.getEmailPrestador());
						p.setEnquadramento("N");
						p.setInscricaoPrestador(inscricaoPrestador.trim());
						p.setTelefone(dlp.getTelefonePrestador());
						prestadoresDao.save(p);
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
				nf.setSituacaoTributaria(dlp.getRegimeTributacao().trim().substring(0, 1));
				nf.setDataHoraEmissao(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
				if (dlp.getCodigoVerificacao() != null) {
					if (dlp.getCodigoVerificacao().length() > 9) {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim().substring(0, 9));
					} else {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim());
					}
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

				// -- serviços
				try {

					NotasFiscaisServicos nfs = new NotasFiscaisServicos();
					nfs.setInscricaoPrestador(dlp.getCnpjPrestador());
					nfs.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
					nfs.setMunicipioIbge(util.CODIGO_IBGE);
					nfs.setItemListaServico("0001");
					nfs.setDescricao(dlp.getDiscriminacaoServico());
					nfs.setAliquota(BigDecimal.valueOf(dlp.getValorAliquota()));
					nfs.setValorServico(BigDecimal.valueOf(dlp.getValorServico()));
					nfs.setQuantidade(BigDecimal.valueOf(1));
					nfs.setValorServico(BigDecimal.valueOf(dlp.getValorServico()));
					nfs.setValorDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
					nfs.setValorDescCondicionado(BigDecimal.valueOf(dlp.getValorDescontoCondicionado()));
					nfs.setValorDescIncondicionado(BigDecimal.valueOf(dlp.getValorDescontoIncondicionado()));
					nfs.setValorBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
					nfs.setAliquota(BigDecimal.valueOf(dlp.getValorAliquota()));
					nfs.setValorIss(BigDecimal.valueOf(dlp.getValorIss()));
					nfs.setNotasFiscais(nf);
					nfs.setValorUnitario(BigDecimal.valueOf(dlp.getValorServico()));
					notasFiscaisServicosDao.save(nfs);
				} catch (Exception e) {
					log.fillError(linha, e);
					e.printStackTrace();
				}

				// -- canceladas
				if (dlp.getStatusNota().substring(0, 1).equals("C")) {
					try {
						NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
						nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataCancelamento()));
						nfc.setInscricaoPrestador(dlp.getCnpjPrestador());
						nfc.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
						nfc.setMotivo(dlp.getMotivoCancelamento());
						nfc.setNotasFiscais(nf);

					} catch (Exception e) {
						log.fillError(linha, e);
						e.printStackTrace();
					}
				}

				// email
				if (dlp.getEmailPrestador() != null && !dlp.getEmailPrestador().isEmpty()) {
					try {
						NotasFiscaisEmails nfe = new NotasFiscaisEmails();
						nfe.setEmail(dlp.getEmailPrestador());
						nfe.setInscricaoPrestador(dlp.getCnpjPrestador());
						nfe.setNotasFiscais(nf);
						nfe.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
						notasFiscaisEmailsDao.save(nfe);
					} catch (Exception e) {
						log.fillError(linha, e);
						e.printStackTrace();
					}

				}

				// notas-fiscais-cond-pagamentos ??

				// notas-fiscais-obras ??

				// notas-fiscais-prestadores
				try {
					NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
					nfp.setBairro(dlp.getEnderecoBairroPrestador());
					nfp.setCelular(dlp.getTelefonePrestador());
					nfp.setCep(dlp.getCepPrestador());
					nfp.setComplemento(dlp.getEnderecoComplementoPrestador());
					nfp.setEmail(dlp.getTelefonePrestador());
					nfp.setEndereco(dlp.getEnderecoPrestador());
					nfp.setInscricaoPrestador(dlp.getCnpjPrestador());
					nfp.setNome(dlp.getRazaoSocialPrestador());
					nfp.setNomeFantasia(dlp.getNomeFantasiaPrestador());
					nfp.setNotasFiscais(nf);
					nfp.setNumero(dlp.getEnderecoNumeroPrestador());
					nfp.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
					nfp.setOptanteSimples(dlp.getOptantePeloSimplesNacional().substring(0, 1));
					nfp.setCep(dlp.getCepPrestador());
					nfp.setTipoPessoa(util.getTipoPessoa(dlp.getCnpjPrestador()));
					notasFiscaisPrestadoresDao.save(nfp);

				} catch (Exception e) {
					log.fillError(linha, e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.fillError(linha, e);
			}

		}
		log.close();

	}
	
	public List<String> lerArquivo(String arquivoIn) {
		File file;
		file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			List<String> dadosList = new ArrayList<String>();
			try {
				br.readLine(); // cabeçalho
				while (br.ready()) {
					String linha = br.readLine();
					dadosList.add(linha);
				}
				br.close();
				fr.close();
				return dadosList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<String> lerArquivo(String arquivoIn, int qtdeCampos) {
		File file, fileWr;
		file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
		fileWr = new File("c:/TEMP/lagoa/txts_corrigidos/" + arquivoIn + "_new.txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(fileWr);
			BufferedWriter bw = new BufferedWriter(fw);
			List<String> dadosList = new ArrayList<String>();
			try {
				br.readLine(); // cabeçalho
				while (br.ready()) {
					StringBuilder linhaDefinitiva = new StringBuilder();
					String[] arrayAux = { "", "" };

					while (arrayAux != null && arrayAux.length < qtdeCampos) {
						String linha = br.readLine();
						linha = preparaParaSplit(linha);
						linhaDefinitiva.append(linha);
						arrayAux = linhaDefinitiva.toString().split("#");
					}

					dadosList.add(linhaDefinitiva.toString());
					bw.write(linhaDefinitiva.toString() + "\n");

				}
				br.close();
				fr.close();
				bw.close();
				fw.close();
				return dadosList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


}
