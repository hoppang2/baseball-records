'use strict';

$(function () {
	//검색 이벤트 부여
	$("#searchValue").keyup(function(key) {
		if (key.keyCode == 13) {
			$.comm.search(1, null);
		}
	});

	//snb team list
	$.comm.getTeamList(function(list){
		var _html = '';
		for(var i = 0; i < list.length; i++){
			_html += '<a class="collapse-item" href="/team/' + list[i].seqNo + '">' + list[i].teamNm + '&nbsp;' + list[i].teamNmSub + '</a>'
		}

		$(".teamList").append(_html);
		$.comm.getSnb();
	});

});

var CONTEXT_PATH = '';
let fn = {pageIfIdx: 0};
fn.__endpoint = '/';
fn.__printLog = true;
fn.__lock = false;

fn.alertJson = function (obj) {
    alert(JSON.stringify(obj));
};



fn.toDateStr = function (dt, format) {
    if (dt == null) {
        return '-';
    }
    return (new Date(dt)).toFormattedString(format);
};



fn.getFormData = function (formId) {
    var unindexed_array = $('#' + formId).serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
};

fn.toSearchUrl = function (formId) {

    var url = convertQueryString('', 'cPage', 1);
    var tmpQuery = $('#' + formId).serialize();

    if (tmpQuery != "") {
        var tmpArr = tmpQuery.split("&"); // & 배열로 나눈다.
        var ei = 0;
        for (var i = 0; i < tmpArr.length; i++) {
            var tmpArr2 = tmpArr[i].split("="); // = 배열나누기
            if (tmpArr2.length == 2) {
                url = convertQueryString(url, tmpArr2[0], tmpArr2[1]);
            }
        }
    }

    return url;
};

fn.logout = function () {
//	if(!confirm('로그아웃 하시겠습니까?')){
//		return;
//	}
    location.href = CONTEXT_PATH + '/logout';
};


fn.validPW = function (pw) {
    var pw_passed = true;

    var pattern1 = /[0-9]/;
    var pattern2 = /[a-zA-Z]/;
    var pattern3 = /[~!@\#$%<>^&*]/; // 원하는 특수문자 추가 제거

    var pw_msg = "";

    if (pw.length == 0) {
        alert("비밀번호를 입력해주세요");
        return false;
    }

    if (!pattern1.test(pw) || !pattern2.test(pw) || !pattern3.test(pw)
        || pw.length < 8 || pw.length > 50) {
        alert(Languages['pwRule']);
        return false;
    }

    var SamePass_0 = 0; // 동일문자 카운트
    var SamePass_1 = 0; // 연속성(+) 카운드
    var SamePass_2 = 0; // 연속성(-) 카운드

    for (var i = 0; i < pw.length; i++) {
        var chr_pass_0;
        var chr_pass_1;
        var chr_pass_2;

        if (i >= 2) {
            chr_pass_0 = pw.charCodeAt(i - 2);
            chr_pass_1 = pw.charCodeAt(i - 1);
            chr_pass_2 = pw.charCodeAt(i);

            // 동일문자 카운트
            if ((chr_pass_0 == chr_pass_1) && (chr_pass_1 == chr_pass_2)) {
                SamePass_0++;
            } else {
                SamePass_0 = 0;
            }

            // 연속성(+) 카운드
            if (chr_pass_0 - chr_pass_1 == 1 && chr_pass_1 - chr_pass_2 == 1) {
                SamePass_1++;
            } else {
                SamePass_1 = 0;
            }

            // 연속성(-) 카운드
            if (chr_pass_0 - chr_pass_1 == -1 && chr_pass_1 - chr_pass_2 == -1) {

                SamePass_2++;
            } else {
                SamePass_2 = 0;
            }
        }

        if (SamePass_0 > 0) {
            alert(Languages['msgNotSame']);
            pw_passed = false;

        }

        if (SamePass_1 > 0 || SamePass_2 > 0) {
            alert(Languages['msgNotNum']);
            pw_passed = false;

        }

        if (!pw_passed) {

            return false;
            break;
        }
    }
    return true;

};

fn.ymdToStr = function (ymd, format) {
    if (!ymd) {
        return '-';
    }
    if (ymd == null) {
        return '-';
    }
    // 2019 03 11 00 11 30
// var yyyy = ymd.substring(0, 4);
// var MM = ymd.substring(4, 6);
// var dd = ymd.substring(6, 8);
// var HH = ymd.substring(8, 10);
// var mm = ymd.substring(10, 12);
// var ss = ymd.substring(12, 14);
    return ymd.substring(0, 4) + '-' + ymd.substring(4, 6) + '-' + ymd.substring(6, 8) + ' ' + ymd.substring(8, 10) + ':' + ymd.substring(10, 12);
};

fn.ifN = function (val, replceValue) {

    if (typeof (replceValue) == 'undefined') {
        replceValue = '';
    }

    if (val) {
        if (val == null) {
            return replceValue;
        }

        val = val.toString();
        if (val.trim() == '') {
            return replceValue;
        } else {
            return val;
        }
    }
    return replceValue;
};


// 시간 추출
fn.toHour = function (min) {
    var hour = parseInt(min / 60);

    if (hour != 24) {
        hour = hour % 24;
    }

    return hour.toString().padStart(2, '0');
};

// 전체 분에서 분 값만 추출
fn.toMin = function (min) {
    return (min % 60).toString().padStart(2, '0');
};


// 분을 HH:mm 형태로 반환
fn.minToTime = function (min) {
    return fn.toHour(min) + ':' + fn.toMin(min);
};

fn.toMoney = function (val) {
    if (val == null) {
        return '0'
    }
    var num = val.toString().toNum();
    var pattern = /(-?[0-9]+)([0-9]{3})/;
    while (pattern.test(num)) {
        num = num.replace(pattern, "$1,$2");
    }
    return num;
};

fn.get = function (resource, data, callback, async) {
    this.__callApi(resource, "GET", data, callback, async);
};

fn.post = function (resource, data, callback, async) {
    this.__callApi(resource, "POST", data, callback, async);
};

fn.__callApi = function (resource, type, data, callback, async) {

    if (this.__lock) {
        console.warn("API IS LOCK")
        return;
    }

    let asyncOpt = true;
    if (typeof async == "boolean") {
        asyncOpt = async;
    }

    let reqIdx = this.pageIfIdx;

    let apiUrl = this.__endpoint;
    if (resource.indexOf('/') == 0) {
        apiUrl += resource.slice(1);
    } else {
        apiUrl += resource;
    }

    let reqUrl = (resource.indexOf('http') == 0 ? resource : apiUrl);

    if (this.__printLog) {
        console.info('--------------------------------------');
        console.info('send : [' + this.pageIfIdx + '][' + reqUrl + ']');
        console.info('type : ' + type);
        console.info('data :', data);
        console.info('///////////////////////////////////////');
    }

    let callTime = (new Date()).getTime();
    let ajaxOpt = {
        url: reqUrl,
        crossDomain: true,
        type: type,
        dataType: "json",
        async: asyncOpt,
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {

        },
        success: function (rtn, status, req) {

            if (fn.__lock) {
                console.warn("API IS LOCK")
                return;
            }

            if (fn.__printLog) {
                console.info('recv : [' + reqIdx + '][' + ((new Date()).getTime() - callTime).toString().toMoney() + ' ms][' + reqUrl + ']', rtn);
            }


            if (!rtn.success) {
            }

            if (callback != undefined) {
                callback(rtn);
            }
        },
        error: function (xhr, status, e) {

            if (fn.__lock) {
                console.warn("API IS LOCK")
                return;
            }
            fn.__lock = true;

            if (fn.__printLog) {
                console.info('recv : [' + reqIdx + '][' + ((new Date()).getTime() - callTime).toString().toMoney() + ' ms]');
            }

            if (xhr.status == 403) {
                console.info('session expired : [' + reqIdx + ']');
                alert('세션이 만료되었습니다.\n다시 로그인해주세요.');
                alert('세션 만료 처리');
            } else {
                if (status == 'error' && xhr.readyState == 0 && xhr.status == 0) {
                    alert('서버와의 통신이 원활하지 않습니다.');
                    location.reload();
                    return;
                }

                console.error(xhr, status, e);
                alert("죄송합니다.\n오류가 발생하였습니다.\n관리자 확인 후 조치하겠습니다.");
            }
        }
    }
    if (type == 'POST' || type == 'PUT') {
        ajaxOpt.data = JSON.stringify(data);
    } else {
        ajaxOpt.data = data;
    }

    return $.ajax(ajaxOpt);


};

fn.onlyNumber = function(val){
	return val.replace(/[^0-9]/g,"");
};


// 쿼리스트링 변환
function convertPageURL(page, pageIdx) {
    var PathName = "";
    var tmpQuery = "";

    var PathNameArr = location.pathname.split("/");

    for (var i = 0; i < PathNameArr.length - 1; i++) {
        if (PathNameArr[i] != "") {
            PathName += "/" + PathNameArr[i];
        }
    }

    PathName += "/" + page;

    if (pageIdx !== undefined) {
        PathName += "/" + pageIdx;
    }

    tmpQuery = location.search.toString();

    if (tmpQuery != "") {
        PathName += tmpQuery;
    }

    return PathName;
}

function convertQueryString(tURL, query, value) {
    var PathName = "";
    var tmpQuery = "";
    if (tURL === undefined || tURL == null || tURL == "") {
        PathName = location.pathname;
        tmpQuery = location.search.toString();
        if (tmpQuery != "") {
            tmpQuery = tmpQuery.slice(1);
        }
    } else {
        if (tURL.indexOf("?") == -1) {
            PathName = tURL;
        } else {
            PathName = tURL.substr(0, tURL.indexOf("?"));
            tmpQuery = tURL.substr(tURL.indexOf("?") + 1, tURL.length);
        }
    }

    var QueryArray = new Array();

    if (tmpQuery != "") {
        var tmpArr = tmpQuery.split("&"); // & 배열로 나눈다.
        var ei = 0;
        for (var i = 0; i < tmpArr.length; i++) {
            var tmpArr2 = tmpArr[i].split("="); // = 배열나누기
            if (tmpArr2.length == 2) {
                if (tmpArr2[0].toLowerCase() != query.toLowerCase()) {
                    QueryArray[ei] = new Array();
                    QueryArray[ei][0] = tmpArr2[0];
                    QueryArray[ei][1] = tmpArr2[1];
                    ei++;
                }
            }
        }
    }

    var findQuery = true;
    if (typeof (query) == "undefined" || query == null || typeof (value) == undefined || value == null) {
        findQuery = false;
    } else {
        query = query.toString();
        value = value.toString();
    }

    if (findQuery && query != "" && value != "") {
        var lastIndex = QueryArray.length;
        QueryArray[lastIndex] = new Array();
        QueryArray[lastIndex][0] = query;
        QueryArray[lastIndex][1] = value;
    }

    PathName += "?";

    for (var i = 0; i < QueryArray.length; i++) {
        if (i != 0) {
            PathName += "&";
        }
        PathName += QueryArray[i][0] + "=" + QueryArray[i][1];
    }
    return PathName;
}

function getQueryValue(query, value, defaultValue) {

    value = value + "=";
    var startIdx = query.indexOf(value);

    if (startIdx == -1) {
        if (defaultValue !== undefined) {
            return defaultValue;
        } else {
            return "";
        }
    }

    startIdx = startIdx + value.length;

    query = query.substr(startIdx, query.length);

    var endIdx = query.indexOf("&");
    if (endIdx == -1) {
        return query;
    }

    return query.substr(0, endIdx);


}

function isDate(year, month, day) {

    if (year.num() && month.num() && day.num()) {
    } else {
        return false;
    }

    year = Number(year);
    month = Number(month);
    day = Number(day);

    if (year < 0)
        return false;
    if (month < 1 || month > 12)
        return false;
    if (day < 1 || day > 31)
        return false;

    if (month == 4 || month == 6 || month == 9 || month == 11) {
        if (day == 31)
            return false;
    }

    // 윤년체크
    if (month == 2) {
        var g = parseInt(year / 4);
        if (isNaN(g)) {
            return false;
        }
        if (day > 29)
            return false;
        if (day == 29 && ((year / 4) != parseInt(year / 4)))
            return false;
    }
    return true;
}


function dateDiff(date1, date2) {
    var a1 = date1.getTime();
    var a2 = date2.getTime();
    return (a2 - a1) / (1000 * 60 * 60 * 24);
};

