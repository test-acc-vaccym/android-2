function showSignStatus(id){
	jQuery.ajax({
		   type:'GET',
		   url:'/cmdn/supesite/mmarket/getStatus.php?action=zfwz&lattest_news=login_once',
		   dataType:'json',
		   success:function(data){
			   jQuery('#'+id).html(data.status);
			   jQuery('#js_appstatus').show();
		   },
		   error:function (XMLHttpRequest, textStatus, errorThrown){
			   var err = '签约状态加载失败，请<a href="javascript:showSignStatus(\"sign_status\")">重试</a>';
			   jQuery('#'+id).html(err);
		   }
		   });
}
showSignStatus('sign_status');

jQuery(document).ready(function(){
	
	jQuery.ajax({
		   type:'GET',
		   url:'/cmdn/supesite/mmarket/getSchema.php?action=zfwz&lattest_news=login_once',
		   dataType:'json',
		   success:function(data){			   
			   var total = data.draft+data.testing+data.reject+data.commercial+data.other;
			   			   
			   if(data.reject > 0)
				{	
					   jQuery('#js_reject').html(data.reject);
					   var reject_url = '/cmdn/supesite/newdev.controlpanel.php?operation=EL_REJECT';	
					   jQuery('#reject_link').html('<a href="'+reject_url+'" class="fr">进入处理</a>');					   
				}
				else
				{
					   jQuery('#reject_link').parent().hide();
				}
				if(data.commercial > 0)
				{
					   jQuery('#commercial_link').parent().show();
					   jQuery('#js_commercial').html(data.commercial);
					   var commercial_url = '/cmdn/supesite/newdev.controlpanel.php?operation=EL_COMMERCIAL';	
					   jQuery('#commercial_link').html('<a href="'+commercial_url+'" class="fr">点击查看</a>');					   	
				}
				else
				{
					  jQuery('#commercial_link').parent().hide();
				}				
				if(data.testing > 0)
				{   	
					   jQuery('#js_testing').html(data.testing); 
					   var testing_url = '/cmdn/supesite/newdev.controlpanel.php?operation=EL_TESTING';	
					   jQuery('#testing_link').html('<a href="'+testing_url+'" class="fr">点击查看</a>');					   
				}
				else
				{
					   jQuery('#testing_link').parent().hide();
				}
				
				if(total > 0)
				{
					   jQuery('#js_total').html(total);
					   var total_url = '/cmdn/supesite/newdev.controlpanel.php?operation=EL_ALL';
					   jQuery('#total_link').html('<a href="'+total_url+'" class="link01">查看全部&gt;&gt;</a>');	   
				}
				else
				{
					   jQuery('#cont_total').parent().hide();
					   jQuery('#total_link').hide();
				}
				firstShow();//登录后调用显示我的最新进展层
			 }
		 });	

});

//JavaScript Document
function firstShow(){
    jQuery(".toDoList").show().hover(
		  function () {
			jQuery(this).removeClass("leave");
		  },
		  function () {
			jQuery(this).addClass("leave");
		  }
    );
	jQuery(".toDoBtn").show().addClass("current");
	setTimeout(function(){
		jQuery(".leave").hide();
		if (jQuery(".toDoList").is(":hidden")){
		  jQuery(".toDoBtn").removeClass("current");
		}else{}
	}, 5000);
};

