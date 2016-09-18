package cmm.util;

import java.util.List;

import cmm.dao.DadosLivroPrestadorDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.service.ExtractorService;

public class GravaDadosLivroPrestador {

	private ExtractorService extractorService = new ExtractorService();

	public static void main(String args[]) {

		GravaDadosLivroPrestador grava = new GravaDadosLivroPrestador();
		grava.gravaDadosLivroPrestador();

	}

	private void gravaDadosLivroPrestador() {
		System.out.println("Lendo dados livro prestador");
		List<String> dadosList = extractorService.lerArquivo("dados_livro_prestador");

		Util util = new Util();
		DadosLivroPrestadorDao dadosLivroPrestadorDao = new DadosLivroPrestadorDao();

		FileLog log = new FileLog("dados_livro_prestador_notas_fiscais");

		for (String linha : dadosList) {

			String[] arrayLinha = linha.split("@@\\|");

			try {

				DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4],
						arrayLinha[5], arrayLinha[6], arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13],
						arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], util.corrigeDouble(arrayLinha[18]), util.corrigeDouble(arrayLinha[19]),
						util.corrigeDouble(arrayLinha[20]), util.corrigeDouble(arrayLinha[21]), util.corrigeDouble(arrayLinha[22]),
						util.corrigeDouble(arrayLinha[23]), util.corrigeDouble(arrayLinha[24]), arrayLinha[25], util.corrigeDouble(arrayLinha[26]),
						util.corrigeDouble(arrayLinha[27]), util.corrigeDouble(arrayLinha[28]), util.corrigeDouble(arrayLinha[29]),
						util.corrigeDouble(arrayLinha[30]), util.corrigeDouble(arrayLinha[31]), util.corrigeDouble(arrayLinha[32]),
						util.corrigeDouble(arrayLinha[33]), arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38], arrayLinha[39],
						arrayLinha[40], arrayLinha[41], arrayLinha[42], arrayLinha[43], arrayLinha[44], arrayLinha[45], arrayLinha[46], arrayLinha[47],
						arrayLinha[48], arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52], arrayLinha[53], arrayLinha[54], arrayLinha[55],
						arrayLinha[56], arrayLinha[57], arrayLinha[58], arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[62], arrayLinha[63],
						arrayLinha[64]);

				if (!dadosLivroPrestadorDao.exists(dlp.getIdCodigo())) {
					dlp = dadosLivroPrestadorDao.save(dlp);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		log.close();

	}

}
