<%--
  Created by IntelliJ IDEA.
  User: yhxkitest-INT
  Date: 2018-08-17
  Time: 오후 3:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>180817 Todoist</title>
    
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css" />

    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>   
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>




 <script type="text/javascript">

     //날짜 지나도 마감 기능 안들어가있음..ㅠㅠ
 var cont = ''; //컨텍스트 패스...

	
	$(function() {
		
		var show = true;
		init();
		
	       $('#datetimepicker1').datetimepicker({	//시작일      	 
	     			format: 'YYYY-MM-DD'
		     });
			
            $('#datetimepicker2').datetimepicker({  //종료일 
            		format: 'YYYY-MM-DD'
             });
	
		
		$('#myModal form').submit(function(e) { // 입력
			//수정 버튼으로 수정 완료 기능까지
			e.preventDefault();
			insertOne();
			$('#myModal').modal('hide'); 
		});
		
		
		$(document).on('click','table .showThis',function() { //상세보기
		
			var idx=$(this).parent().next().next().text();
			selectOne(idx);
		});


		$('#detailModal .modal-footer>button').eq(0).click(function(){ //수정 버튼 클릭시
			
			if(show){ //show가 true면

				$('#detailModal input').attr('type', 'text'); //detailModal 아래의 인풋 속성바꿈
				$('.detail').hide();
			}else{
				//input 으로 입력이 활성화되어 있을때에만 기능 실행
				$('#detailModal input').attr('type', 'hidden'); //detailModal 아래의 인풋 속성바꿈
				$('.detail').show();
				editOne();
			}
				show=!show; //기능하고 나면 show를 반대로 바꿔줌

		});
		

	 	$(document).on('click', 'table button', function(){ //완료하기 클릭시= 마감
			console.log($(this).parent().next().text());
			var idx = $(this).parent().next().text();
			var dateFrom = $(this).parent().prev().prev().prev().text();
			var dateTo =$(this).parent().prev().prev().text();
			var title =$(this).parent().prev().text();
			var status = true;
			var tags =$(this).parent().next().next().text();
			
			console.log('할일하나 완료할것이다...시작: '+dateFrom+' 종료:'+dateTo+' 타이틀 '+title+' 진행 '+status+'태그 '+tags+' idx'+idx);
			
			var params=JSON.stringify({
				'toDoIdx':idx,
				'dateFrom':dateFrom,
				'dateTo':dateTo, 
				'title':title,
				'tags':tags,
				'status': status
			});
			completeOne(params);
		});
		 


		$('#detailModal .modal-footer button').eq(1).click(function(){//상세 삭제 버튼 클릭시 이벤트

			var idx = $('.detail').eq(5).text();
			deleteOne(idx);//삭제

			$('#detailModal').modal('hide'); //상세페이지 숨김
            window.location.reload();
		});
		
		$(document).on('click','table .deleteThis',function(e) { //완료된 리스트 중에서 삭제하기
			e.preventDefault();
			var idx=$(this).parent().next().text();
			deleteOne(idx);
			alert("삭제되었습니다");
			window.location.reload();
		});
		
	});

	$(document).on('click', '#detailModal .detail a', function(e){ //태그로 검색하기
	    e.preventDefault();

        var tagForSearch = $(this).text();
        $('#detailModal').modal('hide');
        init("searchWithTag/"+tagForSearch);

    });

	$(document).on('click', '.container #goToList', function(e){ //리스트로 돌아가기
	    e.preventDefault();
	    init();

    });



	/////////////////
	
	function completeOne(params){
		console.log(params+" 완료 실행 "+$.parseJSON(params).toDoIdx);
	
		$.ajax({
			contentType:'application/json; charset=utf-8',
			type : 'PUT',
			url : 'http://localhost:8080/'+cont+$.parseJSON(params).toDoIdx,
			data: params,
			success : function(){
				console.log("one complete~");
				init(); //샐고~
			}
		});
	}


	function editOne(){
		var dateFrom=$('#detailModal input').eq(0).val();
		var dateTo=$('#detailModal input').eq(1).val();
		var title=$('#detailModal input').eq(2).val();
		var tags=$('#detailModal input').eq(3).val();
		//status는 상세 페이지가 아니라 리스트에서 할 수 있게 해놔서 안넘김..
		
		var toDoIdx=$('#detailModal input').eq(5).val();

	 	var param = JSON.stringify({
		    'dateFrom':dateFrom,
			'dateTo':dateTo, 
			'title':title,
			'tags':tags,
			'status': false
			//,'toDoIdx':toDoIdx
		}); 

		console.log(param);
		
	$.ajax({
		url : 'http://localhost:8080/'+cont+$('.detail').eq(5).text(),
		type : 'PUT',
        data : param,
		contentType:'application/json; charset=utf-8',
		success : function(){
			init(); //뒤의 목록을 샐고하는 역할... 이거 없으면 샐고 따로 안하면 수정반영 ㄴㄴ
			$('#detailModal').modal('hide'); //상세페이지 숨김
		},
		complete : function(data){
			console.log("메서드 실행 결과 "+JSON.stringify(data));
		}
	});

	}




	function deleteOne(idx){
		$.ajax({
			type : 'DELETE',
			url : 'http://localhost:8080/'+cont+idx,
			success : function(){
				console.log("delete complete~");
			}
		});
	}



	function insertOne() {
		var data =$('#myModal form').serialize();
		console.log(data);
		$.post('add', data, function(){
			//init(); //그냥 init으로 샐고하면 새로 입력할 때 먼저 썼던 할일이 계속 남아있어서 리로드해줌..
			window.location.reload();
		});
	}


	function selectOne(idx) {
		$.getJSON('http://localhost:8080/'+cont+idx, function(data){


            $('.detail').eq(0).text(data[0].dateFrom);
			$('.detail').eq(1).text(data[0].dateTo);
			$('.detail').eq(2).text(data[0].title);

			var tagsForText="";
			var tagsForLink="";
			for(var i=0; i<data[1].length; i++){
			    tagsForText+=(data[1][i].tag+" ");
			    tagsForLink+=("<a href='#' class='tagLink'>"+data[1][i].tag+"</a> ");
            }

			$('.detail').eq(3).html(tagsForLink);

			$('.detail').eq(4).text(data[0].status);
			$('.detail').eq(5).text(data[0].toDoIdx);

			//수정을 눌러도 값을 가져오도록..
			$('#detailModal input').eq(0).val(data[0].dateFrom);
			$('#detailModal input').eq(1).val(data[0].dateTo);
			$('#detailModal input').eq(2).val(data[0].title);
			$('#detailModal input').eq(3).val(tagsForText);
			$('#detailModal input').eq(4).val(data[0].status);
			$('#detailModal input').eq(5).val(data[0].toDoIdx);


		});
	}
	
	

  
	function init(url) {
		
		if(url == null) {
		    url='list';
        }
            var listObj = "";
            $.getJSON('http://localhost:8080/' + cont + url, function (data) {

                $(data).each(function (num, ele) {//첫번째 인자가 인덱스, 두번째 인자가 엘리먼트

                    if (!ele.status) {

                        listObj += '<tr><td hidden="hidden">' + ele.dateFrom + '</td>'
                            + '<td>' + ele.dateTo
                            + '</td><td><a href="#" class="showThis" data-toggle="modal" data-target="#detailModal">'
                            + ele.title + '</a></td><td>'
                            + '<button type="button" class="btn btn-primary btn-sm">완료하기</button>'
                            + '</td><td hidden="hidden">' + ele.toDoIdx
                            + '</td><td hidden=""hidden">' + ele.tags + '</td></tr>';
                    } else {
                        listObj += '<tr class="text-muted"><td class="col-md-2">' + ele.dateTo
                            + '</td><td class="col-md-5">' + ele.title + '</td><td class="col-md-1">'
                            + '<a href="#" class="deleteThis">삭제</a>'
                            + '</td><td hidden="hidden">' + ele.toDoIdx
                            + '</td></tr>';

                    }
                    console.log(num + ":" + listObj);
                });

                console.log(listObj);
                $('table>tbody').eq(1).html(listObj);
            });



	}
</script>
  </head>
  <body>
    	<div class="container">
            <h3><a href="#" id="goToList">To Do List</a></h3>
    		<table class="table">
    			<tr>
    				<th style="width: 10%">마감일</th>
    				<th style="width: 40%">목표</th>
    				<th style="width: 3%">완료</th>
    			</tr>
    			<tbody></tbody>
    		</table>


    		<!-- popup -->
    		<!-- Trigger the modal with a button -->
    		
<div class="modal-footer">
    <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">할 일 추가</button>
</div>
    <!-- Modal -->
    <div id="myModal" class="modal fade" role="dialog">
      <div class="modal-dialog">


        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">입력페이지</h4>
          </div>
          <div class="modal-body">
            <form>
    		  <div class="form-group">
    		  
    		  <label for="dateFrom, dateTo">날짜 설정</label>
    		    <div class='input-group date'> 
                    <input type='text' class="form-control" name="dateFrom" id="datetimepicker1" placeholder="시작일"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                    <input type='text' class="form-control" name="dateTo" id="datetimepicker2" placeholder="종료일"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>

<br/>

    		    <label for="title">목표 설정</label>
    		    <input type="text" name="title" class="form-control" id="title" placeholder="할 일을 입력하세요" />
    		  
    		    <input type="text" name="tags" class="form-control" id="tags" placeholder="태그" />
    		  </div>
    	
    		  
    		  <div class="form-group">
    		    <button type="submit" class="btn btn-default">Submit</button>
    		  </div>
    		 </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div>

      </div>
    </div>
    		<!-- popup end -->
    		
    		
    		
    		
    <!-- Modal  data-toggle="modal" data-target="#detailModal"-->
    <div id="detailModal" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">수정&삭제페이지</h4>
          </div>
          <div class="modal-body">
         
          		<p>
          			<label for="dateFrom">시작일</label>
          			<span class="detail"></span>
          			<input type="hidden" id="dateFrom">
          		</p>
          		<p>
          			<label for="dateTo">마감일</label>
          			<span class="detail"></span>
          			<input type="hidden" id="dateTo">
          		</p>
          		<p>
          			<label for="title">할 일</label>
          			<span class="detail"></span>
          			<input type="hidden" id="title">
          		</p>

          		<p>
          			<label for="tag">태그</label>
          			<span class="detail"></span>
          			<input type="hidden" id="tag">
          		</p>
          		
          		<p hidden="hidden">
          			<span class="detail"></span>
          			<input type="hidden" id="status">
          		</p>
          		<p hidden="hidden">         			
          			<span class="detail"></span>
          			<input type="hidden" id="toDoIdx">
          		</p>
          		
          		

        </div>
          <div class="modal-footer">
          	<button type="button" class="btn btn-default">Edit</button>

          	<button type="button" class="btn btn-default">Delete</button>
         											 <!-- //data-dismiss=창을 닫음 -->
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
          
        </div>

      </div>
    </div>
    		<!-- popup end -->




    	</div>




  </body>
</html>
