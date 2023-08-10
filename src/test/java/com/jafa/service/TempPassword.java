package com.jafa.service;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

public class TempPassword {
	
	
	
	@Test
	public void test() {
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
		System.out.println(sb.toString());
	}
	
	@Test
	@Ignore
	public void test2() {
		StringBuilder characters = new StringBuilder();
		for (char c = 48; c <= 122; c++) {
			if(c>=58 && c<=64 || c>=91 && c<=96) continue;
			characters.append(c);
		}
		
		System.out.println(characters.toString());
	}
	
}
