package cmm.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cmm.dao.CnaeDao;
import cmm.dao.DadosLivroPrestadorDao;
import cmm.dao.GuiasDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.ListaServicosDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.NotasFiscaisCanceladasDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.NotasFiscaisEmailsDao;
import cmm.dao.NotasFiscaisPrestadoresDao;
import cmm.dao.NotasFiscaisServicosDao;
import cmm.dao.NotasFiscaisTomadoresDao;
import cmm.dao.PagamentosDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.ServicosDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.entidadesOrigem.Servicos;
import cmm.model.Cnae;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;
import cmm.model.ListaServicos;
import cmm.model.NotasFiscais;
import cmm.model.NotasFiscaisCanceladas;
import cmm.model.NotasFiscaisEmails;
import cmm.model.NotasFiscaisPrestadores;
import cmm.model.NotasFiscaisServicos;
import cmm.model.NotasFiscaisTomadores;
import cmm.model.Pessoa;
import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
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
	private NotasFiscaisTomadoresDao notasFiscaisTomadoresDao = new NotasFiscaisTomadoresDao();
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	
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
		if (util.isEmptyOrNull(nf.getInscricaoTomador())) {
			nf.setInscricaoTomador(util.CPF_TOMADOR_FICTICIO);
			nf.setNomeTomador(dlp.getRazaoSocialTomador());
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
		nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().substring(0, 1));
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
		if (nf.getValorTotalServico()!=null && nf.getValorTotalServico().doubleValue()<0){
			nf.setValorTotalServico(BigDecimal.valueOf(nf.getValorTotalServico().doubleValue()*-1));
		}
		nf.setValorTotalIss(BigDecimal.valueOf(dlp.getValorIss()));
		nf.setSituacaoOriginal(dlp.getStatusNota().trim().substring(0, 1));
		nf.setSituacaoTributaria(util.getSituacaoTributaria(dlp));
		
		Guias g = null;
		if (dlp.getNossoNumero() != null && !dlp.getNossoNumero().trim().isEmpty()) {
			g = guiasDao.findByNumeroGuia(dlp.getNossoNumero());
		}/*
		// Não gerar guia para notas retidas - Passado por Sandro por email e telefone 30/09
		if (nf.getSituacaoTributaria().equals("R")){
			/*log.fillError(linha,
					"Guia com status de retenção não gravada de acordo com definição da cmm. " + "contribuinte:"
							+ nf.getNomePrestador() + " - " + p.getInscricaoPrestador() + " nota:" + nf.getNumeroNota() + " status:"
							+ nf.getSituacaoTributaria());
			if (g != null && g.getId() != null) {
				pagamentosDao.deleteByGuia(g);
				guiasDao.delete(g);
				g = null;
			}
		}*/
		
		if (g != null && g.getId() != null && !nf.getSituacaoOriginal().equals("C")) {
			nf.setSituacao("E");
		} else {
			nf.setSituacao("N");
		}

		if (dlp.getCodigoVerificacao() != null && !dlp.getCodigoVerificacao().trim().isEmpty()) {
			if (dlp.getCodigoVerificacao().length() > 9) {
				nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim().substring(0, 9));
			} else {
				nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim());
			}
		} else {
			nf.setNumeroVerificacao(util.completarZerosEsquerda(dlp.getIdCodigo().toString(), 9));
		}

		nf.setValorTotalBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
		nf.setValorTotalDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
		if ("EXTERIOR/EX".equals(dlp.getMunicipioTomador().trim())) {
			nf.setServicoPrestadoForaPais("S");
		} else {
			nf.setServicoPrestadoForaPais("N");
		}

		List<BigDecimal> lista = Arrays.asList(nf.getValorCofins(), nf.getValorCsll(), nf.getValorInss(),
				nf.getValorIr());
		BigDecimal descontos = util.getSumOfBigDecimal(lista);

		nf.setValorLiquido(util.getSubtract(BigDecimal.valueOf(dlp.getValorTotalNfse()), descontos));
		if (nf.getValorLiquido().compareTo(BigDecimal.ZERO) == -1) {
			nf.setValorLiquido(nf.getValorLiquido().multiply(BigDecimal.valueOf(-1)));
		}
		if (util.isEmptyOrNull(nf.getNomeTomador())) {
			nf.setNomeTomador("Não informado.");
		}
		else{
			nf.setNomeTomador(nf.getNomeTomador().trim());
		}
		try {
			nf = notasFiscaisDao.save(nf);
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal ", e);

			try {
				NotasFiscais outraNf = notasFiscaisDao.findNotaExistente(nf);
				if (outraNf != null) {
					nf.setId(outraNf.getId());
					String msg = "Nota duplicada-> numero:" + nf.getNumeroNota() + " prestador:"
							+ nf.getInscricaoPrestador() + "\n";
					msg += "Nota duplicada-> nf nova valor:" + nf.getValorTotalServico().doubleValue()
							+ " nf banco valor:" + outraNf.getValorTotalServico().doubleValue();
					log.fillError(linha, msg);
				}
			} catch (Exception e1) {
			}
			return;
		}

		// tomadores
		Tomadores t = null;

		if (!util.isEmptyOrNull(nf.getInscricaoTomador())) {

			t = tomadoresDao.findByInscricao(nf.getInscricaoTomador(), nf.getInscricaoPrestador());
			if (t == null || t.getId() == null) {
				try {
					t = new Tomadores();
					t.setOptanteSimples(util.getOptantePeloSimplesNacional("N"));
					t.setNome(nf.getNomeTomador());
					t.setNomeFantasia(nf.getNomeTomador());
					t.setPrestadores(nf.getPrestadores());
					t.setTipoPessoa(util.getTipoPessoa(inscricaoTomador));
					t.setInscricaoTomador(nf.getInscricaoTomador());
					t.setBairro(util.getNullIfEmpty(dlp.getEnderecoBairroTomador()));
					t.setCep(util.trataCep(dlp.getCepTomador()));
					t.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoTomador()));
					t.setEmail(util.trataEmail(dlp.getEmailTomador()));
					if (!util.isEmptyOrNull(t.getEmail()) && t.getEmail().length()>80){
						t.setEmail(t.getEmail().substring(0, 80));
					}
					t.setEndereco(util.getNullIfEmpty(dlp.getEnderecoTomador()));
					t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
					t.setInscricaoMunicipal(dlp.getInscricaoMunicipalTomador());
					t.setMunicipio(dlp.getMunicipioTomador());
					t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
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
					log.fillError(linha, "Nota Fiscal Tomadores ", e);
					t = null;
				}

			}
		}

		// -- serviÃ§os

		processaNotasFilhaServico(p, nf, dlp, log, linha, "S");

		// -- canceladas
		if ("C".equals(dlp.getStatusNota().substring(0, 1))) {
			processaNotasFilhaCancelada(p, nf, dlp, log, linha, "C");
		}

		// email
		if (dlp.getEmailPrestador() != null && !dlp.getEmailPrestador().isEmpty()) {
			processaNotasFilhaEmail(p, nf, dlp, log, linha, "E");
		}

		// notas-fiscais-cond-pagamentos ??

		// notas-fiscais-obras ??

		// notas-fiscais-prestadores
		processaNotasFilhaPrestadores(p, nf, dlp, log, linha, "P");

		// guias x notas fiscais

		if (nf.getSituacao().equals("E")) { // guia emitida
			processaNotasFilhaGuias(p, nf, dlp, log, linha, "G", g);

		} else {
			// log.fillError(linha, "Numero de guia nÃ£o encontrado: " +
			// dlp.getNossoNumero());

		}

		// notas fiscais tomadores

		if (t != null && t.getId() != null)

		{
			processaNotasFilhaTomadores(p, nf, dlp, log, linha, "T", g, t);
		}

	}

	private void processaNotasFilhaTomadores(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String string, Guias g, Tomadores t) {
		try {
			NotasFiscaisTomadores nft = new NotasFiscaisTomadores();
			nft.setBairro(dlp.getEnderecoBairroTomador());
			nft.setCelular(t.getCelular());
			nft.setCep(dlp.getCepTomador());
			nft.setComplemento(dlp.getEnderecoComplementoTomador());
			nft.setEmail(util.trataEmail(dlp.getEmailTomador()));
			if (!util.isEmptyOrNull(nft.getEmail()) && nft.getEmail().length() > 80) {
				nft.setEmail(nft.getEmail().substring(0, 80));
			}
			nft.setEndereco(dlp.getEnderecoBairroTomador());
			nft.setInscricaoEstadual(t.getInscricaoEstadual());
			nft.setInscricaoMunicipal(t.getInscricaoMunicipal());
			nft.setInscricaoPrestador(nf.getInscricaoPrestador());
			nft.setInscricaoTomador(t.getInscricaoTomador());
			nft.setMunicipio(t.getMunicipio());
			if (t.getMunicipioIbge() != null) {
				nft.setMunicipioIbge(Long.toString(t.getMunicipioIbge()));
			}
			nft.setNome(dlp.getRazaoSocialTomador());
			nft.setNomeFantasia(t.getNomeFantasia());
			if (nft.getNomeFantasia() == null) {
				nft.setNomeFantasia(t.getNome());
			}
			nft.setNotasFiscais(nf);
			nft.setNumero(dlp.getEnderecoNumeroTomador());
			nft.setNumeroNota(nf.getNumeroNota());
			nft.setOptanteSimples(t.getOptanteSimples());
			nft.setTipoPessoa(t.getTipoPessoa());
			notasFiscaisTomadoresDao.save(nft);
		} catch (Exception e) {

			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Tomadores ", e);
		}
	}

	private void processaNotasFilhaGuias(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String string, Guias guia) {
		try {
			Pessoa pessoa = new PessoaDao().findByCnpjCpf(p.getInscricaoPrestador());
			if (pessoa != null && pessoa.getOptanteSimples().equals("S")) {
				return;
			}
			GuiasNotasFiscais gnf = new GuiasNotasFiscais();
			gnf.setGuias(guia);
			gnf.setInscricaoPrestador(p.getInscricaoPrestador());
			// gnf.setNumeroGuia(g.getNumeroGuia());
			gnf.setNumeroGuia(guia.getNumeroGuia()); // acertar depois
			gnf.setNumeroNota(nf.getNumeroNota());
			gnf.setSituacaoTributaria(nf.getSituacaoTributaria());
			guiasNotasFiscaisDao.save(gnf);
		} catch (Exception e) {
			e.printStackTrace();
			String idGuia = "";
			String idNota = nf.getId()+"";
			if (guia!=null && guia.getId()!=null && guia.getId()>0){
				idGuia = guia.getId()+"";
			}
			log.fillError(linha, "Guia Nota Fiscal: "+idGuia+" - Nota: "+idNota+" - ", e);
		}

	}

	private void processaNotasFilhaPrestadores(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {
		try {
			NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
			nfp.setBairro(dlp.getEnderecoBairroPrestador());
			nfp.setTelefone(p.getTelefone());
			nfp.setCep(dlp.getCepPrestador());
			nfp.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoPrestador()));
			nfp.setEmail(p.getEmail());
			nfp.setEndereco(dlp.getEnderecoPrestador());
			nfp.setInscricaoPrestador(p.getInscricaoPrestador());
			nfp.setNome(dlp.getRazaoSocialPrestador());
			nfp.setNomeFantasia(dlp.getNomeFantasiaPrestador());
			nfp.setNotasFiscais(nf);
			nfp.setNumero(dlp.getEnderecoNumeroPrestador());
			nfp.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			nfp.setOptanteSimples(dlp.getOptantePeloSimplesNacional().substring(0, 1));
			nfp.setTipoPessoa(util.getTipoPessoa(p.getInscricaoPrestador()));
			notasFiscaisPrestadoresDao.save(nfp);

		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Prestador ", e);
		}

	}

	private void processaNotasFilhaEmail(Prestadores p, NotasFiscais nf2, DadosLivroPrestador dlp, FileLog log2,
			String linha2, String string) {
		try {
			NotasFiscaisEmails nfe = new NotasFiscaisEmails();
			nfe.setEmail(p.getEmail());
			nfe.setInscricaoPrestador(p.getInscricaoPrestador());
			nfe.setNotasFiscais(nf);
			nfe.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			notasFiscaisEmailsDao.save(nfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Email ", e);
		}

	}

	private void processaNotasFilhaCancelada(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {
		try {
			NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
			if (!util.isEmptyOrNull(dlp.getDataCancelamento())) {
				nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataCancelamento()));
				if (nfc.getDatahoracancelamento().getTime() < nf.getDataHoraEmissao().getTime()) {
					nfc.setDatahoracancelamento(nf.getDataHoraEmissao());
				}
			} else {
				nfc.setDatahoracancelamento(nf.getDataHoraEmissao());
			}

			nfc.setInscricaoPrestador(util.getCpfCnpj(dlp.getCnpjPrestador()));
			nfc.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			nfc.setMotivo(dlp.getMotivoCancelamento());
			if (util.isEmptyOrNull(nfc.getMotivo())) {
				nfc.setMotivo("Dados incorretos");
			}
			nfc.setNotasFiscais(nf);
			notasFiscaisCanceladasDao.save(nfc);

		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Cancelada ", e);
		}

	}

	private void processaNotasFilhaServico(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {

		try {

			NotasFiscaisServicos nfs = new NotasFiscaisServicos();
			nfs.setInscricaoPrestador(util.getCpfCnpj(dlp.getCnpjPrestador()));
			nfs.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));

			if (nf.getNaturezaOperacao().equals("1")) { // serviços prestados em lagoa
				nfs.setMunicipioIbge(util.CODIGO_IBGE);
			} else if (nf.getNaturezaOperacao().equals("2")) { // fora de lagoa
				try {
					nfs.setMunicipioIbge(
							municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador()));
					if (util.isEmptyOrNull(nfs.getMunicipioIbge())) {
						nfs.setMunicipioIbge(util.CODIGO_IBGE_XIQUE_XIQUE);
						//throw new Exception("Município Ibge não encontrado:" + dlp.getMunicipioTomador() + "-" + dlp.getUfTomador());
					}
				} catch (Exception e) {
					log.fillError("Erro: nota fiscal de serviço sem codigo ibge valido. " + dlp.getMunicipioTomador()
							+ "/" + dlp.getUfTomador() + " Conteúdo da linha: " + linha, "Nota Fiscal Serviço ", e);
					e.printStackTrace();
					nfs.setMunicipioIbge(util.CODIGO_IBGE_XIQUE_XIQUE);
				}
				if (util.CODIGO_IBGE.equals(nfs.getMunicipioIbge().trim())) {
					nfs.setMunicipioIbge(util.CODIGO_IBGE_XIQUE_XIQUE);
				}
					
			} else if (nf.getNaturezaOperacao().equals("3")) {
				nfs.setMunicipioIbge(util.CODIGO_IBGE);
			}

			PrestadoresAtividades pa = prestadoresAtividadesDao.findByInscricao(nfs.getInscricaoPrestador());

			nfs.setItemListaServico(util.completarZerosEsquerda(
					util.converteItemListaServico(dlp.getCodigoAtividadeMunipal()).replaceAll("\\.", ""), 4));

			if (nfs.getItemListaServico() == null) {
				if (pa == null || pa.getId() == null) {
					nfs.setItemListaServico(null);
				} else {
					nfs.setItemListaServico(util.completarZerosEsquerda(pa.getCodigoAtividade(), 4));
				}

			}
			String cnae = util.getStringLimpa(dlp.getCodigoCnae());

			if (!util.isEmptyOrNull(cnae)) {
				Cnae c = new CnaeDao().findByCodigo(cnae);
				if (c != null && !util.isEmptyOrNull(c.getDescricao())) {
					nfs.setDescricaoCnae(c.getDescricao());
					nfs.setIcnaes(c.getCnae());
				} else {
					nfs.setIcnaes(util.completarZerosDireita(cnae, 7));
				}
			}

			nfs.setDescricao(dlp.getDiscriminacaoServico());
			if (util.isEmptyOrNull(nfs.getDescricao().trim())) {
				nfs.setDescricao("Serviços Diversos");
			}

			// --

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
			ServicosDao dao = new ServicosDao();
			Servicos serv = dao.findByCodigoServicoCodigoCnae(nfs.getItemListaServico(), nfs.getIcnaes());
			if (serv == null || serv.getId() == 0) {
				Servicos s = new Servicos();
				s.setAliquota("" + nfs.getAliquota().doubleValue());
				s.setCnaes(nfs.getIcnaes());
				s.setCodigo(nfs.getItemListaServico());
				s.setNome(nfs.getDescricaoCnae());
				dao.save(s);
			}
		} catch (Exception e) {
			log.fillError(linha, "Nota Fiscal Serviço ", e);
			e.printStackTrace();
		}
	}

}
