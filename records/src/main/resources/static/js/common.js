/**
 * DEV COMPANY : THE LOCA 2018 author: 장훈동 since:2018. 05. 31
 */

(function($) {

	$.comm = {
		toggle : 0	//toggle off
	}

	$.extend(true, $.comm, {
		/**
		 * null체크
		 */
		isNull : function(val) {
			val = $.trim(val);
		    if( val === "" || val == null || val == 'undefined' || val == undefined || ( val != null && typeof val == "object" && !Object.keys(val).length ) ){
		    	return true;
		    }else{
		    	return false;
		    }
		},
		/**
		 * url parameter 추출
		 */
		getUrlParameter : function(name, defaultValue) {
		    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
		    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
		    var results = regex.exec(location.search);
		    if(results === null){
		    	if(typeof(defaultValue) == 'undefined'){
		    		return '';
		    	}
		    	else{
		    		return defaultValue;
		    	}
		    }
		    else{
		    	return decodeURIComponent(results[1].replace(/\+/g, ' '));
		    }
		},


		/*	신규 작성	*/
		/**
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
		},
		/**
		 * 페이지별 소속 snb Open 처리
		 */
		getSnb : function(){
			var pathname = location.pathname;
			var idx = 0;
			var teamSeqNo = this.getUrlParameter('teamSeqNo', 0);

			//전체 active 클래스 제거
			$('#accordionSidebar .nav-item').removeClass('active');

			//pathname별 처리
			if(pathname.indexOf('/main/index') > -1){	//Dashboard

			}else if(pathname.indexOf('/player') > -1){	//Team Players
				idx = 0;
			}else if(pathname.indexOf('/records') > -1){	//Team Records
				idx = 1;
			}else if(pathname.indexOf('/team') > -1){	//Team
				idx = 2;
			}

			console.log(idx);
			$('#accordionSidebar .nav-item').eq(idx).addClass('active');

			//Dashboard를 제외한 SNB sub navigation active class 추가
			if(teamSeqNo > 0){
				$('.collapseTeam' + teamSeqNo).addClass('active');
				$('.collapseTeam' + teamSeqNo).find('.nav-link').removeClass('collapsed');
				$('#collapseTeam' + teamSeqNo).addClass('show');

				$('.team' + teamSeqNo).find('a').eq(idx).addClass('active');
			}

			//TODO : 토글 처리
			$.comm.toggle;
		},
		/**
		 * 토글 클릭
		 */
		onClickToggle : function(){
			if($('body').hasClass('sidebar-toggled')){
				$.comm.toggle = 0;
			}else{
				$.comm.toggle = 1;
			}
		}

	});



})($);