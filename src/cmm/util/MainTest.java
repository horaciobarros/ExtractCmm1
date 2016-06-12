package cmm.util;

import java.text.SimpleDateFormat;

import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.TomadoresDao;
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
		
		String telefone = "031987068062";
		String telefone2 = telefone.substring(1);
		System.out.println(telefone + " | " + telefone2);
		

	}

	
	

}
