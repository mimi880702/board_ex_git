package com.jafa.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO implements Serializable{
	
	private static final long serialVersionUID = -1161300462847277536L;
	
	private String memberId; 
	private String memberPwd; 
	private String memberName; 
	private String email; 
	private boolean enabled;
	
	private LocalDateTime regDate; 
	private LocalDateTime updateDate; 
	
	private List<AuthVO> authList; 
}
