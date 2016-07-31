package cmm.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cmm.dao.GuiasDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.NotasFiscaisCanceladasDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.NotasFiscaisEmailsDao;
import cmm.dao.NotasFiscaisPrestadoresDao;
import cmm.dao.NotasFiscaisServicosDao;
import cmm.dao.NotasFiscaisTomadoresDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.TabLogDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;
import cmm.model.NotasFiscais;
import cmm.model.NotasFiscaisCanceladas;
import cmm.model.NotasFiscaisEmails;
import cmm.model.NotasFiscaisPrestadores;
import cmm.model.NotasFiscaisServicos;
import cmm.model.NotasFiscaisTomadores;
import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
import cmm.model.TabLog;
import cmm.model.Tomadores;
import cmm.util.FileLog;
import cmm.util.Util;

public class NotaMaeThreadService implements Runnable {
	
	private final Util util = new Util();
	private final PrestadoresDao prestadoresDao = new PrestadoresDao();
	private final GuiasDao guiasDao = new GuiasDao();
	private final NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private final PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private int linhasMil = 0;
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private NotasFiscais nf;
	private String linha;
	private FileLog log;
	private List<String> dadosList;
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	private Tomadores tomadores;
	private NotasFiscaisTomadoresDao notasFiscaisTomadoresDao = new NotasFiscaisTomadoresDao();
	private TabLogDao tabLogDao = new TabLogDao();
	
	public NotaMaeThreadService(List<String> dadosList, FileLog log) {
		
		this.log = log;
		this.dadosList = dadosList;
		
	}

	@Override
	public void run() {
		int linhas = 0;
		for (String linha : dadosList) {

			if (linhasMil == 1000) {
				util.mostraProgresso(linhas, dadosList.size());
			}

			//linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("@@\\|");

			try {

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

					processaNotas(dlp);
				

			} catch (Exception e) {
				log.fillError(linha, e.getMessage());
				gravaTabLog(linha, "NotasFiscais", e);
				e.printStackTrace();
			}

		}
		
		
		
	}
	
	private void gravaTabLog(String linha, String entidade, Exception e) {
		TabLog tabLog = new TabLog(linha, entidade, e);
		tabLogDao.save(tabLog);
	}

	private void processaNotas(DadosLivroPrestador dlp) {
		String inscricaoPrestador = dlp.getCnpjPrestador().trim();;
		Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
		try {
			if (p == null || p.getId() == 0 || !p.getInscricaoPrestador().equals(inscricaoPrestador.trim())) {
				System.out.println("Prestador não encontrado:" + inscricaoPrestador);
				throw new Exception("Prestador não encontrado:" + inscricaoPrestador);
			}
		} catch (Exception e) {
			gravaTabLog(linha, "NotasFiscais", e);
			e.printStackTrace();
		}

		nf = new NotasFiscais();
		// nf.setDataHoraEmissao(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
		nf.setDataHoraEmissao(util.getStringToDate(dlp.getDataCompetencia())); // definido
																				// pela
																				// cmm
		nf.setInscricaoPrestador(dlp.getCnpjPrestador());

		if ("F".equals(util.getTipoPessoa(dlp.getCnpjTomador()))) {
			if (Util.validarCpf(dlp.getCnpjTomador())) {
				nf.setInscricaoTomador(dlp.getCnpjTomador());
				nf.setNomeTomador(dlp.getRazaoSocialTomador());
			}
		} else if ("J".equals(util.getTipoPessoa(dlp.getCnpjTomador()))) {
			if (Util.validarCnpj(dlp.getCnpjTomador())) {
				nf.setInscricaoTomador(dlp.getCnpjTomador());
				nf.setNomeTomador(dlp.getRazaoSocialTomador());
			}
		}

		nf.setNaturezaOperacao(dlp.getNaturezaOperacao());
		nf.setNomePrestador(dlp.getRazaoSocialPrestador());
		nf.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
		nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().substring(0, 1));
		nf.setPrestadores(p);
		if ("J".equals(util.getTipoPessoa(p.getInscricaoPrestador()))) {
			nf.setValorCofins(BigDecimal.valueOf(dlp.getValorCofins()));
			nf.setValorCsll(BigDecimal.valueOf(dlp.getValorCsll()));
		}
		nf.setValorInss(BigDecimal.valueOf(dlp.getValorInss()));
		nf.setValorIr(BigDecimal.valueOf(dlp.getValorIr()));
		nf.setValorOutrasRetencoes(BigDecimal.valueOf(dlp.getValorOutrasRetencoes()));
		nf.setValorTotalIssOptante(BigDecimal.valueOf(dlp.getValorIss()));
		nf.setValorTotalServico(BigDecimal.valueOf(dlp.getValorTotalNfse()));
		nf.setValorTotalIss(BigDecimal.valueOf(dlp.getValorIss()));
		nf.setSituacaoOriginal(dlp.getStatusNota().trim().substring(0, 1));
		nf.setSituacao("N");
		nf.setSituacaoTributaria(util.getSituacaoTributaria(dlp));
		if (dlp.getCodigoVerificacao() != null && !dlp.getCodigoVerificacao().trim().isEmpty()) {
			if (dlp.getCodigoVerificacao().length() > 9) {
				nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim().substring(0, 9));
			} else {
				nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim());
			}
		} else {
			nf.setNumeroVerificacao(util.completarZerosEsquerda(dlp.getIdCodigo().toString(), 9));
		}
		nf.setNaturezaOperacao("1"); // TODO resolver
		nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().substring(0, 1));
		nf.setValorTotalBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
		nf.setValorTotalDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
		nf.setServicoPrestadoForaPais("N");
		// nf.setDataHoraRps(nf.getDataHoraEmissao());

		List<BigDecimal> lista = Arrays.asList(nf.getValorCofins(), nf.getValorCsll(), nf.getValorInss(),
				nf.getValorIr());
		BigDecimal descontos = util.getSumOfBigDecimal(lista);

		nf.setValorLiquido(util.getSubtract(BigDecimal.valueOf(dlp.getValorTotalNfse()), descontos));
		if (nf.getValorLiquido().compareTo(BigDecimal.ZERO) == -1) {
			nf.setValorLiquido(nf.getValorLiquido().multiply(BigDecimal.valueOf(-1)));
		}

		try {
			nf = notasFiscaisDao.save(nf);
		} catch (Exception e) {
			e.printStackTrace();
			gravaTabLog(linha, "NotasFiscais", e);
			log.fillError(linha, e);

			try {
				NotasFiscais outraNf = notasFiscaisDao.findNotaExistente(nf);
				if (outraNf != null) {
					nf.setId(outraNf.getId());
					String msg = "Nota duplicada-> numero:" + nf.getNumeroNota() + " prestador:"
							+ nf.getInscricaoPrestador()+"\n";
					msg += "Nota duplicada-> nf nova valor:" + nf.getValorTotalServico().doubleValue()
							+ " nf banco valor:" + outraNf.getValorTotalServico().doubleValue();
					log.fillError(linha, msg);
				}
			} catch (Exception e1) {
				log.fillError(linha, e.getMessage());
				gravaTabLog(linha, "NotasFiscais", e);
			}
		}

		// tomadores
		Tomadores t = null;

		if (!util.isEmptyOrNull(nf.getInscricaoTomador()) && !util.isEmptyOrNull(dlp.getRazaoSocialTomador())) {
			t = tomadoresDao.findByInscricao(nf.getInscricaoTomador(), nf.getInscricaoPrestador());
			if (t == null || t.getId() == null) {
				try {
					t = new Tomadores();
					t.setOptanteSimples(util.getOptantePeloSimplesNacional("N"));
					t.setNome(dlp.getRazaoSocialTomador());
					t.setNomeFantasia(dlp.getRazaoSocialTomador());
					t.setPrestadores(nf.getPrestadores());
					t.setTipoPessoa(util.getTipoPessoa(nf.getInscricaoTomador()));
					t.setInscricaoTomador(nf.getInscricaoTomador());
					t.setBairro(dlp.getEnderecoBairroTomador());
					t.setCep(util.trataCep(dlp.getCepTomador()));
					t.setComplemento(dlp.getEnderecoComplementoTomador());
					t.setEmail(util.trataEmail(dlp.getEmailTomador()));
					t.setEndereco(dlp.getEnderecoTomador());
					t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
					t.setInscricaoMunicipal(dlp.getInscricaoMunicipalTomador());
					t.setMunicipio(dlp.getMunicipioTomador());
					t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador().trim()));
					try {
						t.setMunicipioIbge(Long.valueOf(
								municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador())));
					} catch (Exception e) {
						// log.fillError(linha, e);
						// e.printStackTrace();
						gravaTabLog(linha, "Tomador", e);
					}

					util.trataNumerosTelefones(t);
					util.anulaCamposVazios(t);

					// S� salvar tomador se a inscri��o n�o for = 00000000000;
					if (!"".equals(t.getInscricaoTomador().replace("0", "").trim())) {
						t = tomadoresDao.save(t);
					}

				} catch (Exception e) {
					log.fillError(linha, e.getMessage());
					gravaTabLog(linha, "Tomadores", e);
					e.printStackTrace();
					t = null;
				}

			}
		}

		// -- serviços
		gravaServico(p, nf, dlp, log, linha, "S");

		// -- canceladas
		if ("C".equals(dlp.getStatusNota().substring(0, 1))) {
			gravaCanceladas(p, nf, dlp, log, linha, "C");
		}

		// email
		if (dlp.getEmailPrestador() != null && !dlp.getEmailPrestador().isEmpty()) {
			gravaEmail(p, nf, dlp, log, linha, "E");
		}

		// notas-fiscais-cond-pagamentos ??

		// notas-fiscais-obras ??

		// notas-fiscais-prestadores
		gravaNotasPrestadores(p, nf, dlp, log, linha, "P");

		// guias x notas fiscais
		Guias g = new Guias();
		if (dlp.getNossoNumero() != null && !dlp.getNossoNumero().trim().isEmpty()) {
			g = guiasDao.findByNumeroGuia(dlp.getNossoNumero());
			if (g != null) {
				gravaGuiasNotas(p, nf, dlp, log, linha, "G", g);

			} else {
				System.out.println("Numero de guia não encontrado: " + dlp.getNossoNumero());

			}
		}

		// notas fiscais tomadores

		if (t != null && t.getId() != null) {
			gravaNotasTomadores(p, nf, dlp, log, linha, "T", g, t);
		}

	}

	private void gravaNotasTomadores(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2,
			String linha2, String tipoNotaFilha, Guias g, Tomadores tomadores) {
		if ("T".equals(tipoNotaFilha) && tomadores != null && tomadores.getId() != null) {
			try {
				NotasFiscaisTomadores nft = new NotasFiscaisTomadores();
				nft.setBairro(tomadores.getBairro());
				nft.setCelular(util.getLimpaTelefone(tomadores.getCelular()));
				nft.setCep(util.trataCep(tomadores.getCep()));
				nft.setComplemento(tomadores.getComplemento());
				nft.setEmail(util.trataEmail(tomadores.getEmail()));
				nft.setEndereco(tomadores.getEndereco());
				nft.setInscricaoEstadual(tomadores.getInscricaoEstadual());
				nft.setInscricaoMunicipal(tomadores.getInscricaoMunicipal());
				nft.setInscricaoPrestador(nf.getInscricaoPrestador());
				nft.setInscricaoTomador(tomadores.getInscricaoTomador());
				nft.setMunicipio(tomadores.getMunicipio());
				if (tomadores.getMunicipioIbge() != null) {
					nft.setMunicipioIbge(Long.toString(tomadores.getMunicipioIbge()));
				}
				nft.setNome(tomadores.getNome());
				nft.setNomeFantasia(tomadores.getNomeFantasia());
				if (nft.getNomeFantasia() == null) {
					nft.setNomeFantasia(tomadores.getNome());
				}
				nft.setNotasFiscais(nf);
				nft.setNumero(tomadores.getNumero());
				nft.setNumeroNota(nf.getNumeroNota());
				nft.setOptanteSimples(tomadores.getOptanteSimples());
				nft.setTipoPessoa(tomadores.getTipoPessoa());
				notasFiscaisTomadoresDao.save(nft);
				nft = null;
				tomadores = null;
			} catch (Exception e) {
				gravaTabLog(linha, "Tomadores", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private void gravaGuiasNotas(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2, String linha2,
			String tipoNotaFilha, Guias g) {
		if ("G".equals(tipoNotaFilha)) { // guias
			try {
				GuiasNotasFiscais gnf = new GuiasNotasFiscais();
				gnf.setGuias(g);
				gnf.setInscricaoPrestador(p.getInscricaoPrestador());
				// gnf.setNumeroGuia(g.getNumeroGuia());
				gnf.setNumeroGuia(g.getNumeroGuia()); // acertar depois
				gnf.setNumeroNota(nf.getNumeroNota());
				guiasNotasFiscaisDao.save(gnf);
				gnf = null;
			} catch (Exception e) {
				gravaTabLog(linha, "GuiasNotasFiscais", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	private void gravaNotasPrestadores(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2,
			String linha2, String tipoNotaFilha) {
		if ("P".equals(tipoNotaFilha)) { // prestadores
			try {
				NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
				nfp.setBairro(dlp.getEnderecoBairroPrestador());
				nfp.setCelular(util.trataTelefone(util.getLimpaTelefone(dlp.getTelefonePrestador())));
				nfp.setCep(dlp.getCepPrestador());
				nfp.setComplemento(dlp.getEnderecoComplementoPrestador());
				nfp.setEmail(p.getEmail());
				nfp.setEndereco(dlp.getEnderecoPrestador());
				nfp.setInscricaoPrestador(dlp.getCnpjPrestador());
				nfp.setNome(dlp.getRazaoSocialPrestador());
				nfp.setNomeFantasia(dlp.getNomeFantasiaPrestador());
				nfp.setNotasFiscais(nf);
				nfp.setNumero(dlp.getEnderecoNumeroPrestador());
				nfp.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				nfp.setOptanteSimples(dlp.getOptantePeloSimplesNacional().substring(0, 1));
				nfp.setTipoPessoa(util.getTipoPessoa(dlp.getCnpjPrestador()));
				notasFiscaisPrestadoresDao.save(nfp);
				nfp = null;
			} catch (Exception e) {
				gravaTabLog(linha, "NotasFiscaisPrestadores", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	private void gravaEmail(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2,
			String linha2, String tipoNotaFilha) {

		if ("E".equals(tipoNotaFilha)) { // email
			try {
				NotasFiscaisEmails nfe = new NotasFiscaisEmails();
				nfe.setEmail(p.getEmail());
				nfe.setInscricaoPrestador(dlp.getCnpjPrestador());
				nfe.setNotasFiscais(nf);
				nfe.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				notasFiscaisEmailsDao.save(nfe);
				nfe = null;
			} catch (Exception e) {
				gravaTabLog(linha, "NotasFiscaisEmail", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	private void gravaCanceladas(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2, String linha2,
			String tipoNotaFilha) {
		if ("C".equals(tipoNotaFilha)) { // canceladas
			try {
				NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
				nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataCancelamento()));
				if (nfc.getDatahoracancelamento().getTime() < nf.getDataHoraEmissao().getTime()) {
					nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
				}
				nfc.setInscricaoPrestador(dlp.getCnpjPrestador());
				nfc.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				nfc.setMotivo(dlp.getMotivoCancelamento());
				if (util.isEmptyOrNull(nfc.getMotivo())) {
					nfc.setMotivo("Dados incorretos");
				}
				nfc.setNotasFiscais(nf);
				notasFiscaisCanceladasDao.save(nfc);
				nfc = null;
			} catch (Exception e) {
				gravaTabLog(linha, "NotasFiscaisCanceladas", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	private void gravaServico(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2, String linha2,
			String tipoNotaFilha) {
		if (("S").equals(tipoNotaFilha) && !"C".equals(nf.getSituacaoOriginal())) { // serviços
			try {

				NotasFiscaisServicos nfs = new NotasFiscaisServicos();
				nfs.setInscricaoPrestador(dlp.getCnpjPrestador());
				nfs.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				nfs.setMunicipioIbge(util.CODIGO_IBGE);
				PrestadoresAtividades pa = prestadoresAtividadesDao.findByInscricao(nfs.getInscricaoPrestador());
				if (pa == null || pa.getId() == null) {
					nfs.setItemListaServico("1401");
				} else {
					nfs.setItemListaServico(util.completarZerosEsquerda(pa.getCodigoAtividade(), 4));
				}
				nfs.setDescricao(dlp.getDiscriminacaoServico());
				if (util.isEmptyOrNull(nfs.getDescricao().trim())) {
					nfs.setDescricao("Serviços Diversos");
				}
				nfs.setAliquota(BigDecimal.valueOf(dlp.getValorAliquota()));
				nfs.setQuantidade(BigDecimal.valueOf(1));
				nfs.setValorServico(BigDecimal.valueOf(dlp.getValorServico()));
				nfs.setValorDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
				nfs.setValorDescCondicionado(BigDecimal.valueOf(dlp.getValorDescontoCondicionado()));
				nfs.setValorDescIncondicionado(BigDecimal.valueOf(dlp.getValorDescontoIncondicionado()));
				nfs.setValorBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
				nfs.setValorIss(BigDecimal.valueOf(dlp.getValorIss()));
				nfs.setNotasFiscais(nf);
				nfs.setValorUnitario(BigDecimal.valueOf(dlp.getValorServico()));
				if (nfs.getAliquota().compareTo(BigDecimal.ZERO) == 0) {
					nfs.setAliquota(BigDecimal.valueOf(1));
				}
				notasFiscaisServicosDao.save(nfs);
				nfs = null;
			} catch (Exception e) {
				gravaTabLog(linha, "NotasFiscaisServicos", e);
				log.fillError(linha, e.getMessage());
				e.printStackTrace();
			}
			
		}


		
	}

		

}
