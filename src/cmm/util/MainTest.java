package cmm.util;

public class MainTest {

	public static void main(String args[]) {
		Util util = new Util();
		
		String data = "2016-01-20 00:00:00";
		System.out.println(util.getStringToDateHoursMinutes(data));
		
	}


	
}
