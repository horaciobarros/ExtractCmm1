package cmm.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cmm.dao.DadosLivroPrestadorDao;
import cmm.dao.GuiasDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Guias;
import cmm.model.NotasFiscais;
import cmm.model.Prestadores;
import cmm.model.Tomadores;
import cmm.util.FileLog;
import cmm.util.Util;

public class NotasMaeThread implements Runnable {

	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private DadosLivroPrestadorDao dadosLivroPrestadorDao = new DadosLivroPrestadorDao();
	private GuiasDao guiasDao = new GuiasDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private String linha;
	private Util util;
	private FileLog log;
	private NotasFiscais nf;

	public NotasMaeThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		// linha = preparaParaSplit(linha);
		String[] arrayLinha = linha.split("@@\\|");

		try {

			DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
					arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13],
					arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], util.corrigeDouble(arrayLinha[18]), util.corrigeDouble(arrayLinha[19]),
					util.corrigeDouble(arrayLinha[20]), util.corrigeDouble(arrayLinha[21]), util.corrigeDouble(arrayLinha[22]), util.corrigeDouble(arrayLinha[23]),
					util.corrigeDouble(arrayLinha[24]), arrayLinha[25], util.corrigeDouble(arrayLinha[26]), util.corrigeDouble(arrayLinha[27]),
					util.corrigeDouble(arrayLinha[28]), util.corrigeDouble(arrayLinha[29]), util.corrigeDouble(arrayLinha[30]), util.corrigeDouble(arrayLinha[31]),
					util.corrigeDouble(arrayLinha[32]), util.corrigeDouble(arrayLinha[33]), arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37],
					arrayLinha[38], arrayLinha[39], arrayLinha[40], arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46],
					arrayLinha[47], arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52], arrayLinha[53], arrayLinha[54], arrayLinha[55],
					arrayLinha[56], arrayLinha[57], arrayLinha[58], arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[62], arrayLinha[63], arrayLinha[64]);

			if (!dadosLivroPrestadorDao.exists(dlp.getIdCodigo())) {
				dlp = dadosLivroPrestadorDao.save(dlp);
			}

			processaDlp(dlp, log, linha);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processaDlp(DadosLivroPrestador dlp, FileLog log, String linha) {
		String inscricaoPrestador = util.getCpfCnpj(dlp.getCnpjPrestador());

		Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
		try {
			if (p == null || p.getId() == 0 || !p.getInscricaoPrestador().equals(inscricaoPrestador.trim())) {
				System.out.println("Prestador nÃ£o encontrado:" + inscricaoPrestador);
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		nf = new NotasFiscais();
		nf.setDadosLivroPrestador(dlp);
		// nf.setDataHoraEmissao(util.getStringToDateHoursMinutes(dlp.getDataEmissao()));
		nf.setDataHoraEmissao(util.getStringToDate(dlp.getDataCompetencia())); // definido
																				// pela
																				// cmm
		nf.setInscricaoPrestador(inscricaoPrestador);
		String inscricaoTomador = util.getCpfCnpj(dlp.getCnpjTomador());
		if ("F".equals(util.getTipoPessoa(inscricaoTomador))) {
			if (Util.validarCpf(inscricaoTomador)) {
				nf.setInscricaoTomador(inscricaoTomador);
				nf.setNomeTomador(dlp.getRazaoSocialTomador());
			}
		} else if ("J".equals(util.getTipoPessoa(inscricaoTomador))) {
			if (Util.validarCnpj(inscricaoTomador)) {
				nf.setInscricaoTomador(inscricaoTomador);
				nf.setNomeTomador(dlp.getRazaoSocialTomador());
			}
		}

		if ("Tributação no municipio".equals(dlp.getNaturezaOperacao().trim())) {
			nf.setNaturezaOperacao("1");
		} else if ("Tributação fora do municipio".equals(dlp.getNaturezaOperacao().trim())) {
			nf.setNaturezaOperacao("2");
		} else if ("Isenção".equals(dlp.getNaturezaOperacao().trim())) {
			nf.setNaturezaOperacao("3");
		}

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
		nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().substring(0, 1));
		nf.setValorTotalBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
		nf.setValorTotalDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
		nf.setServicoPrestadoForaPais("N");

		List<BigDecimal> lista = Arrays.asList(nf.getValorCofins(), nf.getValorCsll(), nf.getValorInss(), nf.getValorIr());
		BigDecimal descontos = util.getSumOfBigDecimal(lista);

		nf.setValorLiquido(util.getSubtract(BigDecimal.valueOf(dlp.getValorTotalNfse()), descontos));
		if (nf.getValorLiquido().compareTo(BigDecimal.ZERO) == -1) {
			nf.setValorLiquido(nf.getValorLiquido().multiply(BigDecimal.valueOf(-1)));
		}

		try {
			nf = notasFiscaisDao.save(nf);
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha,"Nota Fiscal ", e);

			try {
				NotasFiscais outraNf = notasFiscaisDao.findNotaExistente(nf);
				if (outraNf != null) {
					nf.setId(outraNf.getId());
					String msg = "Nota duplicada-> numero:" + nf.getNumeroNota() + " prestador:" + nf.getInscricaoPrestador() + "\n";
					msg += "Nota duplicada-> nf nova valor:" + nf.getValorTotalServico().doubleValue() + " nf banco valor:"
							+ outraNf.getValorTotalServico().doubleValue();
					log.fillError(linha, msg);
				}
			} catch (Exception e1) {
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
					t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
					try {
						t.setMunicipioIbge(Long.valueOf(municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador())));
					} catch (Exception e) {
						//log.fillError(linha,"Nota Fiscal Tomadores ", e);
						//e.printStackTrace();
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
					log.fillError(linha,"Nota Fiscal Tomadores ", e);
					t = null;
				}

			}
		} else {

			if (util.isEmptyOrNull(nf.getInscricaoTomador())) {
				String nomeTomador = dlp.getRazaoSocialTomador();
				if (util.isEmptyOrNull(nomeTomador)) {
					nomeTomador = "Não Informado";
				}

				t = tomadoresDao.findByNome(nomeTomador);

				if (t == null || t.getId() == null) { // incluindo tomador
														// ficticio
					String inscricaoTomadorFicticio = util.geraCpfFicticio();

					t = new Tomadores();
					t.setOptanteSimples(util.getOptantePeloSimplesNacional("N"));
					t.setNome(nomeTomador);
					t.setPrestadores(nf.getPrestadores());
					t.setTipoPessoa("O");
					t.setInscricaoTomador(inscricaoTomadorFicticio);
					t.setBairro(dlp.getEnderecoBairroTomador());
					t.setCep(util.trataCep(dlp.getCepTomador()));
					t.setComplemento(dlp.getEnderecoComplementoTomador());
					t.setEmail(util.trataEmail(dlp.getEmailTomador()));
					t.setEndereco(dlp.getEnderecoTomador());
					t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
					t.setInscricaoMunicipal(dlp.getInscricaoMunicipalTomador());
					t.setMunicipio(dlp.getMunicipioTomador());
					t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
					try {
						t.setMunicipioIbge(Long.valueOf(municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador())));
					} catch (Exception e) {
						t.setMunicipioIbge(Long.valueOf(util.CODIGO_IBGE));
						e.printStackTrace();
					}

					util.trataNumerosTelefones(t);
					util.anulaCamposVazios(t);

					t.setTomadorFicticio("S");

					t = tomadoresDao.save(t);

					log.fillError(linha, "Warn: foi gravado gerado um cpf ficticio para o tomador " + nomeTomador);
				}
			}
		}

		// -- serviÃ§os
		NotasThreadService nfServico = new NotasThreadService(p, nf, dlp, log, linha, "S");
		Thread s = new Thread(nfServico);
		s.start();

		// -- canceladas
		if ("C".equals(dlp.getStatusNota().substring(0, 1))) {
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
		Guias g = new Guias();
		if (dlp.getNossoNumero() != null && !dlp.getNossoNumero().trim().isEmpty()) {
			g = guiasDao.findByNumeroGuia(dlp.getNossoNumero());
			if (g != null) {
				NotasThreadService nfGuias = new NotasThreadService(p, nf, dlp, log, linha, "G", g);
				Thread gThread = new Thread(nfGuias);
				gThread.start();

			} else {
				System.out.println("Numero de guia nÃ£o encontrado: " + dlp.getNossoNumero());

			}
		}

		// notas fiscais tomadores

		if (t != null && t.getId() != null) {
			NotasThreadService nfTomadores = new NotasThreadService(p, nf, dlp, log, linha, "T", g, t);
			Thread nftThread = new Thread(nfTomadores);
			nftThread.start();
		}

	}

}
