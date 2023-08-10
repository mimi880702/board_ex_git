package com.jafa.service;

import java.security.SecureRandom;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.exception.NotFoundMemberException;
import com.jafa.repository.MemberRepository;

@Service
public class MailSendService {
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private MemberRepository memberRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private int authNumber; 
	
	// 가입 인증번호
	public void makeRandomNumber() {
		Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111;//111111 ~ 999999 (6자리 난수)
		authNumber = checkNum;
		System.out.println("인증번호 : " + authNumber);
	}
	
	// 임시비밀번호 생성 
	public String generateTempPassword() {
		StringBuilder charSb = new StringBuilder();
		for (char c = 48; c <= 122; c++) {
			if(c>=58 && c<=64 || c>=91 && c<=96) continue;
			charSb.append(c);
		}
		String characters = charSb.toString();
		Random random = new SecureRandom();
		
		StringBuilder sb = new StringBuilder(12);
		for (int i = 0; i < 12; i++) {
			int randomIdx = random.nextInt(characters.length()); // 0-18 
			char randomChar = characters.charAt(randomIdx);
			sb.append(randomChar);
		}
		return sb.toString();
	}
	
	// 회원가입 인증 메일 양식 
	public String joinEmail(String email) {
		makeRandomNumber(); // 인증번호 생성
		String setFrom = "p_metaphor@naver.com"; // 발신자  
		String toMail = email; // 수신자
		String title = "회원 가입 인증 이메일 입니다.";  // 메일 제목
		String content = "회원가입을 위한 인증 번호는 " + authNumber + "입니다." + "<br>" + 
			    "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
		mailSend(setFrom, toMail, title, content);
		return Integer.toString(authNumber);
	}
	
	// 아이디 찾기 메일 양식 
	public void findIdByEmail(String email) {
		
		String findMemberId = memberRepository.selectByEamil(email);
		if(findMemberId==null) {
			throw new NotFoundMemberException();
		}
		
		String setFrom = "p_metaphor@naver.com"; // 발신자  
		String toMail = email; // 수신자
		String title = "아이디 찾기 메일 서비스 입니다.";  // 메일 제목
		String content = "회워님의 아이디는 <b>" + findMemberId + "</b>입니다."; 
		mailSend(setFrom, toMail, title, content);
	}
	
	// 임시비밀번호 메일 전송 
	@Transactional
	public void findPwdByEamil(String email) {
		String findMemberId = memberRepository.selectByEamil(email);
		if(findMemberId==null) {
			throw new NotFoundMemberException();
		}
		String tempPassword = generateTempPassword();
		memberRepository.updatePassword(findMemberId, passwordEncoder.encode(tempPassword));
		
		String setFrom = "p_metaphor@naver.com"; // 발신자  
		String toMail = email; // 수신자
		String title = "임시비밀번호 발급 메일 서비스 입니다.";  // 메일 제목
		String content = "회원님의 비밀번호는 <b>" + tempPassword + "</b>입니다."; 
		mailSend(setFrom, toMail, title, content);
	}
		
	//이메일 전송 메소드
	public void mailSend(String setFrom, String toMail, String title, String content) { 
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
			helper.setFrom(setFrom); // 발신자 
			helper.setTo(toMail); // 수신자 
			helper.setSubject(title); // 메일 제목
			helper.setText(content,true); // 메일 내용 true : html 형식으로 전송
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


}
