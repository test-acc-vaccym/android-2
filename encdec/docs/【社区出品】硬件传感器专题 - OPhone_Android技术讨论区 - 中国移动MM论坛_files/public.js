/*Last Edit: 121112 by immelon*/
(function($){
    $.fn.hoverDelay = function(options){
        var defaults = {
            hoverDuring: 200,
            outDuring: 200,
            hoverEvent: function(){
                $.noop();//这个函数表示什么也不做
            },
            outEvent: function(){
                $.noop();//这个函数表示什么也不做
            }
        };
        var sets = $.extend(defaults,options || {});
        var hoverTimer, outTimer;
        return $(this).each(function(){
        	var t = this;
            $(this).hover(function(){
                clearTimeout(outTimer);
                hoverTimer = setTimeout(sets.hoverEvent, sets.hoverDuring);
                hoverTimer = setTimeout(function(){sets.hoverEvent.apply(t);}, sets.hoverDuring);
            },function(){
                clearTimeout(hoverTimer);
                outTimer = setTimeout(sets.outEvent, sets.outDuring);
                outTimer = setTimeout(function(){sets.outEvent.apply(t);}, sets.outDuring);
            });
        });
    };
})(jQuery);

jQuery(function($) {
/*hover*/
   devindexhover = function(_hover,hoverclass)
	{
		_hover.mouseover(function(){$(this).addClass(hoverclass)});
		_hover.mouseout(function(){$(this).removeClass(hoverclass)});
	}

/*dropDownMenu*/
   $(".drop-arrow").hoverDelay({
		hoverDuring:200,
		outDuring:200,
		hoverEvent:function(){
			$(".drop-arrow ul").hide();
			$(".drop-arrow").removeClass('hover');
			$(this).children('ul').show();
			$(this).addClass('hover');
			return false;
		},
		outEvent:function(){
			$(this).children('ul').hide();
			$(this).removeClass('hover');
			return false;
		}
	});
	
	$('#show').on("hover",function(){ //鼠标移动函数
		$('.right-hidden-menu').slideDown(); //找到显示

		$('.right-hidden-menu').hover(function(){},
			function(){
				$('.right-hidden-menu').slideUp();
			}
		);
	});

	$('.right-hidden-menu a').on("click",function(){
		$('.right-hidden-menu').slideUp();
	});
	
	$("#searchType").click(function(){
		if($("#searchTypeList").is(":hidden")){
			$("#searchTypeList").show();
			$("#searchType").addClass("sel");
			$("#setFocus").focus().blur(function(){
				setTimeout(function(){$("#searchTypeList").hide();$("#searchType").removeClass("sel");},200);
			});
			$("#searchTypeList a").click(function(){
				$("#searchType").text($(this).text()).attr("rel",$(this).attr("rel"));
				$("#searchTypeList").hide();
				$("#searchType").removeClass("sel");
				return false;
			})
		}
		return false;			
	});
});
/*tab*/
var tab=function(oT,oC,t){
	var a=setTimeout(function(){
		$(oT).bind(t,function(){
			$(oT).removeClass("current");
			$(oC).hide();
			$(this).addClass("current");
			$(oC).eq($(this).index()).show();
		})
	},200);
	clearTimeout(this.a);
}


//隔行变色
//说明：obj对象 
//      t1为颜色值1
//      t2为颜色值2
//      t3为鼠标经过的颜色值
//      n为开始行从0开始
function eoddTble(obj,t1,t2,t3,n){
   	var o=$(obj);
	var m=0;
	var tempBg;
	if(n!=""){m=n;}
	for (i=m;i<o.length;i++) { 
		var bgc= (i%2==0)? t1:t2; 
		o.eq(i).css("background-color",bgc); 
	}
	o.hover(function(){
			tempBg=$(this).css("background-color");
			$(this).css("background-color",t3);
		},function(){
			$(this).css("background-color",tempBg);
	});
}

/*jquery输入框提示信息*/
function inputTipText(obj){  //所有样式名中含有grayTips的input    
	$(obj).each(function(){   
		var oldVal=$(this).val();   
		$(this).css({"color":"#999"}).focus(function(){   
			if($(this).val()!=oldVal){$(this).css({"color":"#333"})}else{$(this).val("").css({"color":"#999"})}   
		}).blur(function(){   
			if($(this).val()==""){$(this).val(oldVal).css({"color":"#999"})}   
		})   
		.keydown(function(){$(this).css({"color":"#333"})})   
	})   
}

function inputTipTextPWD(obj){
	$(obj).each(function(){ 
		var oldVal=$(this).val();
		$(this).focus(function(){
			if($(this).val() != oldVal) return;
			var that=$(this);
			setTimeout(function(){
				that.hide().next().show().focus().val("");
			},200);
		});	
		$(this).next().blur(function(){
			if($(this).val() != "") return;
			$(this).hide();
			$(this).prev().show().val(oldVal);	
		});
	});
}
