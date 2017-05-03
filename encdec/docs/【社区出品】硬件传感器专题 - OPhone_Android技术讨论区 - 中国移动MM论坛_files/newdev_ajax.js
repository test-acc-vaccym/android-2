var objtimeout;
function InitAjax() //初始化浏览器类型
{
  var ajax=false; 
  try {
      ajax = new ActiveXObject("Msxm12.XMLHTTP");
　  } catch (e){ 
  try { 
      ajax = new ActiveXObject("Microsoft.XMLHTTP");
　　} catch (e) { 
　　　ajax = false; 
　　} 
　}
  if (!ajax && typeof XMLHttpRequest!='undefined') { 
　　 ajax = new XMLHttpRequest(); 
　} 
  return ajax;
} 

//检查验证码是否正确
function ajax_check_seccode()
{
  	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var seccode = document.getElementById("seccode").value;
	var poststring = "seccode="+seccode+"&chkopt=chkseccode";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
	     
	     //begin add by zhang junting 2012-02-09
	     document.getElementById("showseccodemsg").style.display="block";
	     if(typeof changeHeight=="function")
	     {
	    	 changeHeight(); 
	     }
	     //end add;
		 var m_array = getMsg.split('<a><script');
		 getMsg = m_array[0];
	     document.getElementById("showseccodemsg").innerHTML = getMsg;		 

		 if(getMsg.indexOf('/cmdn/res/img/bbs/default/check_right.gif') >= 0)
		 {
			  //begin add by zhang junting 2012-02-09
			  document.getElementById("showseccodemsg").className='';
			  //end add;
			  document.getElementById("flag").value = 1;   //验证码正确
		  // document.getElementById("getpwd").innerHTML = "<a href='javascript:///' onclick='getPassword()'>获取动态密码</a>";	 
		}else{
			document.getElementById("flag").value = 0;   //验证码不正确
		}
	  }
	};
}
//获取动态密码
function getPassword()
{
  var ajax = InitAjax();
  var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
  var phonenumber = document.getElementById("phonenumber").value;
  var poststring = "chkopt=getpwd&phone="+phonenumber;
  if(phonenumber == '')
  {
	alert("请先输入手机号");
	return false;
  }
  //var regMobile = /^1[358]\d{9}$/;
  var regMobile = /^1(3[4-9]|47|5[012789]|8[23478])\d{8}$/ ;
  if(!regMobile.test(phonenumber))
  {
    alert("请输入正确的手机号码");
    return false;
  }
	  
  ajax.open("POST",urlRedirect,true);
  ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded"); 
  ajax.send(poststring);
  
  ajax.onreadystatechange = function()
  {
    if(ajax.readyState == 4 && ajax.status == 200)
	{
	  var issuccess = ajax.responseText;
	  if(issuccess == "1"){	//成功
		  alert("短信密码已发送，请注意查收");
	  }
	}
  };
}
//检查手机验证码是否正确
function ajax_check_seccodephone()
{
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	//var seccodePhone = document.getElementById("seccodePhone").value;
	//var poststring = "seccodePhone="+seccodePhone+"&chkopt=chkseccodephone";
	 var phonenumber = document.getElementById("phonenumber").value;
	 var phonepass = document.getElementById("phonepass").value;
	var poststring = "phonenumber="+phonenumber+"&phonepass="+phonepass+"&chkopt=chkseccodephone";
	
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 document.getElementById("showseccodephonemsg").innerHTML = getMsg;
	  }
	};
}

//发烧友注册时：根据手机品牌 查找手机类型
function getphonetype(brandid)
{
	document.getElementById("hidden_phonebrand").value=brandid
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "brandid="+brandid+"&chkopt=getphonetype";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 //alert(getMsg);
		document.getElementById("span_phonetype").innerHTML = getMsg;
	  }
	};
}

//注册时 检查用户名是否正确
function register_checkusername(username){
	var nametemp = username.replace(/ /g,"");
	if(nametemp == ""){
		alert("用户名不能为空");
		document.getElementById("username").focus();
		return false;
	}
	
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "username="+username+"&chkopt=register_checuserkname";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 //alert(getMsg);
		document.getElementById("checkusername").innerHTML = getMsg;
	  }
	};
}

//注册时 检查邀请码是否正确
function register_checkinvitenum(invitecode){
	var ivc = invitecode.replace(/ /g,"");
	if(ivc == ""){
		//alert("请输入邀请码");
		document.getElementById("checkinvitenum").innerHTML="";
		return false;
	}
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "invitecode="+invitecode+"&chkopt=register_checkinvitecode";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 //alert(getMsg);
		document.getElementById("checkinvitenum").innerHTML = getMsg;
	  }
	};
}

//注册时 检查phonenumber是否正确 不能重复
function register_checkphonenumber(phonenumber){
	var phonenumber = phonenumber.replace(/ /g,"");
	if(phonenumber == ""){
		//alert("请输入手机号码！");
		document.getElementById("checkphonenumber").innerHTML = '请输入手机号码！';
		return false;
	}
	//var regMobile = /^1[358]\d{9}$/;
	var regMobile = /^1(3[4-9]|47|5[012789]|8[23478])\d{8}$/ ;
	if(!regMobile.test(phonenumber)){
		//alert("请输入正确的手机号码！");
		document.getElementById("checkphonenumber").innerHTML = '您输入的不是移动的手机号，不能使用短信激活方式';
		return false;
	}
	phonenumber = setRSA2(phonenumber);
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "phonenumber="+phonenumber+"&chkopt=register_checkphonenumber";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 if(getMsg == "0"){
			document.getElementById("checkphonenumber").innerHTML = '<img src="/cmdn/res/img/bbs/default/check_right.gif" />'
			/*var email = document.getElementById("email").value;
			if(email == ""){
				document.getElementById("email").value = phonenumber+"@139.com";
			}*/
		 }else{
		 	//alert(getMsg);
			document.getElementById("checkphonenumber").innerHTML = '该手机号已被使用';
		 }
	  }
	};
	document.getElementById("checkphonenumber").innerHTML = '';
}

//注册时 检查email是否正确
function register_checkemail(email){
	var email = email.replace(/ /g,"");
	if(email == ""){
		alert("邮箱地址不能为空");
		document.getElementById("checkemail").innerHTML = '';
		return false;
	}
	if(email !=null && email!= ""){
		//var regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
		var regEmail = /^([a-zA-Z0-9]+[-|_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[-|_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
		if(email != ''){
			if(!regEmail.test(email))
			{
			  document.getElementById("checkemail").innerHTML = '请输入正确的邮箱地址';
			  return false;
			}
		}
		email = setRSA2(email);
		var ajax = InitAjax();
		var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
		var poststring = "email="+email+"&chkopt=register_checkemail";
		ajax.open("POST",urlRedirect,true);
		ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		ajax.send(poststring);
		ajax.onreadystatechange = function()
		{
		  if(ajax.readyState == 4 && ajax.status == 200)
		  {
			 var getMsg = ajax.responseText;
			 //alert(getMsg);
			//document.getElementById("checkemail").innerHTML = getMsg;
			if(getMsg.indexOf("img") > 0){
				var  regDomainName = /@(qq|139)\.com/;
				if(email.match(regDomainName ) != null){
					var regEmail = /^[0-9]+@([a-zA-Z0-9]+[-|_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
					if(!regEmail.test(email)){
					  document.getElementById("checkemail").innerHTML = getMsg + " <font color='#000000' id='emailbyname'>该邮箱可用，</font>不建议使用邮箱别名";
					  return false;
					}else{
						document.getElementById("checkemail").innerHTML = getMsg;
					}
				}else{
					document.getElementById("checkemail").innerHTML = getMsg;
				}
			}else{
				document.getElementById("checkemail").innerHTML = getMsg;
			}
		  }
		};
	}
}

//注册时 检查nickname是否正确 不能重复
function register_checknickname(nickname){
	var nickname = nickname.replace(/ /g,"");
	if(nickname == ""){
		alert("用户昵称不能为空");
		document.getElementById("checknickname").innerHTML = '';
		return false;
	}
	if(nickname.length <2 || nickname.length > 20){
	  alert("用户昵称长度为：2-20位");
	  document.getElementById("checknickname").innerHTML = '';
	  return false;
	}
	var myReg = /^[^@\/\'\\\"#$%&\^\*（）()。，‘“；：·~’]+$/;
	if(!myReg.test(nickname)){
		document.getElementById("spec").innerHTML = "昵称中不能包含特殊字符";
		return false;
	}else{
		document.getElementById("spec").innerHTML = "";	
	}
	nickname = setRSA2(nickname);
	nickname = encodeURIComponent(nickname);
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "nickname="+nickname+"&chkopt=register_checknickname";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 if(getMsg == "0"){
			 document.getElementById("checknickname").innerHTML = '<img src="/cmdn/res/img/bbs/default/check_right.gif" />'
		 }else if(getMsg == "2"){
		 	 //Add By HeWeihua 2013-4-15
		 	 document.getElementById("checknickname").innerHTML = '该用户昵称包含敏感字，不能使用';
		 }else{
		 	//alert(getMsg);
			document.getElementById("checknickname").innerHTML = '该用户昵称已被使用';
		 }
	  }
	};
}

//注册时 检查corp_fullname是否正确 不能重复
function register_checkcorp_fullname(corp_fullname){
	var corp_fullname = corp_fullname.replace(/ /g,"");
	if(corp_fullname == ""){
		//alert("用户昵称不能为空！");
		document.getElementById("checkcorp_fullname").innerHTML = '';
		return false;
	}
	var myReg = /^[^@\/\'\\\"#$%&\^\*。，‘“；：·~’]+$/;
	if(!myReg.test(corp_fullname)){
		document.getElementById("checkcorp_fullname").innerHTML = "企业名称中不能包含特殊字符";
		return false;
	}else{
		document.getElementById("checkcorp_fullname").innerHTML = "";	
	}
	var myReg = /^[()]+$/;
	if(!myReg.test(corp_fullname)){
		corp_fullname = corp_fullname.replace(/\(/g,"（");
		corp_fullname = corp_fullname.replace(/\)/g,"）");
		document.getElementById("corp_fullname").value = corp_fullname;
	}
	corp_fullname=setRSA2(corp_fullname);
	corp_fullname = encodeURIComponent(corp_fullname);
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "corp_fullname="+corp_fullname+"&chkopt=register_checkcorp_fullname";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 if(getMsg == "0"){
			 document.getElementById("checkcorp_fullname").innerHTML = '<img src="/cmdn/res/img/bbs/default/check_right.gif" />';
		 }else if(getMsg == "2"){
		 	 //Add By HeWeihua 2013-4-15
		 	 document.getElementById("checkcorp_fullname").innerHTML = '该企业名称包含敏感字，不能使用';
		 }else{
		 	//alert(getMsg);
			document.getElementById("checkcorp_fullname").innerHTML = '该企业名称已被使用';
		 }
	  }
	};
}

//注册时 检查corp_shortname是否正确 不能重复
function register_checkcorp_shortname(corp_shortname){
	var corp_shortname = corp_shortname.replace(/ /g,"");
	if(corp_shortname == ""){
		//alert("用户昵称不能为空！");
		document.getElementById("checkcorp_shortname").innerHTML = '';
		return false;
	}
	var myReg = /^[^@\/\'\\\"#$%&\^\*。，‘“；：·~’]+$/;
	if(!myReg.test(corp_shortname)){
		document.getElementById("checkcorp_shortname").innerHTML = "企业简称中不能包含特殊字符";
		return false;
	}else{
		document.getElementById("checkcorp_shortname").innerHTML = "";	
	}
	var myReg = /^[()]+$/;
	if(!myReg.test(corp_shortname)){
		corp_shortname = corp_shortname.replace(/\(/g,"（");
		corp_shortname = corp_shortname.replace(/\)/g,"）");
		document.getElementById("corp_shortname").value = corp_shortname;
	}
	corp_shortname = setRSA2(corp_shortname);
	corp_shortname = encodeURIComponent(corp_shortname);
	var ajax = InitAjax();
	var urlRedirect = "/cmdn/bbs/seccode/newdev_ajax.php"; //后台处理页
	var poststring = "corp_shortname="+corp_shortname+"&chkopt=register_checkcorp_shortname";
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		 if(getMsg == "0"){
			 document.getElementById("checkcorp_shortname").innerHTML = '<img src="/cmdn/res/img/bbs/default/check_right.gif" />'
		 }else if(getMsg == "2"){
		 	 //Add By HeWeihua 2013-4-15
		 	 document.getElementById("checkcorp_shortname").innerHTML = '该企业简称包含敏感字，不能使用';
		 }else{
		 	//alert(getMsg);
			document.getElementById("checkcorp_shortname").innerHTML = '该企业简称已被使用';
		 }
	  }
	};
}


/*异步取 头部菜单的 二级分类
	raty:20090616
*/
function getHeadCategory(menu){
	var ajax = InitAjax();
	var urlRedirect = "../supesite/newdev.head_category.php"; //后台处理页
	var poststring = "menu="+menu;
	ajax.open("POST",urlRedirect,true);
	ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	ajax.send(poststring);
	ajax.onreadystatechange = function()
	{
	  if(ajax.readyState == 4 && ajax.status == 200)
	  {
	     var getMsg = ajax.responseText;
		//alert(getMsg);
		clearTimeout(objtimeout); //终止setTimeOut操作
		document.getElementById("head_category").style.display=''; //初始化子导航为显示
		document.getElementById("head_category").innerHTML = getMsg;
	  }
	};
}
function hiddenBar() //隐藏指定ID的导航栏
{
  clearTimeout(objtimeout);
  objtimeout = setTimeout("hiddenBarInfo()",1000); //定时执行，5秒以后隐藏子导航栏	
}
function showBar() //显示子导航
{
  clearTimeout(objtimeout);
  document.getElementById("head_category").style.display='';
}
function hiddenBarInfo() //隐藏导航栏具体操作
{
	document.getElementById("head_category").style.display='none';
}