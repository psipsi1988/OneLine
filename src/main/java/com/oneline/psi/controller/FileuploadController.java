package com.oneline.psi.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileuploadController {
	
	//서버의 물리적 경로 확인하기
	@RequestMapping("/uploadPath.do")
	public void uploadPath(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	
		
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		
		resp.setContentType("text/html); charset=utf-8");
		PrintWriter pw = resp.getWriter();
		pw.print("/upload 디렉토리의 물리적 경로 : ");
		pw.print(path);
		
		
		
	}

	/*
	UUID(Universally Unique Identifier)
		: 범용 고유 식별자. randomUUID()메소드를 통해 문자열을 
		생성하면 하이픈이 4개 포함된 32자의 랜덤하고 유니크한 문자열이 생성된다. 
		※JDK에서 기본적으로 제공하는 클래스임. 
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("생성된UUID-1:"+uuid);
		uuid = uuid.replace("-", "");
		System.out.println("생성된UUID-2:"+uuid);
		return uuid;
	}
	
	
	
	
	
	
	//파일 업로드 처리
	/*
	파일업로드는 반드시 POST방식으로 처리해야 하므로 컨트롤러에 
	매핑시 method.value 두 가지 속성을 명시해야 한다. 
	 */
	
	/* private static final String filePath = "c:/Users/gs/Desktop/upload/"; 마지막에 꼭 / 닫아주기*/
	@RequestMapping(value="uploadAction", method=RequestMethod.POST)
	public String uploadAction(Model model, MultipartHttpServletRequest req) throws IllegalStateException{
		
		
		///////////////교육 인셉션 달기
//		Iterator<String> itr = req.getFileNames();
//		while (itr.hasNext()) {
//			MultipartFile mFile = req.getFile(itr.next());
//			
//			String realName = mFile.getOriginalFilename();
//			String saveFileName = System.currentTimeMillis() + "_"+ mFile.getOriginalFilename();
//			//맵에  담아서 DB에 저장
//			
//			mFile.transferTo(new File("저장경로+파일명"));
//			
//		}
		
		
		//서버의 물리적 경로 가져오기
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		//폼값과 파일명을 저장 후 View로 전달하기 위한 맵 생성
		Map returnObj = new HashMap();
		try {
			
			//업로드폼의 file속성의 필드를 가져온다. (여기서는 2개임)
			Iterator itr = req.getFileNames();
			
			MultipartFile mfile = null;
			String fileName = "";
			List resultList = new ArrayList();
			//파일 외의 폼값 받음(여기서는 제목만 있음)
			String title = req.getParameter("title");
			System.out.println("title="+title);
			
			/*
			물리적 경로를 기반으로 File객체를 생성한 후 지정된 디렉토리가 
			존재하는지 확인함. 
			만약 없다면 생성함. 
			 */
			File directory = new File(path);
			if(!directory.isDirectory()) {
				directory.mkdirs();
			}
			
			//업로드폼의 file속성의 필드 개수만큼 반복
			while(itr.hasNext()) {
				
				//전송된 파일의 이름을 읽어옴
				fileName = (String)itr.next();
				mfile = req.getFile(fileName);
				System.out.println("mfile="+mfile);
				
				//한글깨짐방지 처리 후 전송된 파일명을 가져옴. 
				String originalName = 
						new String(mfile.getOriginalFilename().getBytes(), "UTF-8");
				
				//서버로 전송된 파일이 없다면 while문의 처음으로 돌아간다. 
				if("".equals(originalName)){
					continue;
				}
				
				//파일명에서 확장자 부분을 가져옴
				String ext = originalName.substring(originalName.lastIndexOf('.'));
				
				//UUID를 통해 생성된 문자열과 확장자를 합침
				String saveFileName = getUuid() + ext;
				//물리적 경로에 새롭게 생성된 파일명으로 파일 저장
				File serverFullName = new File(path + File.separator + saveFileName);
				
				mfile.transferTo(serverFullName);
				
				//서버에 파일업로드 완료 후
				Map file = new HashMap();
				file.put("originalName", originalName); 		//원본파일명
				file.put("saveFileName",  saveFileName);		//저장된 파일명
				file.put("serverFullName", serverFullName);		//서버의 전체 경로
				file.put("title", title);						//제목
				
				//위 네 가지 정보를 저장한 Map을 ArrayList에 저장한다. 
				resultList.add(file);
			}
			returnObj.put("files", resultList);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("returnObj", returnObj);
		return "board/uploadAction";
		

	}
	
	

	//파일 목록 보기
	@RequestMapping("uploadList")
	public String uploadList(HttpServletRequest req, Model model) {
		
		//서버의 물리적 경로 가져오기
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		
		//경로를 기반으로 File객체 생성
		File file = new File(path);
		//파일의 목록을 배열 형태로 얻어옴
		File[] fileArray = file.listFiles();
		//View로 파일 목록을 전달하기 위해 Map 생성함. 
		Map<String, Integer> fileMap = new HashMap<String, Integer>();
		for(File f : fileArray) {
			
			/*
			Map의 key값은 파일명, value값은 파일의 용량을 저장함. 
			 */
			fileMap.put(f.getName(), (int)Math.ceil(f.length()/1024.0));
		}
		
		model.addAttribute("fileMap", fileMap);
		System.out.println(fileMap);
		return "board/uploadList";
	}
	
	
	@RequestMapping("/download.do")
	public ModelAndView download(HttpServletRequest req, HttpServletResponse resp) throws Exception{

		String fileName = req.getParameter("fileName");
		String oriFileName = req.getParameter("oriFileName");
		String saveDirectory = req.getSession().getServletContext().getRealPath("/resources/upload");
		File downloadFile = new File(saveDirectory + "/"+fileName);
		if(!downloadFile.canRead()) {
			throw new Exception("파일을 찾을 수 없습니다.");
		}
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("fileDownloadView");
		mv.addObject("downloadFile", downloadFile);
		mv.addObject("oriFileName", oriFileName);
		
		return mv;
	}
	
	
	
	
	 @RequestMapping(value="/fileDelete", method=RequestMethod.POST)
	 @ResponseBody
	 public String fileDelete(HttpServletRequest req, @RequestParam Map<String, Object> map) throws Exception {

		 int result  = 1;
		 System.out.println("fileDelete로 들어옴");

		 
//		String oriFileName = req.getParameter("oriFileName");
		String fileName = (String) map.get("fileDelete");
		System.out.println("삭제되는 파일 이름:"+fileName);
		String saveDirectory = req.getSession().getServletContext().getRealPath("/resources/upload");
		File deleteFile = new File(saveDirectory + "/"+fileName);
		
		System.out.println("deleteFile"+deleteFile);
		deleteFile.delete();
		//new File(saveDirectory+fileName.replace('/',File.separatorChar)).delete(); //파일 삭제       

		return Integer.toString(result);
	 }
	
	
//	 @RequestMapping(value="/fileDelete", method=RequestMethod.POST)
//	 public String fileDelete(HttpServletRequest req, @RequestParam Map<String, Object> map) throws Exception {
//		 int result = 1;
//		System.out.println("fileDelete로 들어옴");
//		String oriFileName = req.getParameter("oriFileName");
//		String nnnn = req.getParameter("id");
//		System.out.println(nnnn);
//		String fileName = (String) map.get("fileDelete");
//		System.out.println("삭제되는 파일 이름:"+fileName);
//		String saveDirectory = req.getSession().getServletContext().getRealPath("/resources/upload");
//		File deleteFile = new File(saveDirectory + "/"+fileName);
//		
//		
//		System.out.println("deleteFile"+deleteFile);
//		deleteFile.delete();
//		//new File(saveDirectory+fileName.replace('/',File.separatorChar)).delete(); //파일 삭제       
//
//		 return Integer.toString(result);
//	 }

}
