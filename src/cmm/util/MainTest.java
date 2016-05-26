package cmm.util;

import java.text.SimpleDateFormat;

import cmm.service.ExtractorService;

public class MainTest {

	public static void main(String args[]) {
		Util util = new Util();
		ExtractorService extractorService = new ExtractorService();
		
		String data = "2016-01-31";
		System.out.println(util.getDecimoDiaMesPosterior(util.getStringToDate(data)));
		
		
	}


	
}
