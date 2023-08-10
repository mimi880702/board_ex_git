package com.jafa.task;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jafa.AppTest;
import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.repository.BoardAttachRepository;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileCheckTaskTest extends AppTest{

	@Autowired
	BoardAttachRepository boardAttachRepository; 
	
	@Test
	public void test() {
		
		// 어제 데이터베이스에 기록된 파일 정보 
		List<BoardAttachVO> fileList = boardAttachRepository.pastFiles();
		
		List<Path> fileListPath = fileList.stream()
			.map(vo -> Paths.get("c:/storage",vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName()))
			.collect(Collectors.toList());
		
		fileList.stream()
			.map(vo -> Paths.get("c:/storage",vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
			.forEach(e-> fileListPath.add(e));
		
//		log.info(fileListPath);
		
		
		// 어제 날짜 폴더에 있는 모든 파일
		// c:/storage/2023/07/13
//		File taregetDir = new File("c:/storage",getYesterdayFolder());
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
	
//	@Test
	public void test2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.HOUR, 2);

		log.info(cal.after(cal2)); // cal은 cal2보다 이후 시간인가?
		log.info(cal.before(cal2)); // cal은 cal2보다 이전 시간인가?
		
		log.info(sdf.format(cal.getTime()));
	}
	
//	@Test
	public void test3() {
		List<String> list = List.of("가","나","다","라","마");
		log.info(list);
		log.info(list.contains("가"));
		log.info(list.contains("A"));
	}
	
//	@Test
	public void test4() {
		
		List<BoardVO> list = new ArrayList<BoardVO>();
		
		IntStream.rangeClosed(1, 3).forEach(i->{
			BoardVO vo = BoardVO.builder()
					.title("제목"+i)
					.content("내용"+i)
					.writer("작성자"+i).build();
			list.add(vo);
		});
		// 컬렉션 스트림 
		// T : BoardVO R : String
		List<String> collect = 
		list.stream().map(vo-> vo.getTitle()) // Stream<BoardVO> => Stream<String>
			.collect(Collectors.toList()); // Stream<String> => List<String>
		log.info(collect);
	}

//	@Test
	public void test5() {
		// 배열 스트림 
		String[] strArr = {"가","나","다","라","마"};
		Arrays.stream(strArr).forEach(s-> log.info(s));
	}
	
}
