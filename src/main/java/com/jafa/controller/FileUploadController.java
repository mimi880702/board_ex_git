package com.jafa.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jafa.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Log4j
@RestController
@RequestMapping("/files")
public class FileUploadController {

	
	@PostMapping("/upload")
	public ResponseEntity<List<BoardAttachVO>> upload(@RequestParam("uploadFile") MultipartFile[] multipartFiles){
		List<BoardAttachVO> list = new ArrayList<BoardAttachVO>();
		File uploadPath = new File("c:/storage",getFolder()); // c:/storage/2023/07/11

		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : multipartFiles) {
			BoardAttachVO attachVO = new BoardAttachVO();
			
			String fileName = multipartFile.getOriginalFilename();// 파일이름
			String uuid = UUID.randomUUID().toString(); // 파일이름 중복 방지
			File saveFile = new File(uploadPath,uuid+"_"+fileName);
			// c:/storage/2023/07/11/ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ_test.txt
			
			attachVO.setFileName(fileName);
			attachVO.setUuid(uuid);
			attachVO.setUploadPath(getFolder());
			
			try {
				if(checkImageType(saveFile)) {
					attachVO.setFileType(true);
					// 섬네일 이미지 업로드
					File thumbFile = new File(uploadPath,"s_"+uuid+"_"+fileName);
					FileOutputStream thumbnail = new FileOutputStream(thumbFile); 
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 50, 50);
				}
				multipartFile.transferTo(saveFile); // 실제 디렉토리에 물리적으로 파일 저장
				list.add(attachVO);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(list,HttpStatus.OK); 
	}

	private boolean checkImageType(File file) throws IOException {
		String contentType = Files.probeContentType(file.toPath());
		return contentType!=null ? contentType.startsWith("image") : false;
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(new Date()); // "2023/07/11"
	}
	
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(String fileName){
		File file = new File("c:/storage/"+fileName);
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> result = null; 
		try {
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers,HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result; 
	}
	
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(BoardAttachVO vo){
		// 원본파일 삭제 
		File file = new File("c:/storage/"+vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName());
		file.delete();
		// 섬네일 삭제 
		if(vo.isFileType()) {
			file = new File("c:/storage/"+vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName());
			file.delete();
		}
		return new ResponseEntity<>("success",HttpStatus.OK); 
	}
	
	@GetMapping("/download")
	public ResponseEntity<Resource> dowloadFile(String fileName){
		Resource resource = new FileSystemResource("c:/storage/"+fileName);
		// 
		if(!resource.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		String resouceName = resource.getFilename(); 
		String resouceOriginName = resouceName.substring(resouceName.indexOf("_")+1);
		
		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = URLEncoder.encode(resouceOriginName,"utf-8");
			headers.add("Content-Disposition", "attachment; fileName="+downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(resource,headers,HttpStatus.OK);
	}
	
}
