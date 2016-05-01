package cmm.service;

import java.math.BigDecimal;

import cmm.dao.GuiasDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.NotasFiscaisCanceladasDao;
import cmm.dao.NotasFiscaisDao;
import cmm.dao.NotasFiscaisEmailsDao;
import cmm.dao.NotasFiscaisPrestadoresDao;
import cmm.dao.NotasFiscaisServicosDao;
import cmm.dao.PagamentosDao;
import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresOptanteSimplesDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;
import cmm.model.NotasFiscais;
import cmm.model.NotasFiscaisCanceladas;
import cmm.model.NotasFiscaisEmails;
import cmm.model.NotasFiscaisPrestadores;
import cmm.model.NotasFiscaisServicos;
import cmm.model.Prestadores;
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
	private GuiasDao guiasDao = new GuiasDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();

	public NotasThreadService(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log, String linha,
			String tipoNotaFilha) {
		this.p = p;
		this.nf = nf;
		this.dlp = dlp;
		this.log = log;
		this.linha = linha;
		this.tipoNotaFilha = tipoNotaFilha;

	}
	
	public NotasThreadService(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log, String linha,
			String tipoNotaFilha, Guias guia) {
		this.p = p;
		this.nf = nf;
		this.dlp = dlp;
		this.log = log;
		this.linha = linha;
		this.tipoNotaFilha = tipoNotaFilha;
		this.guia = guia;

	}


	
	@Override
	public void run() {
		if (tipoNotaFilha.equals('S')) { // serviços
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
		}

		if (tipoNotaFilha.equals("C")) { // canceladas
			try {
				NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
				nfc.setDatahoracancelamento(util.getStringToDateHoursMinutes(dlp.getDataCancelamento()));
				nfc.setInscricaoPrestador(dlp.getCnpjPrestador());
				nfc.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
				nfc.setMotivo(dlp.getMotivoCancelamento());
				nfc.setNotasFiscais(nf);
				notasFiscaisCanceladasDao.save(nfc);

			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}
		}

		if (tipoNotaFilha.equals("E")) { // canceladas
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

		if (tipoNotaFilha.equals("P")) { // canceladas
			try {
				NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
				nfp.setBairro(dlp.getEnderecoBairroPrestador());
				nfp.setCelular(dlp.getTelefonePrestador());
				nfp.setCep(dlp.getCepPrestador());
				nfp.setComplemento(dlp.getEnderecoComplementoPrestador());
				nfp.setEmail(dlp.getEmailPrestador());
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
		}

		if (tipoNotaFilha.equals("G")) { // canceladas
			try {
				GuiasNotasFiscais gnf = new GuiasNotasFiscais();
				gnf.setGuias(guia);
				gnf.setInscricaoPrestador(p.getInscricaoPrestador());
				// gnf.setNumeroGuia(g.getNumeroGuia());
				gnf.setNumeroGuia(guia.getId()); // acertar depois
				gnf.setNumeroNota(nf.getNumeroNota());
				guiasNotasFiscaisDao.save(gnf);
			} catch (Exception e) {
				log.fillError(linha, e);
				e.printStackTrace();
			}
		}

	}

}