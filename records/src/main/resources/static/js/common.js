/**
 * DEV COMPANY : THE LOCA 2018 author: 장훈동 since:2018. 05. 31
 */

(function($) {

	$.comm = {

	}

	$.extend(true, $.comm, {

		/*
		 * Team List 조회
		 */
		getTeamList : function(callBack) {
			//snb team list
			fn.get('/team/list', null, function(obj){
				var list = obj.payload;
				if (typeof (callBack) == 'function') {
					callBack(list);
				}
			});
		}

		/*
		 * 페이지별 소속 snb Open 처리
		 */
		, getSnb : function(){
			var pathname = location.pathname;

			$('#accordionSidebar .nav-item').removeClass('active');

			if(pathname.indexOf('/main/index') > -1){	//Dashboard
				$('#accordionSidebar .nav-item').eq(0).addClass('active');
			}else if(pathname.indexOf('/team') > -1){	//Team
				var idx = parseInt(location.pathname.substring(6));
				$('#accordionSidebar .nav-item').eq(1).addClass('active');
				$('#accordionSidebar .nav-item').eq(1).find('.nav-link').removeClass('collapsed');
				$('#collapseTeam').addClass('show');
				$('#accordionSidebar .nav-item').eq(1).find('div').eq(1).find('a').eq(idx - 1).addClass('active');
			}

		}

	});



})($);