<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="container">
	<div class="w-50 mx-auto my-5">
		<h1 class="text-center py-3">회원가입</h1>
		<form:form action="${ctxPath}/member/join" modelAttribute="memberVO">
			<div class="form-group row">
				<div class="col-9">
					<form:input class="form-control" path="memberId" placeholder="아이디"/>
				</div>
				<div class="col-3">
					<button type="button" class="idCheck btn btn-outline-info form-control">ID중복확인</button>
				</div>
			</div>
			<div class="form-group">
				<form:input class="form-control" path="memberName" placeholder="이름"/>
			</div>
			<div class="form-group">
				<form:input class="form-control" path="email" placeholder="이메일" readonly="true"/>
			</div>
			<div class="form-group">
				<form:password class="form-control" path="memberPwd" placeholder="비밀번호"/>
			</div>
			<button type="button" class="btn btn-outline-primary join">회원가입</button>
		</form:form>
	</div>	
</div>
<%@ include file="../includes/footer.jsp"%>

<script>
$(function(){
	let idCheckFlag = false;
	
	$('.idCheck').click(function(){
		let idInput = $('#memberId');
		let memberId = idInput.val();
		
		if(idInput.attr('readonly')){ // 이미 값이 입력된 경우
			idInput.attr('readonly',false);
			idInput.focus();
			$(this).html('ID중복확인');
			idCheckFlag = false;
			return;
		}
		
		if(memberId==''){
			alert('아이디를 입력하세요');
			return;
		}
		
		// 아이디 중복 검사 
		$.ajax({
			type : 'post', 
			url : '${ctxPath}/member/idCheck',
			data : {memberId : memberId}, 
			success :function(result){
				if(result){ // 사용가능한 경우(True) 
					 alert('사용할 수 있는 아이디 입니다.');
					 idCheckFlag = true;
					 $('.idCheck').html('변경');
					 idInput.attr('readonly',true);
				} else { // 중복 되는 경우(False) 
					alert('사용할 수 없는 아이디 입니다.');
					idInput.focus();
				}
			}
		})
	});
	
	$('.join').click(function(){
		
		if(!idCheckFlag){
			alert('ID중복 확인바람!')
			return;
		}
		
		$('#memberVO').submit();			
	})
});
</script>