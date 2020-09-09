package com.oneline.psi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneline.psi.HomeController;
import com.oneline.psi.service.BoardService;
import com.oneline.psi.service.impl.BoardServiceImpl;
import com.tobesoft.platform.data.DatasetList;
import com.tobesoft.platform.data.VariableList;


import  java.util.*; 
import  java.sql.*;
import  com.tobesoft.platform.*; 
import com.tobesoft.platform.data.*;
import  java.io.*;






import util.EnvFileReader;
import util.PagingUtil;

@Controller
public class BoardController {

	@Autowired
	SqlSession sqlSession;

	@Resource(name = "service")
	private BoardService boardService;

	//게시판 리스트
	@RequestMapping("list")
	public String list(@RequestParam Map<String, Object> map, Model model, HttpServletRequest req) {

		System.out.println("기본 submit으로 들어옴");
		// 검색어 불러오기
		String searchField = (String) map.get("searchField");
		String searchTxt = (String) map.get("searchTxt");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");

		System.out.println("--[검색테이블:" + searchField + "]--[검색어:" + searchTxt + "]--");
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchTxt", searchTxt);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);

		// 전체 게시물 세기
		int totalRecordCount = boardService.totalRecordCount(map);
		System.out.println("게시글 총 개수:" + totalRecordCount);

		// 페이지 처리 설정값
		int pageSize = Integer.parseInt(EnvFileReader.getValue("Inits.properties", "list.pageSize"));
		int blockPage = Integer.parseInt(EnvFileReader.getValue("Inits.properties", "list.blockPage"));
		System.out.println("--[페이지사이즈:" + pageSize + "]--[블록페이지:" + blockPage + "]--");

		// 전체 페이지 수 계산
		int totalPage = (int) Math.ceil((double) totalRecordCount / pageSize);
		System.out.println("전체 페이지 수:" + totalPage);

		// 현재페이지에 대한 파라미터 처리 및 시작/끝의 rownum구하기
		int nowPage = map.get("nowPage") == null ? 1 : Integer.parseInt((String) map.get("nowPage"));
		int start = (nowPage - 1) * pageSize + 1;
		int end = nowPage * pageSize;
		map.put("start", start);
		map.put("end", end);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = boardService.list(map);

		System.out.println("전체" + map);

//		for(Map<String, Object> map1 : list) {
//			String temp = map1.boardContent().replace("\r\n", "<br>");//줄바꿈처리
//			map1.put("boardContent");
//		}
//		
		int virtualNum = 0;
		int countNum = 0;

		for (Map<String, Object> map1 : list) {
			virtualNum = totalRecordCount - (((nowPage - 1) * pageSize) + countNum++);
			map1.put("virtualNum", virtualNum);
		}
		System.out.println("virtualNum:" + virtualNum + "countNum:" + countNum);

		// 검색어가 있을 때 다음 페이지에 가도 검색어 파라미터가 사라지지 않는다
		String pagingImg = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/list?" + "&searchField=" + searchField + "&searchTxt=" + searchTxt
						+ "&startDate=" + startDate + "&endDate=" + endDate + "&");

		// 검색어 없을때의 페이징 처리
		String pagingImg2 = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/list?");

		model.addAttribute("list", list);
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("pagingImg", pagingImg);
		model.addAttribute("pagingImg2", pagingImg2);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchTxt", searchTxt);
		System.out.println(list);
		return "board/list";
	}

	public Map<String, Object> pageMap(int curPage, int count, int listSize){
		int BLOCK_SCALE = 5;
		int totPage = (int) Math.ceil(count*1.0 / listSize);
		int totBlock = (int)Math.ceil(totPage / BLOCK_SCALE);
		
		// *현재 페이지가 몇번째 페이지 블록에 속하는지 계산
        int curBlock = (int)Math.ceil((curPage-1) / BLOCK_SCALE)+1;
        
        // *현재 페이지 블록의 시작, 끝 번호 계산
        int blockBegin = (curBlock-1)*BLOCK_SCALE+1;
        
        // 페이지 블록의 끝번호
        int blockEnd = blockBegin+BLOCK_SCALE-1;
        
        // *마지막 블록이 범위를 초과하지 않도록 계산
        if(blockEnd > totPage) blockEnd = totPage;
        
        // *이전을 눌렀을 때 이동할 페이지 번호
        int prevPage = (curPage == 1)? 1:(curBlock-1)*BLOCK_SCALE;
 
        // *다음을 눌렀을 때 이동할 페이지 번호
        int nextPage = curBlock > totBlock ? (curBlock*BLOCK_SCALE) : (curBlock*BLOCK_SCALE)+1;
        
        // 마지막 페이지가 범위를 초과하지 않도록 처리
        if(nextPage >= totPage) nextPage = totPage;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totBlock", totBlock);
        map.put("curBlock", curBlock);
        map.put("blockBegin", blockBegin);
        map.put("blockEnd", blockEnd);
        map.put("prevPage", prevPage);
        map.put("nextPage", nextPage);
        map.put("curPage", curPage);
        map.put("totPage", totPage);
        map.put("BLOCK_SCALE", BLOCK_SCALE);
        
        
        return map;
	}

	// 글쓰기 버튼 눌렀을 때
	@RequestMapping("write")
	public String write() {
		return "board/write";
	}

	// 게시판에서 제목 클릭했을 때 상세보기로 이동
	@RequestMapping("detail")
	public String detail(@RequestParam int seq, Model model) {
		System.out.println("상세보기");
		Map<String, Object> detailMap = boardService.detail(seq);
		model.addAttribute("detail", detailMap);

		int viewCnt = boardService.viewCnt(seq);

		return "board/write";
	}

	// 글쓰기
	@RequestMapping("writeAction.do")
	public String writeAction(@RequestParam Map<String, Object> map) {
		System.out.println("글쓰기 등록 버튼 누름");
		int insert = boardService.writeAction(map);

		System.out.println(insert);
		System.out.println(map);
		// redirect url을 찾아간다.
		return "redirect:list";
	}

	// 글 수정하기
	@RequestMapping("editAction.do")
	public String editAction(@RequestParam Map<String, Object> map) {
		System.out.println("글쓰기 수정 버튼 누름");
		int update = boardService.editAction(map);

		System.out.println(update);
		System.out.println(map);
		// redirect url을 찾아간다.
		return "redirect:list";
	}


	// 글 삭제하기

	@RequestMapping("delete.do")
	@ResponseBody
	public String delete(@RequestParam Map<String, Object> map) throws Exception {

		System.out.println("글 삭제 버튼 누름");
		int result = 1;
		int delete;
		System.out.println("result값:" + result);
		int cnt = Integer.parseInt((String) map.get("cnt"));
		String arr = (String) map.get("arr");
		System.out.println(cnt + "개 선택, seq 번호:" + arr);
		try {
			String[] strArray = arr.split(",");
			for (int i = 0; i < cnt; i++) {
				int temp = Integer.parseInt((String) strArray[i]);
				map.put("seq", temp);
				delete = boardService.delete(map);
				System.out.println("delete 값:" + delete);
			}
		} catch (Exception e) {
			System.out.println("오류");
			result = 0;
		}
		System.out.println("result 값:" + result);
		return Integer.toString(result);
	}

	// 삭제 두 번째
	@RequestMapping("delete2.do")
	public String delete2(Integer[] checkbox) {
		System.out.println("삭제 두 번째");
		List<Integer> list = Arrays.asList(checkbox);
		System.out.println(list);

		int delete = boardService.delete2(list);
//		int update = boardService.editAction(map);
		// redirect url을 찾아간다.
		return "redirect:list";
	}

	//////////////////////////////////////////////////////////////////
	@RequestMapping("list2")
	public String list2(@RequestParam Map<String, Object> map, Model model, HttpServletRequest req) {

		// 검색어 불러오기
		System.out.println("ajax로 들어옴");
		String searchField = (String) map.get("searchField");
		String searchTxt = (String) map.get("searchTxt");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");

		System.out.println("--[검색테이블:" + searchField + "]--[검색어:" + searchTxt + "]--");
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchTxt", searchTxt);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);

		// 전체 게시물 세기
		int totalRecordCount = boardService.totalRecordCount(map);
		System.out.println("게시글 총 개수:" + totalRecordCount);

		// 페이지 처리 설정값
		int pageSize = Integer.parseInt(EnvFileReader.getValue("Inits.properties", "list.pageSize"));
		int blockPage = Integer.parseInt(EnvFileReader.getValue("Inits.properties", "list.blockPage"));
		System.out.println("--[페이지사이즈:" + pageSize + "]--[블록페이지:" + blockPage + "]--");

		// 전체 페이지 수 계산
		int totalPage = (int) Math.ceil((double) totalRecordCount / pageSize);
		System.out.println("전체 페이지 수:" + totalPage);

		// 현재페이지에 대한 파라미터 처리 및 시작/끝의 rownum구하기
		int nowPage = map.get("nowPage") == null ? 1 : Integer.parseInt((String) map.get("nowPage"));
		int start = (nowPage - 1) * pageSize + 1;
		int end = nowPage * pageSize;
		map.put("start", start);
		map.put("end", end);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = boardService.list(map);

		System.out.println("전체" + map);

//		for(Map<String, Object> map1 : list) {
//			String temp = map1.boardContent().replace("\r\n", "<br>");//줄바꿈처리
//			map1.put("boardContent");
//		}
//		
		int virtualNum = 0;
		int countNum = 0;

		for (Map<String, Object> map1 : list) {
			virtualNum = totalRecordCount - (((nowPage - 1) * pageSize) + countNum++);
			map1.put("virtualNum", virtualNum);
		}
		System.out.println("virtualNum:" + virtualNum + "countNum:" + countNum);

		// 검색어가 있을 때 다음 페이지에 가도 검색어 파라미터가 사라지지 않는다
		String pagingImg = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/list?" + "&searchField=" + searchField + "&searchTxt=" + searchTxt
						+ "&startDate=" + startDate + "&endDate=" + endDate + "&");

		// 검색어 없을때의 페이징 처리
		String pagingImg2 = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/list?");

		model.addAttribute("list", list);
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("pagingImg", pagingImg);
		model.addAttribute("pagingImg2", pagingImg2);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchTxt", searchTxt);
		System.out.println("아아아아아ajax list:"+list);
		return "board/searchList";
	}

	//엑셀 다운로드
	@RequestMapping("excelDown")
	public String excelDown() {
		
	
		return "excelList";
	}
	
	
	
	
	
	
	
	
	
	
	
	



	
	
	//miConnector
	@RequestMapping("miConnector")
	public void miConnector(@RequestParam Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println("마이플랫폼 들어옴");
		
		/****** Service API 초기화 ******/
		VariableList vl = new VariableList();
		DatasetList  dl = new DatasetList();
		
		/** Input 부분 초기화 **/
		VariableList in_vl = new VariableList();
		DatasetList  in_dl = new DatasetList();
		PlatformRequest pReq = new PlatformRequest(request, "utf-8");
		
		/** Web Server에서 XML수신 및 Parsing **/
		pReq.receiveData();
		
		
		
		////////////////////////////////////////////////
		/** List 획득 및 Dataset, 변수 획득 **/
		in_vl = pReq.getVariableList();
		in_dl = pReq.getDatasetList();
		Dataset in_ds = in_dl.getDataset("search");
		

//		String searchTxt = request.getParameter("searchTxt");
//		String searchField = request.getParameter("searchField");
//		
//		System.out.println("searchTxt:"+searchTxt);
//		
//		String searchTxt1 = new String(searchTxt.getBytes("8859_1"), "UTF-8"); 
//		String searchTxt2 = new String(searchTxt.getBytes("EUC-KR"), "UTF-8"); 
//		String searchTxt3 = new String(searchTxt.getBytes("MS949"), "UTF-8"); 
//		System.out.println("searchTxt1:"+searchTxt1);
//		System.out.println("searchTxt2:"+searchTxt2);
//		System.out.println("searchTxt3:"+searchTxt3);
//		System.out.println("searchField:"+searchField);
		
		
//		System.out.println("**********************");
//		System.out.println("**********"+in_ds+"**********");
//		
		String searchField = in_vl.getValueAsString("searchField");
		String searchTxt = in_vl.getValueAsString("searchTxt");
		String startDate = in_vl.getValueAsString("startDate");
		String endDate = in_vl.getValueAsString("endDate");
		if(searchField.equals("null")){
			searchField="";
		}
		if(searchTxt.equals("null")){
			searchTxt="";
		}
		if(startDate.equals("null")){
			startDate="";
		}
		if(endDate.equals("null")){
			endDate="";
		}
		
		System.out.println("===searchField:"+searchField);
		System.out.println("===searchTxt:"+searchTxt);
		System.out.println("===startDate:"+startDate);
		System.out.println("===endDate:"+endDate);
		map.put("searchField", searchField);
		map.put("searchTxt", searchTxt);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		///////////////////////////////////////
		
		System.out.println("map:"+map);
		List<Map<String, Object>> milist = new ArrayList<Map<String, Object>>();
		milist = boardService.milist(map);
		System.out.println(milist);
		
		
		/********* Dataset 생성 ************/
		Dataset ds = new Dataset("javaList");
		ds.addColumn("seq",ColumnInfo.COLTYPE_INT, 100);
		ds.addColumn("memId",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("memName",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("boardSubject",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("boardContent",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("regDate",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("uptDate",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("viewCnt",ColumnInfo.COLTYPE_STRING, 100);
		
		
		for (int i= 0; i < milist.size(); i++) {
			int row = ds.appendRow();
			int seq = Integer.parseInt(milist.get(i).get("seq").toString());
			ds.setColumn(row, "seq", seq);
			ds.setColumn(row, "memId", milist.get(i).get("memId").toString());
			ds.setColumn(row, "memName", milist.get(i).get("memName").toString());
			ds.setColumn(row, "boardSubject", milist.get(i).get("boardSubject").toString());
			ds.setColumn(row, "boardContent", milist.get(i).get("boardContent").toString());
			ds.setColumn(row, "regDate", milist.get(i).get("regDate").toString());

			String uptDate = milist.get(i).get("uptDate") == null ? "" : milist.get(i).get("uptDate").toString();
			
			ds.setColumn(row, "uptDate", uptDate);
			ds.setColumn(row, "viewCnt", milist.get(i).get("viewCnt").toString());
		}

		
		/********* 생성된 DATASET을 DATASETLIST에 추가 ************/
		dl.addDataset(ds);

//		
//		out.clearBuffer();
		PlatformResponse pRes = new PlatformResponse(response, PlatformRequest.JSP_XML, "utf-8");
		pRes.sendData(vl, dl);
//		

//		return "miConnector";
	}
	//miConnector
	@RequestMapping("miConnectorAll")
	public void miConnectorAll(@RequestParam Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println("마이플랫폼 전체 들어옴");
		
		/****** Service API 초기화 ******/
		VariableList vl = new VariableList();
		DatasetList  dl = new DatasetList();
		
		/** Input 부분 초기화 **/
		VariableList in_vl = new VariableList();
		DatasetList  in_dl = new DatasetList();
		PlatformRequest pReq = new PlatformRequest(request, "utf-8");
		
		/** Web Server에서 XML수신 및 Parsing **/
		pReq.receiveData();
		
		
		System.out.println("map:"+map);
		List<Map<String, Object>> milist = new ArrayList<Map<String, Object>>();
		milist = boardService.milist(map);
		System.out.println(milist);
		
		
		/********* Dataset 생성 ************/
		Dataset ds = new Dataset("javaList");
		ds.addColumn("seq",ColumnInfo.COLTYPE_INT, 100);
		ds.addColumn("memId",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("memName",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("boardSubject",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("boardContent",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("regDate",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("uptDate",ColumnInfo.COLTYPE_STRING, 100);
		ds.addColumn("viewCnt",ColumnInfo.COLTYPE_STRING, 100);
		
		
		for (int i= 0; i < milist.size(); i++) {
			int row = ds.appendRow();
			int seq = Integer.parseInt(milist.get(i).get("seq").toString());
			ds.setColumn(row, "seq", seq);
			ds.setColumn(row, "memId", milist.get(i).get("memId").toString());
			ds.setColumn(row, "memName", milist.get(i).get("memName").toString());
			ds.setColumn(row, "boardSubject", milist.get(i).get("boardSubject").toString());
			ds.setColumn(row, "boardContent", milist.get(i).get("boardContent").toString());
			ds.setColumn(row, "regDate", milist.get(i).get("regDate").toString());
			
			String uptDate = milist.get(i).get("uptDate") == null ? "" : milist.get(i).get("uptDate").toString();
			
			ds.setColumn(row, "uptDate", uptDate);
			ds.setColumn(row, "viewCnt", milist.get(i).get("viewCnt").toString());
		}
		
		
		/********* 생성된 DATASET을 DATASETLIST에 추가 ************/
		dl.addDataset(ds);
		
//		
//		out.clearBuffer();
		PlatformResponse pRes = new PlatformResponse(response, PlatformRequest.JSP_XML, "utf-8");
		pRes.sendData(vl, dl);
//		
		
//		return "miConnector";
	}
	
	
	//마이플랫폼에서 글 수정하기
	@RequestMapping("miEditAction.do")
	public void miEditAction(@RequestParam Map<String, Object> map, Model model, HttpServletResponse response) throws IOException {	
		System.out.println("miplatform에서 수정 버튼 누름");
//		int update = boardService.editAction(map);
//		
//		System.out.println(update);
//		System.out.println(map);
//		// redirect url을 찾아간다.
		
		
		
		
		
	
	}
	
}
