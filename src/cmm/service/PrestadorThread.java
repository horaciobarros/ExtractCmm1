package cmm.service;

import cmm.dao.DadosLivroPrestadorDao;
import cmm.dao.PrestadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.model.Prestadores;
import cmm.util.FileLog;
import cmm.util.Util;

public class PrestadorThread implements Runnable {

	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private DadosLivroPrestadorDao dadosLivroPrestadorDao = new DadosLivroPrestadorDao();
	private String linha;
	private Util util;
	private FileLog log;

	public PrestadorThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try {

			// linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("@@\\|");
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

			String inscricaoPrestador = util.getCpfCnpj(dlp.getCnpjPrestador());
			Prestadores pr = prestadoresDao.findByInscricao(inscricaoPrestador);
			try {
				if (pr == null || pr.getId() == 0 || pr.getId() == null) {
					// na hora de processar dados_cadastro estas
					// informa��es
					// tem que ser verificadas
					pr = new Prestadores();
					pr.setAutorizado("N");
					dlp.setTelefonePrestador(util.getLimpaTelefone(dlp.getTelefonePrestador()));
					pr.setEmail(util.trataEmail(dlp.getEmailPrestador()));
					pr.setEnquadramento("N");
					pr.setInscricaoPrestador(inscricaoPrestador);
					pr.setTelefone(util.getLimpaTelefone(dlp.getTelefonePrestador()));
					pr = util.trataNumerosTelefones(pr);
					pr = util.anulaCamposVazios(pr);
					pr.setInscricaoMunicipal(dlp.getInscricaoMunicipalPrestador());
					pr = prestadoresDao.save(pr);
				} else { // preencher campos vazios
					if (util.isEmptyOrNull(pr.getTelefone())) {
						pr.setTelefone(util.getLimpaTelefone(dlp.getTelefonePrestador()));
					}
					String email = util.trataEmail(pr.getEmail());
					if (Util.validarEmail(email)){
						pr.setEmail(email);
					}
					else{
						if (Util.validarEmail(pr.getEmail())){
							pr.setEmail(dlp.getEmailPrestador());
						}
					}

					pr = util.trataNumerosTelefones(pr);
					pr = util.anulaCamposVazios(pr);
					pr = prestadoresDao.update(pr);

				}

			} catch (Exception e) {
				log.fillError(linha,"Prestadores ", e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			log.fillError(linha,"Prestadores ", e);
			e.printStackTrace();
		}
	}

}
