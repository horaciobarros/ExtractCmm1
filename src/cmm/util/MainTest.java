package cmm.util;

import java.text.SimpleDateFormat;

import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.service.ExtractorService;
import cmm.model.Pessoa;
import cmm.model.Tomadores;

import java.util.*;

public class MainTest {

	private PessoaDao pessoaDao = new PessoaDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private ExtractorService extractorService = new ExtractorService();
	private Util util = new Util();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public static void main(String args[]) {

		// notas fiscais
		MainTest m = new MainTest();
		List<String> dadosList = m.extractorService.lerArquivo("dados_livro_prestador");
		m.processaDadosNotasFiscaisAux(dadosList);

	}

	private void processaDadosNotasFiscaisAux(List<String> dadosList) {
	/*	FileLog log = new FileLog("dados_livro_prestador_notas_fiscais");

		int linhas = 0;

		for (String linha : dadosList) {
			extractorService.mostraProgresso(linhas, dadosList.size());

			linha = extractorService.preparaParaSplit(linha);
			String[] arrayLinha = linha.split("#");

			try {

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

			} catch (NumberFormatException e) {
				try {
					String aux = null;
					DadosLivroPrestador dlp = new DadosLivroPrestador(Long.valueOf(arrayLinha[0]), arrayLinha[1],
							arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6], arrayLinha[7],
							arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12],
							arrayLinha[13], arrayLinha[14], arrayLinha[15], arrayLinha[16], arrayLinha[17], aux,
							Double.valueOf(arrayLinha[18]), Double.valueOf(arrayLinha[19]),
							Double.valueOf(arrayLinha[20]), Double.valueOf(arrayLinha[21]),
							Double.valueOf(arrayLinha[22]), Double.valueOf(arrayLinha[23]),
							Double.valueOf(arrayLinha[24]), arrayLinha[25], Double.valueOf(arrayLinha[26]),
							Double.valueOf(arrayLinha[27]), Double.valueOf(arrayLinha[28]),
							Double.valueOf(arrayLinha[29]), Double.valueOf(arrayLinha[30]),
							Double.valueOf(arrayLinha[31]), Double.valueOf(arrayLinha[32]), arrayLinha[33],
							arrayLinha[34], arrayLinha[35], arrayLinha[36], arrayLinha[37], arrayLinha[38],
							arrayLinha[39], arrayLinha[40], arrayLinha[41], arrayLinha[42], arrayLinha[43],
							arrayLinha[44], arrayLinha[45], arrayLinha[46], arrayLinha[47], arrayLinha[48],
							arrayLinha[49], arrayLinha[50], arrayLinha[51], arrayLinha[52], arrayLinha[53],
							arrayLinha[54], arrayLinha[55], arrayLinha[56], arrayLinha[57], arrayLinha[58],
							arrayLinha[59], arrayLinha[60], arrayLinha[61], arrayLinha[61], arrayLinha[63]);

				} catch (Exception e2) {
					log.fillError(linha, e2);
					e2.printStackTrace();
				}

			}

		}
		log.close();
	 	*/
	}

}
