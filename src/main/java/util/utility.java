package util;

import java.sql.ResultSet;

import com.tobesoft.platform.data.Dataset;



public class utility {

	/*********** 공통함수 *************/

	// ResultSet 값 가져오기, 단, "null"을 ""로

	public String rsGet(ResultSet rs, String id) throws Exception

	{

		if( rs.getString(id) == null )

			return "";

		else

			return rs.getString(id);

	} 



	/*********** 공통함수 *************/

	// DataSet 값 가져오기, 단, "null"을 ""로

	public String  dsGet(Dataset ds, int rowno, String id) throws Exception

	{

		String value;

		value = ds.getColumn(rowno,id).getString();

		if( value == null )

			return "";

		else

			return value;

	}



	// 한글 조회조건 처리용 함수

	public static String EucToUni(String s) 

	{

		String result = "";



		try {

			result = new String(s.getBytes("8859_1"), "EUC-KR");

		} catch(Exception e) {

			System.out.println(e);

		}



		return result;

	}



}
