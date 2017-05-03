// JavaScript Document
var jq = jQuery.noConflict();


function userLogin_submit(){

//jq.blockUI({ message: '<img src="http://devres.mm-img.com/cmdn/supesite/attachments/largeinform_attachments/110325/20110325_4a029633be88acc2aed2234935_33594.gif"/>' });
if(!checkAccount() || !checkPass() ){
  //jq.unblockUI();
  return;
}
if(document.getElementById("seccode") && !checkCsecode())
{
	return;
}
var usermobile = document.getElementById("usermobile").value;
usermobile = usermobile.replace(/ /g,"");
var password = document.getElementById("password").value;
password = password.replace(/ /g,"");

/*var remember = document.getElementById("rememberFlag");
if(remember.checked){
	rflag=1;
}else{
	rflag=0;
}
*/
var rflag=0;
password = setRSA2(password);
usermobile=setRSA2(usermobile);	
document.getElementById("loginbut").disabled=true;
document.getElementById("loginbut").style.backgroundPosition="bottom";
var hash= document.getElementById("hash").value;

//edit by zhang jutning
var data={'usermobile':usermobile,'password':password,'from':'carveout','rememberFlag':rflag,'hash':hash}
if(document.getElementById("seccode"))
{
	var seccode= document.getElementById("seccode").value;
	var data={'usermobile':usermobile,'password':password,'from':'carveout','rememberFlag':rflag,'hash':hash,'seccode':seccode}
}
var currenturl = window.location.href;
var post_url = "/cmdn/supesite/newdev.loginout.php?action=login&refererUrl="+currenturl;
	jq.ajax({
		type:"POST",
		url:post_url,
		data:data,
		success: function(m){
			//jq.unblockUI();
			document.getElementById("loginbut").disabled=false;
            document.getElementById("loginbut").style.backgroundPosition="top";
			m = jq.trim(m);
			
			var b = parseInt(m);
			if(b == '0'){
				var msg=m.substr(m.indexOf(' ') + 1);
				document.getElementById("loginmsg").className='warn';
				document.getElementById("loginmsg").innerHTML=msg;
				changeHeight();
				return;
			}else if(b == 2){
				var uid = parseInt(m.substr(m.indexOf(' ') + 1));
				if(uid>0){
					document.getElementById("loginmsg").className='warn';
					document.getElementById("loginmsg").innerHTML='账号未激活,<a href="javascript:active()" >马上激活</a>';
					document.getElementById("uid").value=uid;
					changeHeight();
					return;
				}
			}
			document.location.reload();			
			return false;
		}
	});
}

function loginenter(eve)
{
	if(document.getElementById("seccode"))
	{
		var seccode= document.getElementById("seccode").value;
		if(seccode.length < 3)
		{
			return false;
		}
	}
	if(eve.keyCode == 13) 
	{
		userLogin_submit();
	}
}

function active(){
	document.getElementById("activeForm").submit();
}

function loginBBS(uid){
	jq.ajax({
		type:"POST",
		url:"/cmdn/bbs/api/uc_loginout.php?uid="+uid+"&action=synlogin&rpage=carveout",
		success: function(m){
			document.location.reload();
		}
	});
}


function checkAccount(){
	var usermobile = document.getElementById("usermobile").value;
	usermobile = usermobile.replace(/ /g,"");
	if(usermobile == "" || usermobile == "请输入Email地址或手机号"){
		document.getElementById("usermobiletext").className='warn';
		document.getElementById("usermobiletext").innerHTML='请输入帐户名！';
		changeHeight();
		return;
	}else{
		document.getElementById("usermobiletext").innerHTML='';
		document.getElementById("usermobiletext").className='';
		changeHeight();
		return true;
	}
}
function checkPass(){
	var password = document.getElementById("password").value;
	password = password.replace(/ /g,"");
	if(password.length<6){
		document.getElementById("textpass").className='warn';
		document.getElementById("textpass").innerHTML='请输入密码,密码不少于六位！';
		changeHeight();
		return;
	}else{
		document.getElementById("textpass").innerHTML='';
		document.getElementById("textpass").className='';
		changeHeight();
		return true;
	}
}

//begin add by zhang junting 2012-02-06
function checkCsecode()
{	
	var seccode = document.getElementById("seccode").value;
	if(seccode.replace(/ /g,"") == ""){
		document.getElementById("showseccodemsg").style.display="block";
		document.getElementById("showseccodemsg").innerHTML='请输入校验码！';
		changeHeight();
		return ;
	}
	var checkvcode = document.getElementById("seccode").innerHTML;
	if(checkvcode == '<font color="#ff0000">请输入正确的验证码</font>'){
		document.getElementById("showseccodemsg").style.display="block";
		document.getElementById("showseccodemsg").innerHTML='请输入正确的效验码！';
		changeHeight();
		return ;
	}
	return true;
	
}
//end add;
//begin add by zhang junting 2012-02-06
function validateVCode(){
	var seccode = document.getElementById("seccode").value;
	seccode = seccode.replace(/ /g,"");
	changeHeight();
	if(seccode.length >= 4){		
		ajax_check_seccode(seccode);
	}
}
//end add;

function checkMobile(){
	var mobile = document.getElementById("msisdn").value;
	mobile = mobile.replace(/ /g,"");

	 if (mobile.length != 11 || (/^\d+$/.test(mobile) == false)) {
		document.getElementById("mobiletext").className='warn';
		document.getElementById("mobiletext").innerHTML='请正确输入手机号码！';
		changeHeight();
		return;
	 }else{
		var f = /^1[3458]\d{9}$/;
        var h = /^13[456789]\d{8}$/;
        var g = /^\+?15\d{9}$/;
        var d = /^134[012345678]\d{7}$/;
        var c = /^\+?18[278]\d{8}$/;
        var b = /^\+?147\d{8}$/;

		if (!f.test(mobile)) {
			document.getElementById("mobiletext").className='warn';
			document.getElementById("mobiletext").innerHTML='您输入的手机号码不正确!';
			changeHeight();
			return;
		}
		 if (h.test(mobile) || g.test(mobile) || d.test(mobile) || c.test(mobile) || b.test(mobile)) {
			document.getElementById("mobiletext").innerHTML='';
			document.getElementById("mobiletext").className='';
			changeHeight();
			return true;
		 }else{
			document.getElementById("mobiletext").className='warn';
			document.getElementById("mobiletext").innerHTML='目前只支持中国移动手机用户!';
			changeHeight();
			return;
		 }

	 }
}

function login_mm(){
if(!checkMobile()){
	return;
}
var mobile = document.getElementById("msisdn").value;
mobile = mobile.replace(/ /g,"");

var i=document.getElementById("logonType").value;
if(i=='mmsms'){
 if(!checksmspwd()){
  return;
 }
 var type='logon';
 var password=document.getElementById("smspwd").value;
 password = password.replace(/ /g,"");
}else{
	if(!checkmmgdpwd()){
	  return;
	}
 var type='mm';
 var password=document.getElementById("mmgdpwd").value;
 password = password.replace(/ /g,"");
}
document.getElementById("loginbut1").disabled=true;
document.getElementById("loginbut1").style.backgroundPosition="bottom";
rsa_mobile = setRSA2(mobile);
mobile = window.mmEnc(mobile);
password = window.mmEnc(password);
jq.ajax({
		type:"POST",
		data:{'type':type,'rsa_mobile':rsa_mobile,'msisdn':mobile,'password':password,'action':'checkLogin'},
		url:"/cmdn/bbs/api/interface.php",
		success: function(m){
			document.getElementById("loginbut1").disabled=false;
            document.getElementById("loginbut1").style.backgroundPosition="top";
			m=jq.trim(m);
			if(m==''){
				document.getElementById("loginmsg2").className='warn';
				document.getElementById("loginmsg2").innerHTML='系统繁忙,请稍后再试!';
				changeHeight();
				return;
			}else if(m!='0'){
				document.getElementById("loginmsg2").className='warn';
				document.getElementById("loginmsg2").innerHTML=m;
				changeHeight();
				return;
			}else{
				login_mm2();
			}
		}
	});

}
function login_mm2(){
	var remember = document.getElementById("cooketime");
	if(remember.checked){
		rflag=1;
	}else{
		rflag=0;
	}
	jq.ajax({
		type:"POST",
		url:"/cmdn/bbs/api/interface.php",
		data:{'ajax':'1','rflag':rflag,'action':'sessionlogin'},
		success: function(m){
			m = jq.trim(m);
			var vArr = m.match(/^[0-9]+$/);
			if (vArr>0){
				loginBBS(m);
			}else{
				document.getElementById("loginmsg2").className='warn';
				document.getElementById("loginmsg2").innerHTML=m;
				changeHeight();
				return;
			}
		}
	});
}
var sec = 60;
var timeout=60;
var timer;
function SmsRandomSend(){
	if(!checkMobile()){
		return;
	}
	if(jq("#time_count").is(":hidden")){
		jq("#time_count").show().addClass("correct");
	}
	
	var flag= document.getElementById("smsflag").value;
	if(flag!='1'){
		return;
	}	
	document.getElementById("smsflag").value='0';
	jq("#times").html(sec);
	timeout = sec;
	timer=setInterval(changeSecond,1000);
	
	document.getElementById("loginmsg2").className='';
	document.getElementById("loginmsg2").innerHTML='';
	
	var mobile = document.getElementById("msisdn").value;
	mobile = mobile.replace(/ /g,"");
	mobile =window.mmEnc(mobile);
	jq.ajax({
		type:"POST",
		data:{'msisdn':mobile,'type':'sendsms','action':'checkLogin'},
		url:"/cmdn/bbs/api/interface.php",
		success: function(m){
			if(m==0){
				document.getElementById("loginmsg2").className='';
				document.getElementById("loginmsg2").innerHTML='';
				changeHeight();
			}else{
				document.getElementById("loginmsg2").className='warn';
				document.getElementById("loginmsg2").innerHTML=m;
				changeHeight();
			}
		}
	});
}

window.mmEnc=function(str){
	
	if(!window.RSAPUBLIC_KEY){
		var mobile = document.getElementById("msisdn").value;
		mobile = mobile.replace(/ /g,"");
		mobile =strEnc(mobile,"1234567","","");
		jq.ajax({
		   async:false,
		   type: "POST",
		   url: "/cmdn/bbs/api/interface.php",
		   data:{'msisdn':mobile,'type':'rasPublicKey','action':'checkLogin'},
		   success: function(m){
			if(m!='0'){
				window.RSAPUBLIC_KEY = m;
			}
		   },
		   error: function(){
		   }
		});
	}
	try {
		return window.RSAEncryption("10001",window.RSAPUBLIC_KEY,str);
	}catch (e) {
		return str;
	}
};
function changeHeight(){
          var msha = jq(".massages:eq(0)").height();
		  var mshb = jq(".massages:eq(1)").height();
		  var mshc = jq(".massages:eq(2)").height();
		  var msgHeight = msha;
		  if(msgHeight < mshb){
			  msgHeight = mshb;
		  };
		  if(msgHeight < mshc){
			  msgHeight = mshc;
		  };
		  var tbBoxHeight = 350;
          jq(".TB_box").height(tbBoxHeight + msgHeight);
		  jq(".logon_button").addClass("relc");
}
function changeSecond(){
	timeout --;
	jq("#times").html(timeout);
	if(timeout < 1){
		document.getElementById("smsflag").value='1';
		clearInterval(timer);
	}
}
function checksmspwd(){
	var smspwd = document.getElementById("smspwd").value;
	smspwd = smspwd.replace(/ /g,"");
	if(smspwd==''){
		document.getElementById("smspwdtext").className='warn';
		document.getElementById("smspwdtext").innerHTML='请输入短信验证码！';
		changeHeight();
		return;
	}else if(smspwd.length!=6){
		document.getElementById("smspwdtext").className='warn';
		document.getElementById("smspwdtext").innerHTML='您输入的短信验证码不正确！';
		changeHeight();
		return;
	}else{
		document.getElementById("smspwdtext").innerHTML='';
		document.getElementById("smspwdtext").className='';
		changeHeight();
		return true;
	}
}

function checkmmgdpwd(){
	var mmgdpwd = document.getElementById("mmgdpwd").value;
	mmgdpwd = mmgdpwd.replace(/ /g,"");
	if(mmgdpwd==''){
		document.getElementById("smspwdtext").className='warn';
		document.getElementById("smspwdtext").innerHTML='请输入固定密码！';
		changeHeight();
		return;
	}else if(mmgdpwd.length<6){
		document.getElementById("smspwdtext").className='warn';
		document.getElementById("smspwdtext").innerHTML='密码最少6位，最多20位！';
		changeHeight();
		return;
	}else{
		document.getElementById("smspwdtext").innerHTML='';
		document.getElementById("smspwdtext").className='';
		changeHeight();
		return true;
	}
}

function user_Login(){
	/* linchuangfeng modify  20140814 BBS上的登录跳转至统一登录页面
	jq('.TB_box').show();
	changeHeight();
	if (jq.browser.version == "6.0"){
	}else{jq('.TB_overlayBG').fadeTo('fast',0.5);
	};*/
	
	var currenturl = window.location.href;
	window.location.href="/cmdn/supesite/newdev.loginout.php?refererUrl="+escape(currenturl);
}
function hiddendiv(){
	jq('.TB_overlayBG').fadeOut('fast');
	jq('.TB_box').hide();
}

function MM_Nick(){
	jq('.TB_overlayBG').fadeTo('fast',0.5);
	jq('.TB_boxNick').show();
}

function hiddendivNick(){
	jq('.TB_overlayBG').fadeOut('fast');
	jq('.TB_boxNick').hide();
}



function hiddencontrolpanel(){
	document.getElementById("controlpanel").style.visibility = "hidden";
}
function hiddenActivities(){
	document.getElementById("activities").style.visibility = "hidden";
}
function chkempty(){
var searchword_h_1 = document.getElementById("searchword_h_1").value;

var reg = /[< >]/;
if(searchword_h_1.replace(/ /g,"") == ""){
  alert("关键字不能为空。");
  document.getElementById("searchword_h_1").focus();
  return false;
}
if(reg.test(searchword_h_1)){
  alert("请不要输入特殊符号。");
  document.getElementById("searchword_h_1").focus();
  return false;
}
  document.getElementById("frmSearch_h_1").submit();
}
function addcollection(){
	var title=document.title;
	var url = window.location.href;
	window.external.addFavorite(url,title);
}
jq(".SubMenu ul>li").hover(
	function () {
		jq(this).addClass("visit");
	},
	function () {
		jq(this).removeClass("visit");
	}
);
jq("#controlpanel").hover(
	function () {
	},
	function () {
		jq(this).css("visibility","hidden");
	}
);
function displayActivities(){
	document.getElementById("activities").style.visibility = "visible";
}
function displaycontrolpanel(){
	document.getElementById("controlpanel").style.visibility = "visible";
}
function getCookie(c_name){
	if (document.cookie.length>0){
		c_start=document.cookie.indexOf(c_name + "=");
		if (c_start!=-1){
			c_start=c_start + c_name.length+1;
			c_end=document.cookie.indexOf(";",c_start);
			if (c_end==-1) c_end=document.cookie.length;
			return unescape(document.cookie.substring(c_start,c_end));
		}
	}
	return "";
}
function setCookie(c_name,value,expireseconds){
	var exdate = new Date();
	exdate.setTime(exdate.getTime() + expireseconds*1000);
	document.cookie = c_name + "=" + escape(value)+	((expireseconds==null) ? "" : "; expires="+exdate.toUTCString());
}
jq(function(){
	var first_load_time = getCookie('first_activity_time');
	if(getCookie('is_first_load') == 'true' && first_load_time != getCookie('first_load_time')) {
		jq("#show_tip_add_integral").floatdiv("middle");
		jq("#show_tip_add_integral").show();
		setCookie('first_load_time',getCookie('first_activity_time'),3600);
		setTimeout('jq("#show_tip_add_integral").fadeOut("slow");',5000);
	}
	jq('#close_tip_intergral').click(function() {
		jq("#show_tip_add_integral").fadeOut("slow");
	});
});

function nicknameenter(eve){
	if(eve.keyCode == 13) {
		changeNickname();
	}
}
function len(s){
	var len = 0;
	var a = s.split("");
	for (var i=0;i<a.length;i++) {
		if (a[i].charCodeAt(0)<299) {
			len++;
		} else {
			len+=2;
		}
	}
	return len;
}

function checkNickname(){
	var nickname = document.getElementById("nickname").value;
	var reg = /^(\w|[\u4E00-\u9FA5])*$/;
	var arr=nickname.match(reg);
	if (len(nickname)<2 || len(nickname)>11) {
		document.getElementById("textNickname").className='warn';
		document.getElementById("textNickname").innerHTML='不符规则！';
		changeHeight();
		return;
	}

	if(arr=nickname.match(reg)){
	}else{
		document.getElementById("textNickname").className='warn';
		document.getElementById("textNickname").innerHTML='不符规则！';
		changeHeight();
		return;
	}

	var bol=false;
	jq.ajax({
		type:"POST",
		url:"/cmdn/bbs/api/interface.php",
		data:{'ajax':'2','nickname':nickname,'action':'checknickname'},
		async:false,
		success: function(m){
			m = jq.trim(m);
			if(m == '1') {
				document.getElementById("textNickname").className='correct';
				document.getElementById("textNickname").innerHTML='';
				 bol=true;
			}else{
				document.getElementById("textNickname").className='warn';
				document.getElementById("textNickname").innerHTML=m;
			}
		}
	});
	return bol;
}

function changeNickname(){
	if(!checkNickname()){
		return;
	}
	var nickname = document.getElementById("nickname").value;
	nickname = nickname.replace(/ /g,"");
	var uid = document.getElementById("mmuid").value;
	jq.ajax({
		type:"POST",
		url:"/cmdn/bbs/api/interface.php",
		data:{'ajax':'2','nickname':nickname,'action':'updatenickname','uid':uid},
		async:false,
		success: function(m){
			document.location.reload();
		}
	});
}

jq(function(){
    if(jq(".TB_box").offset() != null){
    var menuYloc = jq(".TB_box").offset().top;
    jq(window).scroll(function() {
		var bh = jq("body").height();
		jq(".TB_overlayBG").css({width:"100%",height:bh});
        var offsetTop =  document.documentElement.clientHeight/2 + jq(window).scrollTop();
		if(jq(".TB_box").is(":visible") && offsetTop<351) {offsetTop=351 ;}
        jq(".TB_box").animate({
            top: offsetTop
        },
        {
            duration: 500,
            queue: false
        });
    });
	jq(window).resize(function(){
		offsetTop = jq(window).scrollTop() + document.documentElement.clientHeight/2;
		if(offsetTop<351) {offsetTop=351 ;};
        jq(".TB_box").animate({
            top: offsetTop
        },
        {
            duration: 500,
            queue: false
        });
	});
	};

    if(jq(".TB_boxNick").offset() != null){
    var menuYloc = jq(".TB_boxNick").offset().top;
    jq(window).scroll(function() {
		bh = jq("body").height();
		jq(".TB_overlayBG").css({width:"100%",height:bh});
        var offsetTop =  document.documentElement.clientHeight/2 + jq(window).scrollTop();
		if(jq(".TB_boxNick").is(":visible") && offsetTop<351) {offsetTop=351 ;};
        jq(".TB_boxNick").animate({
            top: offsetTop
        },
        {
            duration: 500,
            queue: false
        });
    });
	jq(window).resize(function(){
	    offsetTop = jq(window).scrollTop() + document.documentElement.clientHeight/2;
		if(offsetTop<351) {offsetTop=351 ;};
        jq(".TB_boxNick").animate({
            top: offsetTop
        },
        {
            duration: 500,
            queue: false
        });
	});
	};

});
jq(function($) {
//图片切换
/**jquery.featureList-1.0.0.js
		$.featureList(
			$(".imgList li"),
			$(".bigImg a"), {
					start_item	:	0
			}
		);
*/
//标签切换
		tab = function(_tab,content,hoverclass)
       {
	       _tab.click(function(){
				var index = _tab.index($(this));
				_tab.removeClass();
				$(this).addClass("selected");
				content.hide();
				$(content.get(index)).show();
				changeHeight();
				});
	       _tab.mouseout(function(){$(this).removeClass(hoverclass)});

      };
       var mmSignIn_tab = new tab($('.mmSignWrap .siHead ul li'),$('.mmSignWrap .siCon>div'),'hover');
       var mmPw_tab = new tab($('.pwTab li'),$('.pwCon>li'),'hover');
//密码类型切换
      $("#mmsms").click(function(){
			document.getElementById("logonType").value='mmsms';
			document.getElementById("smspwdtext").innerHTML='';
			document.getElementById("smspwdtext").className='';
			SmsRandomSend();			
	  });
	  $("#mmgdmm").click(function(){
			document.getElementById("logonType").value='mmgdmm';
		    if($("#time_count").is(":visible")){
			  $("#time_count").hide().removeClass("correct");
			}
			document.getElementById("loginmsg2").className='';
			document.getElementById("loginmsg2").innerHTML='';
			changeHeight();
	  });
//输入框状态切换
      $("#msisdn").focus(function(){
			var txtValue = $(this).val();
			if (txtValue == "请输入中国移动手机号"){
			    $(this).val("").addClass("gfocus");
			}
	  });
	  $("#msisdn").blur(function(){
			checkMobile();
			var txtValue = $(this).val();
			if (txtValue == ""){
			    $(this).val("请输入中国移动手机号").removeClass("gfocus");
			};
	  });
	  $("#mmgdpwd").focus(function(){
			$(this).addClass("gfocus");
	  });
	   $("#mmgdpwd").blur(function(){
			checkmmgdpwd();
			$(this).removeClass("gfocus");
	  });
	   $("#smspwd").focus(function(){
			$(this).addClass("gfocus");
	  });
	   $("#smspwd").blur(function(){
			checksmspwd();
			$(this).removeClass("gfocus");
	  });
//其他用户
	  $("#msimn").focus(function(){
			var txtValue = $(this).val();
			if (txtValue == "请输入您的用户名"){
			    $(this).val("").addClass("gfocus");
			}
	  });
	  $("#msimn").blur(function(){
			//缺少检查
			var txtValue = $(this).val();
			if (txtValue == ""){
			    $(this).val("请输入您的用户名").removeClass("gfocus");
			}
	  });
	   $("#msimnpw").focus(function(){
			$(this).addClass("gfocus");
	  });
	   $("#msimnpw").blur(function(){
			//缺少检查
			$(this).removeClass("gfocus");
	  });
//社区用户登录
	  $("#usermobile").focus(function(){
			var txtValue = $(this).val();
			if (txtValue == "请输入Email地址或手机号"){
			    $(this).val("").addClass("gfocus");
			}
	  });
	  $("#usermobile").blur(function(){
			checkAccount();
			var txtValue = $(this).val();
			if (txtValue == ""){
			    $(this).val("请输入Email地址或手机号").removeClass("gfocus");
			}
	  });
	   $("#password").focus(function(){
			$(this).addClass("gfocus");
	  });
	   $("#password").blur(function(){
			checkPass();
			$(this).removeClass("gfocus");
	  });
//登录按钮状态
	  $(".logon_button :button").hover(function(){$(this).addClass("btn_h");},function(){$(this).removeClass("btn_d").removeClass("btn_h");})
	  .mousedown(function(){$(this).addClass("btn_d")}).mouseup(function(){$(this).removeClass("btn_d").addClass("btn_h")});
});

function login_mmuser(){
	var msimn =jq.trim(document.getElementById("msimn").value); 
	if(msimn == "" || msimn == "请输入您的用户名"){
		document.getElementById("loginmsg3").className='warn';
		document.getElementById("loginmsg3").innerHTML='请输入您的用户名！';
		return;
	}
	var msimnpw =jq.trim(document.getElementById("msimnpw").value);
	msimnpw =msimnpw.replace(/ /g,"");
	if(msimnpw.length<6){
		document.getElementById("loginmsg3").className='warn';
		document.getElementById("loginmsg3").innerHTML='请输入密码,密码不少于六位！';
		return;
	}
	
	document.getElementById("loginmsg3").className='warn';
	document.getElementById("loginmsg3").innerHTML='接口未完成..待续..';
	return;
}
// document.write('<scr'+'ipt src=/cmdn/res/js/logic/tongji.js></scr'+'ipt>');