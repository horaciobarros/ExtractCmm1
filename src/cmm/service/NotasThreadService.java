package cmm.service;

import java.math.BigDecimal;

import cmm.dao.CnaeDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.NotasFiscaisCanceladasDao;
import cmm.dao.NotasFiscaisEmailsDao;
import cmm.dao.NotasFiscaisPrestadoresDao;
import cmm.dao.NotasFiscaisServicosDao;
import cmm.dao.NotasFiscaisTomadoresDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Cnae;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;
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

public class NotasThreadService implements Runnable {
	private Prestadores p;
	private NotasFiscais nf;
	private DadosLivroPrestador dlp;
	private FileLog log;
	private String linha;
	private String tipoNotaFilha;
	private Guias guia;
	private Util util = new Util();
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	private Tomadores tomadores;
	private NotasFiscaisTomadoresDao notasFiscaisTomadoresDao = new NotasFiscaisTomadoresDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public NotasThreadService(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log, String linha, String tipoNotaFilha) {
		this.p = p;
		this.nf = nf;
		this.dlp = dlp;
		this.log = log;
		this.linha = linha;
		this.tipoNotaFilha = tipoNotaFilha;

	}

	public NotasThreadService(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log, String linha, String tipoNotaFilha, Guias guia) {
		this.p = p;
		this.nf = nf;
		this.dlp = dlp;
		this.log = log;
		this.linha = linha;
		this.tipoNotaFilha = tipoNotaFilha;
		this.guia = guia;

	}

	public NotasThreadService(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log, String linha, String tipoNotaFilha, Guias guia,
			Tomadores tomadores) {
		this.p = p;
		this.nf = nf;
		this.dlp = dlp;
		this.log = log;
		this.linha = linha;
		this.tipoNotaFilha = tipoNotaFilha;
		this.guia = guia;
		this.tomadores = tomadores;

	}

	@Override
	public void run() {
		if (("S").equals(tipoNotaFilha)) { // serviÃ§os
			try {

				NotasFiscaisServicos nfs = new NotasFiscaisServicos();
				nfs.setInscricaoPrestador(util.getCpfCnpj(dlp.getCnpjPrestador()));
				nfs.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));

				if (nf.getNaturezaOperacao().equals("1")) {
					nfs.setMunicipioIbge(util.CODIGO_IBGE);
				} else if (nf.getNaturezaOperacao().equals("2")) {
					try {
						nfs.setMunicipioIbge(municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador()));
						if (util.isEmptyOrNull(nfs.getMunicipioIbge())) {
							throw new Exception();
						}
					} catch (Exception e) {
						log.fillError("Erro: nota fiscal de serviço sem codigo ibge valido. Conteúdo da linha: " + linha,"Nota Fiscal Serviço ", e);
						e.printStackTrace();
					}
				} else if (nf.getNaturezaOperacao().equals("3")) {
					nfs.setMunicipioIbge(util.CODIGO_IBGE);
				}

				PrestadoresAtividades pa = prestadoresAtividadesDao.findByInscricao(nfs.getInscricaoPrestador());
				nfs.setItemListaServico(util.completarZerosEsquerda(dlp.getCodigoAtividadeMunipal().replaceAll("\\.", ""), 4));

				if (nfs.getItemListaServico() == null) {
					if (pa == null || pa.getId() == null) {
						nfs.setItemListaServico(null);
					} else {
						nfs.setItemListaServico(util.completarZerosEsquerda(pa.getCodigoAtividade(), 4));
					}
				}
				String cnae = util.getStringLimpa(dlp.getCodigoCnae());
				if (cnae != null) {
					nfs.setIcnaes(cnae);
				}
				Cnae c = new CnaeDao().findByCodigo(cnae);
				if (c != null && !util.isEmptyOrNull(c.getDescricao())) {
					nfs.setDescricaoCnae(c.getDescricao());
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
			} catch (Exception e) {
				log.fillError(linha,"Nota Fiscal Serviço ", e);
				e.printStackTrace();
			}
			return;
		}

		if ("C".equals(tipoNotaFilha)) { // canceladas
			try {
				NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
				nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataCancelamento()));
				if (nfc.getDatahoracancelamento().getTime() < nf.getDataHoraEmissao().getTime()) {
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
				log.fillError(linha,"Nota Fiscal Cancelada ", e);
			}
			return;
		}

		if ("E".equals(tipoNotaFilha)) { // email
			try {
				NotasFiscaisEmails nfe = new NotasFiscaisEmails();
				nfe.setEmail(p.getEmail());
				nfe.setInscricaoPrestador(p.getInscricaoPrestador());
				nfe.setNotasFiscais(nf);
				nfe.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				notasFiscaisEmailsDao.save(nfe);
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha,"Nota Fiscal Email ", e);
			}
			return;
		}

		if ("P".equals(tipoNotaFilha)) { // prestadores
			try {
				NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
				nfp.setBairro(dlp.getEnderecoBairroPrestador());
				nfp.setTelefone(p.getTelefone());
				nfp.setCep(dlp.getCepPrestador());
				nfp.setComplemento(dlp.getEnderecoComplementoPrestador());
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
				log.fillError(linha,"Nota Fiscal Prestador ", e);
			}
			return;
		}

		if ("G".equals(tipoNotaFilha)) { // guias
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
				guiasNotasFiscaisDao.save(gnf);
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha,"Guia Nota Fiscal ", e);
			}
			return;
		}

		if ("T".equals(tipoNotaFilha)) {
			try {
				NotasFiscaisTomadores nft = new NotasFiscaisTomadores();
				nft.setBairro(tomadores.getBairro());
				nft.setCelular(tomadores.getCelular());
				nft.setCep(tomadores.getCep());
				nft.setComplemento(tomadores.getComplemento());
				nft.setEmail(tomadores.getEmail());
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
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha,"Nota Fiscal Tomadores ", e);
			}
		}
	}
}
