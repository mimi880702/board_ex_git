package com.jafa.repository;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jafa.AppTest;
import com.jafa.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class BoardAttachRepositoryTest extends AppTest {

	@Autowired
	BoardAttachRepository repository;
	
	@Test
	@Ignore
	public void testInsert() {
		String fileName = "test02.png";
		String uuid = UUID.randomUUID().toString();
		
		BoardAttachVO vo = new BoardAttachVO();
		vo.setBno(1L);
		vo.setFileName(fileName);
		vo.setFileType(true);
		vo.setUuid(uuid);
		vo.setUploadPath("c:/upload");
		
		repository.insert(vo);
	}

	@Test
	public void selectTest() {
		repository.selectByBno(1L)
			.forEach(file -> log.info(file));
	}
	
	@Test
	public void deleteTest() {
		String uuid = "70c94097-2000-4015-bac1-7655dd5b1214";
		repository.delete(uuid);
	}	

}
