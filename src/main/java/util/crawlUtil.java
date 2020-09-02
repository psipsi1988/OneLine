package util;

public class crawlUtil {
	
	public String byteCut(String s) {
		
		int size = 3990;
		try {
			byte[] bytes = s.getBytes("utf-8");
			if(bytes.length>size) {
				s = new String(bytes, 0, 3990);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return s;
	}
	
}
