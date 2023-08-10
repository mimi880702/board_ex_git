package com.jafa.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileUploadControllerTest {

	@Test
	@Ignore
	public void test() {
		Date now = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		log.info(sdf.format(now));
		
		File uploadPath = new File("c:/storage", sdf.format(now)); // c:/storage/2023/07/10
		log.info(uploadPath.exists());
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
	}

	@Test
	@Ignore
	public void checkImgType() throws IOException {
		File file = new File("test.zip");
		String contentType = Files.probeContentType(file.toPath()); // image
		log.info(contentType);
		log.info(contentType.startsWith("image"));
	}
	
	@Test
	@Ignore
	public void substringTest() {
		String str = "abcdefghijklmnop";
		int indexOf = str.indexOf("d"); // 해당 인덱스번호 3
		int indexOf2 = str.indexOf("가"); // 존재하지 않으면  -1
		log.info(indexOf);
		log.info(indexOf2);
		
		String sub = str.substring(indexOf+1);
		log.info(sub);
	}

	@Test
	@Ignore
	public void substringTest2() {
		String fileName = "c:/storage/2020/07/12/30498c7e-6914-4437-bb16-61716454ba17_블로그계정.txt";
		int idx = fileName.indexOf("_");
		String subStr = fileName.substring(idx+1);
		log.info(subStr);
	}
	
	@Test
	public void substringTest3() {
		String fileName = "c:/storage/2020/07/12/s_30498c7e-6914-4437-bb16-61716454ba17_블로그계정.txt";
		int idx = fileName.lastIndexOf("_");
		String subStr = fileName.substring(idx+1);
		log.info(subStr);
	}
}
