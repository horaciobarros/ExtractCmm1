package cmm.service;

import java.math.BigDecimal;

import cmm.dao.PrestadoresAtividadesDao;
import cmm.dao.PrestadoresDao;
import cmm.entidadesOrigem.DadosCadastroAtividade;
import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
import cmm.util.FileLog;
import cmm.util.Util;

public class CadastroAtividadeThread implements Runnable {

	
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private String linha;
	private Util util;
	private FileLog log;

	public CadastroAtividadeThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try {
			// linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("@@\\|");
			DadosCadastroAtividade dca = new DadosCadastroAtividade(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
					util.corrigeDouble(arrayLinha[5]), arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12]);

			// atividade prestador{
			String inscricaoPrestador = util.getCpfCnpj(dca.getCnpj());
			Prestadores p = prestadoresDao.findByInscricao(inscricaoPrestador);
			if (p != null && p.getId() != 0) {
				try {
					PrestadoresAtividades pa = new PrestadoresAtividades();
					pa.setAliquota(BigDecimal.valueOf(dca.getAliquota()));
					// pa.setIcnaes(null);
					pa.setIlistaservicos(util.completarZerosEsquerda(dca.getGrupoAtividade(), 4));
					pa.setInscricaoPrestador(inscricaoPrestador);
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
			log.fillError(linha,"CadastroAtividade ", e);
		}
	}

}
