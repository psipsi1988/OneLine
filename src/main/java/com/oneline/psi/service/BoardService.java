package com.oneline.psi.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface BoardService {

	List<Map<String, Object>> list(Map<String, Object> map);

	int writeAction(Map<String, Object> map);

	Map<String, Object> detail(int seq);

	int editAction(Map<String, Object> map);

	int viewCnt(int seq);

	int delete(Map<String, Object> map);

	int delete2(List<Integer> list);

	int totalRecordCount(Map<String, Object> map);

	List<Map<String, Object>> milist(Map<String, Object> map);

	



	
}
