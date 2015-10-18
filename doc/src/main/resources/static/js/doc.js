$(function() {
	var $fixtop = $("nav.navbar-fixed-top");
	var $content = $("div.content");
	var fixTopOffset = 0;
	if ($fixtop.length > 0 && $content.length > 0) {
		fixTopOffset = $fixtop.outerHeight();
		$content.css("margin-top", fixTopOffset + "px");
	}

	win$.init({top: fixTopOffset, delay: false});

	$('body').scrollspy({
		target : '#docindex',
		offset : fixTopOffset
	});

	$('a.scroll').each(function() {
		try {
			var $goto = $($(this).attr('href'));
			if ($goto.length > 0) {
				$(this).bind('click', function(event) {
					var scrollTop = $goto.offset().top - fixTopOffset + 1;

					$('html, body').stop().animate({
						scrollTop : scrollTop
					}, 500, 'easeInOutExpo');
					event.preventDefault();
				});
			}
		} catch (e) {
		}
	});

	var scrollVisible = function($toScroll, $scrollScope) {
	  return function() {
	    var posi = win$.getPositionInViewport($toScroll);
	    var contentPosi = win$.getPositionInViewport($scrollScope);

	    if (posi.bottom <= contentPosi.top) { // 窄屏幕，上下布局，不移动
	      $toScroll.css("margin-top", "0px");
	      return;
	    }

	    var top = 0;
	    try {
	      top = parseInt($toScroll.css("margin-top").match(/\d+/)[0]);
	    } catch (e) {}

	    var maxBottom = contentPosi.bottom;
	    var move;

	    if (posi.bottom >= posi.viewport.height && posi.top <= 0)
	      return;

	    if (posi.top < 0 && posi.bottom < posi.viewport.height) { // 顶部和底部都偏移，移动
	      var off1 = -posi.top;
	      var off2 = posi.viewport.height - posi.bottom;
	      move = Math.abs(off1) > Math.abs(off2) ? off2 : off1;
	    } else if (posi.top > 0) {
	      move = -posi.top;
	    }

	    if (posi.bottom + move > maxBottom)
	      move = maxBottom - posi.bottom;

	    move += top;

	    if (move < 0)
	      $toScroll.css("margin-top", "0px");
	    else
	      $toScroll.css("margin-top", move + "px");
	  }
	};
	
	var scrollFunc = scrollVisible($("#docindex"), $("div#content"));
	$(window).scroll(scrollFunc);
	scrollFunc();
});
