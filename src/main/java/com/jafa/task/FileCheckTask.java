package com.jafa.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jafa.domain.BoardAttachVO;
import com.jafa.repository.BoardAttachRepository;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	
	@Autowired
	BoardAttachRepository boardAttachRepository;
	
	@Scheduled(cron = "0 0 6 * * *")
	public void checkeFile() {
		
		/* 어제 데이터베이스에 기록된 파일 정보 */
		List<BoardAttachVO> fileList = boardAttachRepository.pastFiles();
		
		List<Path> fileListPath = fileList.stream()
				.map(vo -> Paths.get("c:/storage",vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName()))
				.collect(Collectors.toList());
			
			fileList.stream()
				.map(vo -> Paths.get("c:/storage",vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
				.forEach(e-> fileListPath.add(e));
			
			// 어제 날짜 폴더에 있는 모든 파일
			// c:/storage/2023/07/13
//			File taregetDir = new File("c:/storage",getYesterdayFolder());
			File taregetDir = Paths.get("c:/storage",getYesterdayFolder()).toFile();
			
			File[] delTargetList = taregetDir.listFiles(file-> !fileListPath.contains(file.toPath()));
			Arrays.stream(delTargetList).forEach(file->{
				file.delete();
			});		
	}
	
	private String getYesterdayFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return sdf.format(cal.getTime()); 
	}	
}
