console.log('register.js')
$(function(){
	// 첨부파일 목록 배열 
	let uploadResultList = [];
	
	// 파일 업로드 결과 화면에 나타냄 
	let showUploadResult = function(attachList){
		let fileList = '';
		$(attachList).each(function(i,e){
			uploadResultList.push(e); // 첨부파일 배열의 요소로 추가
			fileList += 
			`<li class="list-group-item" data-uuid="${e.uuid}">
				<div class="float-left">`
				if(e.fileType){ // 이미지 파일일 때
					let filePath = e.uploadPath+"/s_"+e.uuid+"_"+e.fileName;
					let encodingFilePath = encodeURIComponent(filePath) 
					fileList += 
					`<div class="d-inline-block mr-4">
						<img alt="첨부파일" src="${ctxPath}/files/display?fileName=${encodingFilePath}">
					</div>`
				} else { // 이미지 파일이 아닐 때 
					fileList += 
					`<div class="d-inline-block mr-4" style="width: 40px">
						<img alt="첨부파일" src="${ctxPath}/resources/images/attach.png" style="width: 100%">
					</div>`
				}
				fileList += 	
					`<div class="d-inline-block">
						${e.fileName}
					</div>
				</div>
				<div class="float-right">
					<a href="#" class="delete">삭제</a>
				</div>
			</li>`;
		});
		$('.uploadResultDiv ul').append(fileList);
	} // showUploadResult
	
	// 파일업로드 
	$('[type="file"]').change(function(){
		let formData = new FormData(); 
		let files = this.files;

		// chekcExtension() 파일 유효성 검사 통과:true, 실패 :faslse
		for(let f of files){
			if(!checkExtension(f.name, f.size)){
				$(this).val('');
				return; 
			}
			formData.append('uploadFile',f);
		}
		
		$.ajax({
			url : `${ctxPath}/files/upload`, 
			type : 'post', 
			data : formData, 
			processData : false, 
			contentType : false, 
			dataType : 'json', 
			success : function(attachList){
				showUploadResult(attachList);
				console.log(uploadResultList);
			}
		});
	});
	
	// 파일 삭제 
	$('.uploadResultDiv ul').on('click','.delete',function(e){
		e.preventDefault();
		let fileLi = $(this).closest('li');
		let uuid = fileLi.data('uuid');
		let targetFile = null; 
		let targetFileIdx = -1;  
		
		$(uploadResultList).each(function(i,e){
			if(e.uuid == uuid){
				targetFileIdx = i;
				targetFile = e; 
				return; 
			}
		})
		
		if(targetFileIdx!=-1) uploadResultList.splice(targetFileIdx,1);
		
		console.log(targetFile);
		console.log(uploadResultList);
		
		$.ajax({
			type : 'post',
			url : `${ctxPath}/files/deleteFile`, 
			data : targetFile, 
			success : function(result){
				console.log(result);
			}
		});
		fileLi.remove();
	});
	
	
	// 글쓰기 처리 
	$('.register').click(function(){
		let form = $('form');
		if(uploadResultList.length>0){
			$(uploadResultList).each(function(i,e){
				let uuid = $('<input/>',{type:'hidden', name : `attachList[${i}].uuid`, value : `${e.uuid}`});
				let fileName = $('<input/>',{type:'hidden', name : `attachList[${i}].fileName`, value : `${e.fileName}`});
				let fileType = $('<input/>',{type:'hidden', name : `attachList[${i}].fileType`, value : `${e.fileType}`});
				let uploadPath = $('<input/>',{type:'hidden', name : `attachList[${i}].uploadPath`, value : `${e.uploadPath}`});
				form.append(uuid)
					.append(fileName)
					.append(fileType)
					.append(uploadPath);
			});
		} 
		form.submit();
	})
	
})