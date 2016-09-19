package cmm.service;

import java.math.BigDecimal;

import cmm.dao.CompetenciasDao;
import cmm.dao.GuiasDao;
import cmm.dao.PagamentosDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresDao;
import cmm.entidadesOrigem.DadosGuia;
import cmm.model.Competencias;
import cmm.model.Guias;
import cmm.model.Pagamentos;
import cmm.model.Pessoa;
import cmm.model.Prestadores;
import cmm.util.FileLog;
import cmm.util.Util;

public class GuiaThread implements Runnable {

	private PessoaDao pessoaDao = new PessoaDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private CompetenciasDao competenciasDao = new CompetenciasDao();
	private GuiasDao guiasDao = new GuiasDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private String linha;
	private Util util;
	private FileLog log;

	public GuiaThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try {
			// linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("@@\\|");
			DadosGuia dg = new DadosGuia(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6],
					util.corrigeDouble(arrayLinha[7]), util.corrigeDouble(arrayLinha[8]), util.corrigeDouble(arrayLinha[9]), util.corrigeDouble(arrayLinha[10]),
					util.corrigeDouble(arrayLinha[11]), arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17],
					arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22], arrayLinha[23], arrayLinha[24], arrayLinha[25], arrayLinha[26],
					arrayLinha[27], arrayLinha[28], util.corrigeDouble(arrayLinha[29]), util.corrigeDouble(arrayLinha[30]), arrayLinha[31], arrayLinha[32],
					arrayLinha[33], arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39], arrayLinha[40], arrayLinha[41]);
			String descricao = util.getNomeMes(dg.getMes()) + "/" + dg.getAno();
			Competencias cp = competenciasDao.findByDescricao(descricao);

			try {
				if ("GUIA_PRESTADOR".equals(dg.getTipoGuia().trim())) {
					String inscricaoPrestador = util.getCpfCnpj(dg.getCnpj());
					Guias guias = new Guias();
					guias.setCompetencias(cp);
					guias.setDataVencimento(util.getStringToDateHoursMinutes(dg.getDataVencimento()));
					guias.setInscricaoPrestador(inscricaoPrestador);
					guias.setIntegrarGuia("N"); // TODO sanar dï¿½vida

					String numeroGuia = dg.getNossoNumero().substring(3);
					int proximoNumeroGuia = 60000000 + Integer.parseInt(numeroGuia);
					guias.setNumeroGuia(Long.valueOf(proximoNumeroGuia));
					guias.setNumeroGuiaOrigem(dg.getNossoNumero());

					Prestadores prestadores = prestadoresDao.findByInscricao(inscricaoPrestador);
					Pessoa pessoa = pessoaDao.findByCnpjCpf(inscricaoPrestador);
					if (pessoa != null && pessoa.getOptanteSimples().equals("S")) {
						//log.fillError(linha, "Guia não gerada. Prestador Simples. " +inscricaoPrestador);
						return;
					}
					if (prestadores == null) {
						log.fillError(linha, "Prestador nï¿½o encontrado:" + dg.getInscMunicipal());
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
							log.fillError(linha,"Pagamentos ", e);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha,"Guias ", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha,"Guias Thread ", e);
		}
	}

}
