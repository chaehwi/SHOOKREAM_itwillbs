<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<title>장바구니</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- 네이버아이디로그인 -->
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<!-- 구글 아이디 로그인 -->
<meta name="google-signin-client_id" content="1047574308186-h6ehte2k4901kjn1u3g5vnonbf2g56on.apps.googleusercontent.com">

<style type="text/css">
#sform {
          display: inline-block;
          text-align: center;
        }
</style>
<style type="text/css">
#table, {
     text-align: center;
}
</style>
<style>
    .paging {
        text-align: center;
    }
    .paging a {
        /*
        display: inline-block 인라인 요소의 특징과 블록 요소의 특징을 모두 갖는다
        크기를 가질 수 있으며 텍스트 정렬도 적용받는다
        */
        display: inline-block;
        
        font-weight: bold;
        text-decoration: none;
        padding: 5px 8px;
        border: 1px solid #ccc;
       	color: #000; 
/*         background-color: #F5F5DC; */
    }
    /* 현재 페이징에 select 클래스를 적용한다*/
    .paging a.select {
/*         color: #fff; */
/*         background-color: #FFA7A7; */
    }
    </style>

<style>
.w3-sidebar a {font-family: "Roboto", sans-serif}
body,h1,h2,h3,h4,h5,h6,.w3-wide {font-family: "Montserrat", sans-serif;}
</style>
</head>
<body class="w3-content" style="max-width:1200px">
<!-- Sidebar/menu -->
<jsp:include page="../inc/side.jsp"/>

<!-- Top menu on small screens -->
<header class="w3-bar w3-top w3-hide-large w3-black w3-xlarge">
  <div class="w3-bar-item w3-padding-24 w3-wide">LOGO</div>
  <a href="javascript:void(0)" class="w3-bar-item w3-button w3-padding-24 w3-right" onclick="w3_open()"><i class="fa fa-bars"></i></a>
</header>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:250px">

  <!-- Push down content on small screens -->
<!--   <div class="w3-hide-large" style="margin-top:83px"></div> -->
  
  <!-- Top header -->
  <header class="w3-container w3-xlarge">
    <p class="w3-left">장바구니</p>
    <p class="w3-right">
      <i class="fa fa-shopping-cart w3-margin-right"></i>
      <i class="fa fa-search"></i>
    </p>
</header>
   
  <!-- Footer -->
  <footer class="w3-padding-64 w3-small w3-center" id="footer">
  <table class="table">
  <thead  class="table-dark" >
    <tr>
      <th scope="col">번호</th>
      <th scope="col">선택</th>
      <th scope="col">image</th>
      <th scope="col">name</th>
      <th scope="col">brand</th>
      <th scope="col">price</th>
      <th scope="col">size</th>
      <th scope="col">개수</th>
      <th scope="col">delete</th>
    </tr>
  </thead>
  <tbody>
    
    <c:forEach var="cart" items="${cartlist }">
    <tr>
      <th scope="row">${cart.cart_idx }</th>
	  <td><input type="checkbox" class ="cartCheckBox" id="cartCheckBox" name ="cartCheckBox" checked="checked" value="${cart.cart_idx }"></td>
      <td><img src="upload/${cart.product_img }"  alt="없음!" class="img-thumbnail" width="150" height="150"></td>
      <td>${cart.product_name }</td>
      <td>${cart.product_brand }</td>
	  <td><fmt:formatNumber value="${cart.product_price }" pattern="#,###"></fmt:formatNumber></td>
      <td>${cart.product_size }</td>
      <td>${cart.product_amount }</td>
      <td>
      <button type="button" class=	"btn btn-dark" onclick="location.href='ProductInfoForm.po?product_idx=${cart.product_idx }'">상세내용</button>
      <button type="button" class="btn btn-dark" onclick="location.href='CartDeletePro.ca?cart_idx=${cart.cart_idx }'">삭제</button>
      </td>
    </tr>
    </c:forEach>
  </tbody>
</table>
	<div class="container px-4 text-center">
	  <div class="row gx-5">
	    <div class="col">
<div class="p-3 border bg-light"><h4>총 금액 : <fmt:formatNumber value="${total }" pattern="#,###"></fmt:formatNumber> </h4></div>	    </div>
	    <div class="col">
	      <div class="p-3 border bg-light">
	      <button onclick="">구매하기</button>
	      
	      <button onclick="test()">test</button>
	      </div>
	    </div>
	  </div>
</div>
<!-- 페이징 처리 -->	
	<div class="paging">
        <c:choose>
			<c:when test="${param.pageNum > 1}">
				<a href="CartList.ca?pageNum=${param.pageNum - 1 }&member_idx=${member_idx }">이전</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)">이전</a>
			</c:otherwise>
		</c:choose>
		
		<c:forEach var="i" begin="${pageInfo.startPage }" end="${pageInfo.endPage }">
			<!-- 단, 현재 페이지 번호는 링크 없이 표시 -->
			<c:choose>
				<c:when test="${param.pageNum eq i}">
					${i }
				</c:when>
				<c:otherwise>
					<a href="CartList.ca?pageNum=${i }&member_idx=${member_idx }">${i }</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
		<c:choose>
			<c:when test="${param.pageNum < pageInfo.maxPage}">
				<a href="CartList.ca?pageNum=${param.pageNum + 1 }&member_idx=${member_idx }">다음</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)">다음</a>
			</c:otherwise>
		</c:choose>
<!--         <a class="select" href="#">1</a> -->
<!--         <a href="#">2</a> -->
<!--         <a href="#">3</a> -->
<!--         <a href="#">4</a> -->
<!--         <a href="#">5</a> -->
    </div>
</footer>
  



<!-- Newsletter Modal -->
<div id="newsletter" class="w3-modal">
  <div class="w3-modal-content w3-animate-zoom" style="padding:32px">
    <div class="w3-container w3-white w3-center">
      <i onclick="document.getElementById('newsletter').style.display='none'" class="fa fa-remove w3-right w3-button w3-transparent w3-xxlarge"></i>
      <h2 class="w3-wide">NEWSLETTER</h2>
      <p>Join our mailing list to receive updates on new arrivals and special offers.</p>
      <p><input class="w3-input w3-border" type="text" placeholder="Enter e-mail"></p>
      <button type="button" class="w3-button w3-padding-large w3-red w3-margin-bottom" onclick="document.getElementById('newsletter').style.display='none'">Subscribe</button>
    </div>
  </div>
</div>


<!-- ------------------------------------------------------------------------------------------------------------>
<!-- 자바스크립트 부분 -->
<script type="text/javascript">
	function order() {
		
	}


</script>

<script>
// Accordion 
function myAccFunc() {
  var x = document.getElementById("demoAcc");
  if (x.className.indexOf("w3-show") == -1) {
    x.className += " w3-show";
  } else {
    x.className = x.className.replace(" w3-show", "");
  }
}

function myAccFunc1() {
	  var x = document.getElementById("cusAcc");
	  if (x.className.indexOf("w3-show") == -1) {
	    x.className += " w3-show";
	  } else {
	    x.className = x.className.replace(" w3-show", "");
	  }
	}

// Click on the "Jeans" link on page load to open the accordion for demo purposes
document.getElementById("myBtn").click();


// Open and close sidebar
function w3_open() {
  document.getElementById("mySidebar").style.display = "block";
  document.getElementById("myOverlay").style.display = "block";
}
 
function w3_close() {
  document.getElementById("mySidebar").style.display = "none";
  document.getElementById("myOverlay").style.display = "none";
}
</script>
<!-- Channel Plugin Scripts -->
<script>
  (function() {
    var w = window;
    if (w.ChannelIO) {
      return (window.console.error || window.console.log || function(){})('ChannelIO script included twice.');
    }
    var ch = function() {
      ch.c(arguments);
    };
    ch.q = [];
    ch.c = function(args) {
      ch.q.push(args);
    };
    w.ChannelIO = ch;
    function l() {
      if (w.ChannelIOInitialized) {
        return;
      }
      w.ChannelIOInitialized = true;
      var s = document.createElement('script');
      s.type = 'text/javascript';
      s.async = true;
      s.src = 'https://cdn.channel.io/plugin/ch-plugin-web.js';
      s.charset = 'UTF-8';
      var x = document.getElementsByTagName('script')[0];
      x.parentNode.insertBefore(s, x);
    }
    if (document.readyState === 'complete') {
      l();
    } else if (window.attachEvent) {
      window.attachEvent('onload', l);
    } else {
      window.addEventListener('DOMContentLoaded', l, false);
      window.addEventListener('load', l, false);
    }
  })();
  ChannelIO('boot', {
    "pluginKey": "552ea0bb-d4a5-4c70-8ba7-463b7682c434"
  });
</script>

<!-- 체크박스에 따른 금액변동 처리 -->
<script type="text/javascript">

// if($("#cartCheckBox").is(":checked") == true){
// 	alert("true");
// }



// $("#cartCheckBox").click(function() {
	
// 	let check = $("#cartCheckBox").val();
// 		$.ajax({
// 				type: "get",
// 				url: "CartMinusPro.ca",
// 				data: {
// 					cart_idx: check
// 				},
// 				dataType: "text",
// 				success: function() {
// 					alert("성공함");
// 				}
// 			});
// });

$("input[name='cartCheckBox']").click(function() {
	//체크박스의 체크 여부를 판별
	var isChecked = $("#cartCheckBox").is(":checked");
	//배열 선언
	let listArr = new Array();
	let list = $("input[name='cartCheckBox']:checked");
	//listArr에 각 체크박스의 값을 넣음
	for(var i=0; i<list.length; i++){
		if(list[i].checked){
			listArr.push(list[i].value);
		}
	//onchange를 통해 변경된 idx 값 가져오기
	$("input[name='cartCheckBox']:checked").change(function(){
		alert(listArr[i] 값이 변경되었습니다.);
	}
			
// 	var product_idx = listArr[i].val();
// 	alert(product_idx);
			
			
			
// 	for(int i=0; i<listArr.length; i++){
		
// 	}
	
// 	 var product_idx = $("input[name='cartCheckBox']").val();
// 	 alert(product_idx);

});
	


//ajax 사용
// $(document).ready(function() {
// 	$("#cartCheckBox").click(function() {
	
		
// 	});
// });














// function setInfo() {

// let totalPrice = 0;
// let totalCount = 0;

// $(".cartCheckBox").each(function(index, element){
// 	if($(element).find(".productCheckbox").is(":checked") === true){	//체크여부
// 		// 총 가격
// 		totalPrice += parseInt($(element).find(".product_price").val());
// 		// 총 갯수
// 		totalCount += parseInt($(element).find(".product_amount").val());
		
// 		document.querySelector('#testResultBox').innerText = totalPrice
		
// 	}
// });

	
// }

// $(".cartCheckBox").on("change", function(){
// 	setInfo($(".cartCheckBox"));
// });







//체크 여부에 따라 + - 처리
// $(document).ready(function(){
//     $("#cartCheckbox").change(function(){
// 		let totalPrice = ${total};
// 		let cartPrice = ("#cartPrice").val();
//     	if($("#cartCheckbox").is(":checked")){
// // 			totalPrice += $("#cartPrice").val();	
// // 			alert("변경 후 값 : " + totalPrice);
// 			alert("값 : " + cartPrice);
// //             alert("체크박스 체크 선택!");
//         }else{
//             alert("체크박스 체크 해제!");
//         }
//     });
// });




</script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript">
function iamport(){
	// 	
		//가맹점 식별코드
		IMP.init('imp77718215');
		IMP.request_pay({
		    pg : 'kakaopay',
		    pay_method : 'cart',
		    merchant_uid : 'merchant_' + new Date().getTime(),
		    name : 'SHOOKREAM' , //결제창에서 보여질 이름
		    amount : '${total }', //실제 결제되는 가격
		    buyer_name : '${sessionScope.sId}',
		}, function(rsp) {
			console.log(rsp);
		    if ( rsp.success ) {
		    	var msg = '결제가 완료되었습니다.';
		        msg += '고유ID : ' + rsp.imp_uid;
		        msg += '상점 거래ID : ' + rsp.merchant_uid;
		        msg += '결제 금액 : ' + rsp.paid_amount;
		        msg += '카드 승인번호 : ' + rsp.apply_num;
		        location.href="ProductOrderPro.po?order_category=주문완료&order_progress=배송완료&member_idx=${member_idx}&product_idx=${product.product_idx}&product_amount=${product.product_amount}&product_sell_count=${product.product_sell_count} ";
		    } else {
		    	 var msg = '결제에 실패하였습니다.';
		         msg += '에러내용 : ' + rsp.error_msg;
		         window.history.back();
		    }
		    alert(msg);
		    
		});
	}

	
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
</body>
</html>
