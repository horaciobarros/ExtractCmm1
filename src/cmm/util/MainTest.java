package cmm.util;

import java.text.SimpleDateFormat;

import cmm.service.ExtractorService;

public class MainTest {

	public static void main(String args[]) {
		
		String teste = "(31)98706-80-62";
		
		teste = teste.replaceAll("\\(", "");
		teste = teste.replaceAll("\\)", "");
		teste = teste.replaceAll("-", "");
		System.out.println(teste);
		
		
	}


	
}
