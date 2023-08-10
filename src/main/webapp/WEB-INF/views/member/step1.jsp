<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="container">
	
	<div class="row my-5">
		<div class="col-2">
			<ul class="list-group">
				<li class="list-group-item active">이용약관</li>
				<li class="list-group-item">이메일 인증</li>
				<li class="list-group-item">회원가입작성</li>
			</ul>		
		</div>
		<div class="col-10">
			<form action="${ctxPath}/join/step2" method="post" id="termForm">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<div class="form-group">
					<h4>이용약관</h4>
					<textarea rows="6" class="form-control my-2 bg-light border border-primary" readonly="readonly"></textarea>
					<label class="mt-3">
						<input type="checkbox" name="agreement">
						<span>위 이용약관에 동의합니다.</span>
					</label>
				</div>
				<div class="form-group">
					<h4>개인정보 수집 및 이용에 대한 안내</h4>
					<textarea rows="6" class="form-control my-2 bg-light border border-primary" readonly="readonly"></textarea>
					<label class="mt-3">
						<input type="checkbox" name="agreement">
						<span>위의 개인정보 수집 및 이용에 대한 안내에 동의합니다</span>
					</label>
				</div>
				
				<div class="border border-primary my-5 py-3 text-center">
					<label>					
						<input type="checkbox" class=checkAll>
						<span>위의 이용약관 및 개인정보 수집 및 이용에 대한 안내에 동의합니다.</span>
					</label>	
				</div>
					
				<div class="my-3 text-center">
					<button type="button" class="btn btn-outline-primary nextBtn col-3">다음</button>
					<button type="button" class="btn btn-outline-danger cancelBtn col-3">취소</button>
				</div>
			</form>
		</div> <!-- col-10 end -->
	</div> <!-- row end -->
	
</div> <!-- container end -->

<script>
$(function(){
	
	let termForm = $('#termForm');
	
	// 모두 동의 체크 
	$('.checkAll').change(function(){
		if($(this).is(':checked')){ // 체크된 경우
			$('[name="agreement"]').prop('checked',true);
		} else { // 해제된 경우
			$('[name="agreement"]').prop('checked',false);
		}
	});
	
	// 다음 단계
	$('.nextBtn').click(function(){
		let checkFlag = [];
		$('[name="agreement"]').each(function(index,element){
			checkFlag.push($(element).is(':checked'));
		});
		
		if(!checkFlag[0]){
			alert('이용약관에 동의 해주세요');
			return;
		}
		
		if(!checkFlag[1]){
			alert('개인정보 수집 및 이용에 대한 안내에 동의해주세요');
			return;
		}

		termForm.submit();
	});

	
});
	
</script>
