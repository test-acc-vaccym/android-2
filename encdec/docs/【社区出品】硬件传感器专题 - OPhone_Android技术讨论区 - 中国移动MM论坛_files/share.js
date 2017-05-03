// JavaScript Document
// 150814im
function closeTB(target){
	jQuery(target).hide();
	jQuery(".ttsBtns .btn").removeClass("current");
};
jQuery(document).ready(function($){
	var agent = navigator.userAgent.toLowerCase(); 

	if(agent.indexOf("msie 6.0") != -1){
		var t1 = $(window).height() - 130;
		$(".toTopAndShare").css({"top": t1});
		$([window, document.body]).bind("scroll",function(e){
			$(".toTopAndShare").css({"top": t1 + $(document).scrollTop()});
		});
	}else{
		$(".toTopAndShare").css({"bottom": 80});
	}
	
	$(".shareBtn").click(function(){
		$(".toDoBtn").removeClass("current");
		$(".followBtn").removeClass("current");
		if($(this).hasClass("current")){
			$(".share").hide();
			$(this).removeClass("current");
		}else{
			$(".share").show();
			$(".toDoList").hide();
			$(".follow").hide();
			$(this).addClass("current");
		}
	});	
	$(".followBtn").click(function(){
		$(".toDoBtn").removeClass("current");
		$(".shareBtn").removeClass("current");
		if($(this).hasClass("current")){
			$(".follow").hide();
			$(this).removeClass("current");
		}else{
			$(".follow").show();
			$(".toDoList").hide();
			$(".share").hide();
			$(this).addClass("current");
		}
	});	
	$(".toDoBtn").click(function(){
		$(".shareBtn").removeClass("current");
		$(".followBtn").removeClass("current");
		if($(this).hasClass("current")){
			$(".toDoList").hide();
			$(this).removeClass("current");
		}else{
			$(".toDoList").show();
			$(".share").hide();
			$(".follow").hide();
			$(this).addClass("current");
		}
	});
	
	//滚动到顶
	$(".toTopBtn").hide();
	$(window).scroll(function () {
			if ($(this).scrollTop() > 150) {
				$('.toTopBtn').fadeIn();
			} else {
				$('.toTopBtn').fadeOut();
			}
		});
		$('.toTopBtn').click(function () {
			$('body,html').animate({
				scrollTop: 0
			}, 800);
			return false;
	});

});



function copyToClipBoard(id){
	var agent = navigator.userAgent.toLowerCase(); 
	if(agent.indexOf("msie") != -1){
		var clipBoardContent="";
		clipBoardContent += jQuery('#'+id).val();
		window.clipboardData.setData("Text", clipBoardContent);
		window.
		alert("复制网址成功!");
	}else{
		jQuery('#'+id).select();
		alert("你所使用的是非IE核心浏览器，请使用Ctrl+C复制代码到剪贴板。");
	}
};
var SNS ={};
/*
* @webId 分享到的网站id
* @url 分享的网页，可选
* @title 分享的文字，可选
*/
SNS.Share = function(webId, url, title){
	var baseUrl = "http://www.jiathis.com/send/";
	if(webId){
		var param = {};
		param.webid = webId;
		param.url = url||location.href;
		param.title = title||document.title;
		if(webId=='139'){
			var sha139url = 'http://talk.shequ.10086.cn/apps/vshare/share.php';
			sha139url    +='?app_key=deb63346facfead18b74ca50c09f142d';
			sha139url    +='&title='+encodeURIComponent(param.title);
			sha139url    +='&url='  +encodeURIComponent(param.url);
			//window.open(sha139url,'_blank','width=650,height=550');
			window.open("/portal/web/sync139Login.do?tourlencode=true&tourl="+encodeURIComponent(sha139url),'_blank','width=650,height=550');
		}else if(webId=="feixin"){
			baseUrl='http://i.feixin.10086.cn/app/api/share';
			var api = baseUrl+"?url="+encodeURIComponent(param.url)+"&title="+encodeURIComponent(param.title);
			window.open('/portal/web/fetionLoginAction.do?tourlencode=true&tourl='+encodeURIComponent(api));
			//window.open(api);
		}else{
			var api = baseUrl+"?webid="+param.webid+"&url="+param.url+"&title="+param.title;
			window.open(api);
		}
	};
	
};
/*
谷歌	google
百度搜藏	baidu
QQ收藏	qq
豆瓣	douban
开心网	kaixin001
和讯	hexun
新浪vivi	sina
微软live	live
雅虎收藏	yahoo
delicious	delicious
digg	digg
facebook	fb
twitter	twitter
饭否	fanfou
抓虾	zhuaxia
鲜果	xianguo
Mister-Wong	wong
POCO网	poco
乐收	leshou
奇客发现	diglog
挖客网	waakee
收客网	shouker
就喜欢	9fav
myspace	myspace
114啦	114la
搜狐白社会	sohu
GMAIL邮箱	gmail
Hotmail邮箱	hotmail
DIIGO.com	diigo
EverNote笔记	evernote
FriendFeed	friendfeed
Funp.com	funp
LinkedIn(商务)	linkedin
NetLog网志	netlog
Netvibes.com	netvibes
PDF在线转换	pdfonline
Plurk.com	plurk
Phonefavs 手机收藏	phonefavs
ping.fm	pingfm
Plaxo.com	plaxo
PollAdium 投票	polladium
printfriendly - 友好打印	printfriendly
Reddit 收藏	reddit
Stumbleupon	stumbleupon
英文翻译	translate
Yahoo! mail	ymail
新浪微博	tsina
同学网	tongxue
人人网	renren
嘀咕网	digu
115收藏	115
Follow5	follow5
淘江湖	taobao
人间网	renjian
做啥	zuosa
邮件	email
MIXX	mixx
谷歌Buzz	buzz
51社区	51
百度空间	hi
QQ空间	qzone
百度贴吧	tieba
创业邦	cyzone
网易微博	t163
139说客	139
若邻网	wealink
中金微博	cnfol
搜狐微博	tsohu
好诶	haoei
易集网	yijee
飞信	feixin
抽屉网	chouti
递客网	dig24
豆瓣9点	douban9dian
黑米书签	hemidemi
宝盒网	baohe
谷歌阅读器	googlereader
myshare	myshare
fwisp	fwisp
腾讯微博	tqq
有道书签	youdao
救救地球	99earth
腾讯朋友	xiaoyou
优士网	youshi
MSN	msn
梦幻人生	dream163
和讯微博	thexun
Bit.ly	bitly
凤凰微博	tifeng
w3c验证	w3c
雷猴	leihou
天涯社区	tianya
新浪博客	blogsina
我烧网	woshao
赶牛网	ganniu
聚友网	myspacecn
天翼社区	189cn
玛撒网	masar
人脉库	renmaiku
猫扑推客	mop
粉丝网	ifensi
青6网	qing6
Instapaper	instapaper
Read It Later	readitlater
189邮箱	189mail
复制网址	copy
收藏夹	fav
打印	print

*/