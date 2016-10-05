package cmm.util;

import java.util.List;

import cmm.dao.DadosGuiaDao;
import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.TomadoresDao;
import cmm.entidadesOrigem.DadosGuia;
import cmm.service.ExtractorService;

public class MainTest {

	private PessoaDao pessoaDao = new PessoaDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private ExtractorService extractorService = new ExtractorService();
	private Util util = new Util();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public static void main(String args[]) {

		try {
			MainTest main = new MainTest();
			main.extractorService.processaAjustesEmServicos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
