package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;




public class KakaoPay {
	
	public static void kakaoPay(String partner_order_id, 
			String partner_user_id
			) {
		
		try {
			URL url = new URL("https://kapi.kakao.com/v1/payment/ready");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "5496cfb0338e31dcda3553a1e680476e");
			conn.setRequestProperty("content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("cid", "TC0ONETIME");
			params.put("partner_user_id", partner_user_id);
			params.put("partner_order_id", partner_order_id);
			params.put("item_name", "item_name");
			params.put("total_amount", "total_amount");
			params.put("quantity", "quantity");
			params.put("tax_free_amount", "0");
			params.put("approval_url", "");
			params.put("cancel_url", "");
			params.put("fail_url", "");
			
			
			String string_params = new String();
			for (Map.Entry<String, String> elem : params.entrySet()) {
				string_params += (elem.getKey() + "=" + elem.getValue() + "&");
			}
			
			System.out.println(string_params);
			conn.getOutputStream().write(string_params.getBytes());
			System.out.println(conn);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String jsonData = in.readLine();
			
			ObjectMapper mapper = new ObjectMapper();
			
			Map<String, String> jsonMap = mapper.readValue(jsonData, Map.class);
			
			System.out.println(jsonMap.get("next_redirect_pc_url"));
			
					
			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	@SuppressWarnings("null")
	public static void main(String[] args) {
		

		kakaoPay("test", "testorder");
		
	}
	
}
