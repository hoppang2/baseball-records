//-----------------------------------------------------------------------------
//String Buffer
//-----------------------------------------------------------------------------

var StringBuffer = function() {
    this.buffer = new Array();
};

StringBuffer.prototype.append = function(obj) {
     this.buffer.push(obj);
};

StringBuffer.prototype.appendln = function(obj) {
    this.buffer.push(obj + '\n');
};

StringBuffer.prototype.toString = function(){
     return this.buffer.join("");
};

if(typeof String.prototype.trimLeft !== 'function') {
    String.prototype.trimLeft = function() {
        return this.replace(/^\s+/,"");
    };
}

if(typeof String.prototype.trimRight !== 'function') {
    String.prototype.trimRight = function() {
        return this.replace(/\s+$/,"");
    };
}

if (!String.prototype.padStart) {
    String.prototype.padStart = function padStart(targetLength,padString) {
        targetLength = targetLength>>0;
        padString = String((typeof padString !== 'undefined' ? padString : ' '));
        if (this.length > targetLength) {
            return String(this);
        }
        else {
            targetLength = targetLength-this.length;
            if (!__IE && targetLength > padString.length) {
                padString += padString.repeat(targetLength/padString.length);
            }
            return padString.slice(0,targetLength) + String(this);
        }
    };
}



//-----------------------------------------------------------------------------
//문자 앞 뒤 공백을 제거 한다.
//-----------------------------------------------------------------------------
String.prototype.trim = function () {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

//-----------------------------------------------------------------------------
//str1에 매치되는 문자를 전부 str2로 바꾼다
//-----------------------------------------------------------------------------
String.prototype.replaceAll = function (str1, str2) {
	return this.split(str1).join(str2);
};

//-----------------------------------------------------------------------------
//내용이 있는지 없는지 확인하다.
//
//@return : true(내용 있음) | false(내용 없음)
//-----------------------------------------------------------------------------
String.prototype.notNull = function () {
	return (this == null || this.trim() == "") ? false : true;
};

//-----------------------------------------------------------------------------
//내용이 있는지 없는지 확인하다.
//
//@return : true(내용 없음) | false(내용 있음)
//-----------------------------------------------------------------------------
String.prototype.isN = function () {
	return (this == null || this.trim() == "") ? true : false;
};

String.prototype.isNN = function () {
	return !this.isN();
};

//-----------------------------------------------------------------------------
//빈값 혹은 0 인지 확인 한다
//
//@return : true(내용 없음) | false(내용 있음)
//-----------------------------------------------------------------------------
String.prototype.isZeroOrN = function () {
	if (this == null || this.trim() == "") {
		return true;
	}
	var val = this.trim();
	if (val == 0) {
		return true;
	}
	return false;
};



//-----------------------------------------------------------------------------
//날짜 형식 체크 YYYY-MM-DD || YYYYMMDD형식
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.date = function () {
	var str = this.replaceAll('-', '');
	var em = str.match(/^[0-9]{4}[0-9]{2}[0-9]{2}$/);
	return (em) ? true : false;
};


//-----------------------------------------------------------------------------
//날짜 형식 체크 YYYY-MM-DD 형식
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.toDate = function () {
	var date = this.replaceAll('-', '');
	var y = date.substr(0,4),
	m = date.substr(4,2) - 1,
	d = date.substr(6,2);
	return new Date(y,m,d);
};


//-----------------------------------------------------------------------------
//날짜 형식 체크 YYYY-MM-DD 형식
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.isDate = function () {
	var pt = /[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/;
	var em = this.match(pt);
	return (em) ? true : false;
};


//-----------------------------------------------------------------------------
//URL 형식 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.domain = function () {
	var em = this.match(/^[.a-zA-Z0-9-]+.[a-zA-Z]+$/);
	return (em) ? true : false;
};

//-----------------------------------------------------------------------------
//주민번호 체크 XXXXXX-XXXXXXX 형태로 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.jumin = function () {
	var num = this.trim().onlyNum();
	if (num.length == 13) {
		num = num.substring(0, 6) + "-" + num.substring(6, 13);
	} else {
		return false;
	}
	num = num.match(/^([0-9]{6})-?([0-9]{7})$/);
	if (!num)
		return false;
	var num1 = RegExp.$1;
	var num2 = RegExp.$2;
	if (!num2.substring(0, 1).match(/^[1-4]{1}$/))
		return false;
	num = num1 + num2;
	var sum = 0;
	var last = num.charCodeAt(12) - 0x30;
	var bases = "234567892345";
	for (var i = 0; i < 12; i++) {
		sum += (num.charCodeAt(i) - 0x30) * (bases.charCodeAt(i) - 0x30);
	}
	var mod = sum % 11;
	return ((11 - mod) % 10 == last) ? true : false;
};

function ssnConfirmForeignCountry(para) { // / 외국은 실명인증

	var fgnno;
	var sum = 0;
	var odd = 0;
	fgnno = para;
	buf = new Array(13);
	for (var i = 0; i < 13; i++) {
		buf[i] = parseInt(fgnno.charAt(i));
	}
	odd = buf[7] * 10 + buf[8];
	if (odd % 2 != 0) {
		return false;
	}

	if ((buf[11] != 6) && (buf[11] != 7) && (buf[11] != 8) && (buf[11] != 9)) {
		return false;
	}

	multipliers = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];

	for (i = 0, sum = 0; i < 12; i++) {
		sum += (buf[i] *= multipliers[i]);
	}
	sum = 11 - (sum % 11);
	if (sum >= 10) {
		sum -= 10;
	}

	sum += 2;
	if (sum >= 10) {
		sum -= 10;
	}

	if (sum != buf[12]) {
		return false;
	}
	return true;
}

//-----------------------------------------------------------------------------
//사업자번호 체크 XXX-XX-XXXXX 형태로 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.biznum = function () {
	var num = this.trim().onlyNum();
	if (num.length == 10) {
		num = num.substring(0, 3) + "-" + num.substring(3, 5) + "-"
				+ num.substring(5, 10);
	} else {
		return false;
	}
	num = num.match(/^([0-9]{3})-?([0-9]{2})-?([0-9]{5})$/);

	if (!num)
		return false;

	num = RegExp.$1 + RegExp.$2 + RegExp.$3;

	var sumMod = 0;
	sumMod += parseInt(num.substring(0, 1));
	sumMod += parseInt(num.substring(1, 2)) * 3 % 10;
	sumMod += parseInt(num.substring(2, 3)) * 7 % 10;
	sumMod += parseInt(num.substring(3, 4)) * 1 % 10;
	sumMod += parseInt(num.substring(4, 5)) * 3 % 10;
	sumMod += parseInt(num.substring(5, 6)) * 7 % 10;
	sumMod += parseInt(num.substring(6, 7)) * 1 % 10;
	sumMod += parseInt(num.substring(7, 8)) * 3 % 10;
	sumMod += Math.floor(parseInt(num.substring(8, 9)) * 5 / 10);
	sumMod += parseInt(num.substring(8, 9)) * 5 % 10;
	sumMod += parseInt(num.substring(9, 10));

	return (sumMod % 10 == 0) ? true : false;
};

//-----------------------------------------------------------------------------
//전화번호 체크 XXX-XXXX-XXXX 형태로 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.phone = function () {
	var num = this.trim().onlyNum();
	if (num.substring(1, 2) == "2") {
		num = num.substring(0, 2) + "-" + num.substring(2, num.length - 4)
				+ "-" + num.substring(num.length - 4, num.length);
	} else {
		num = num.substring(0, 3) + "-" + num.substring(3, num.length - 4)
				+ "-" + num.substring(num.length - 4, num.length);
	}
	num = num.match(/^0[0-9]{1,2}-[1-9]{1}[0-9]{2,3}-[0-9]{4}$/);
	return (num) ? true : false;
};

//-----------------------------------------------------------------------------
//핸드폰 체크 XXX-XXXX-XXXX 형태로 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.mobile = function () {
	var num = this.trim().onlyNum();
	num = num.substring(0, 3) + "-" + num.substring(3, num.length - 4) + "-"+ num.substring(num.length - 4, num.length);
	num = num.trim().match(/^01[016789]{1}-[1-9]{1}[0-9]{2,3}-[0-9]{4}$/);
	return (num) ? true : false;
};

//-----------------------------------------------------------------------------
//숫자만 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.num = function () {
	return (this.trim().match(/^[0-9]+$/)) ? true : false;
};


//-----------------------------------------------------------------------------
//숫자만 체크(+ - 를 포함한 실수)
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.realNum = function () {
	return (this.trim().match(/^[+-]?\d*(\.?\d*)$/)) ? true : false;
};




//-----------------------------------------------------------------------------
//영어만 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.eng = function () {
	return (this.trim().match(/^[a-zA-Z]+$/)) ? true : false;
};

//-----------------------------------------------------------------------------
//영어와 숫자만 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.engnum = function () {
	return (this.trim().match(/^[0-9a-zA-Z]+$/)) ? true : false;
};

//-----------------------------------------------------------------------------
//영어와 숫자만 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.numeng = function () {
	return this.engnum();
};



//-----------------------------------------------------------------------------
//영어와 숫자, 한글 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.korengnum = function () {
	return (this.trim().match(/[^-가-힣a-zA-Z0-9]/)) ? false : true;
};



//-----------------------------------------------------------------------------
//아이디 체크 영어와 숫자만 체크 첫글자는 영어로 시작
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.userid = function () {
	return (this.trim().match(/^[a-zA-z]{1}[0-9a-zA-Z]+$/)) ? true : false;
};


//-----------------------------------------------------------------------------
//한글만 체크
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.kor = function () {
	return (this.trim().match(/^[가-힣]+$/)) ? true : false;
};

//-----------------------------------------------------------------------------
//숫자와 . - 이외의 문자는 다 뺀다. - 통화량을 숫자로 변환
//
//@return : 숫자
//-----------------------------------------------------------------------------
String.prototype.toNum = function () {
	var str = this.trim();
	if(str.substr(0, 1) == '-'){
		return '-' + str.replace(/[^0-9]/g, '');
	}
	else{
		return str.replace(/[^0-9]/g, '');
	}
};

//-----------------------------------------------------------------------------
//숫자와 . - 이외의 문자는 다 뺀다. - Number 형식으로 반환한다.
//
//@return : 숫자
//-----------------------------------------------------------------------------
String.prototype.toNumber = function () {
	return Number(this.toNum());
};

String.prototype.toInt = function () {
	return Number(this.toNum());
};

//-----------------------------------------------------------------------------
//숫자 이외에는 다 뺀다.
//
//@return : 숫자
//-----------------------------------------------------------------------------
String.prototype.onlyNum = function () {
	return (this.trim().replace(/[^0-9]/g, ""));
};

//-----------------------------------------------------------------------------
//숫자만 뺀 나머지 전부
//
//@return : 숫자 이외
//-----------------------------------------------------------------------------
String.prototype.noNum = function () {
	return (this.trim().replace(/[0-9]/g, ""));
};

String.prototype.toBool = function () {
	if (this.toLowerCase() == "true") {
		return true;
	}
	return false;
};


//-----------------------------------------------------------------------------
//숫자에 3자리마다 , 를 찍어서 반환
//
//@return : 통화량
//-----------------------------------------------------------------------------
String.prototype.toMoney = function () {
	var num = this.toNum();
	var pattern = /(-?[0-9]+)([0-9]{3})/;
	while (pattern.test(num)) {
		num = num.replace(pattern, "$1,$2");
	}
	if(num == ''){
		return '0';
	}
	else{
		return num;
	}
};

//-----------------------------------------------------------------------------
//String length 반환 한글 2글자 영어 1글자
//
//@return : int
//-----------------------------------------------------------------------------
String.prototype.getByteLength = function () {
	var tmplen = 0;
	for (var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) > 127)
			tmplen += 2;
		else
			tmplen++;
	}
	return tmplen;
};

String.prototype.korDateToDate = function (splitChar) {
	if (typeof (splitChar) == "undefined") {
		splitChar = "-";
	}
	var dateArr = this.split(splitChar);
	if (dateArr.length != 3) {
		return null;
	}
	return new Date(Number(dateArr[0]), Number(dateArr[1]) - 1, Number(dateArr[2]));
};


//-----------------------------------------------------------------------------
//String  한글 조사 체크
//
//@return : String
//-----------------------------------------------------------------------------
String.prototype.josa = function (josa) {
	var code = this.charCodeAt(this.length - 1) - 44032;
	var cho = 19, jung = 21, jong = 28;
	var i1, i2, code1, code2;

	// 원본 문구가 없을때는 빈 문자열 반환
	if (this.length == 0) {
		return '';
	}

	// 한글이 아닐때
	if (code < 0 || code > 11171) {
		return this;
	}

	function getJosa(josa, jong) {
		// jong : true면 받침있음, false면 받침없음

		if (josa == '을' || josa == '를') return (jong ? '을' : '를');
		if (josa == '이' || josa == '가') return (jong ? '이' : '가');
		if (josa == '은' || josa == '는') return (jong ? '은' : '는');
		if (josa == '와' || josa == '과') return (jong ? '와' : '과');

		// 알 수 없는 조사
		return '**';
	}

	if (code % 28 == 0) {
		return this + getJosa(josa, false);
	}
	else {
		return this + getJosa(josa, true);
	}

};

//-----------------------------------------------------------------------------
//숫자에 콤마 찍기
//
//@return : string
//-----------------------------------------------------------------------------
function numberWithCommas(x) {
  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


//-----------------------------------------------------------------------------
//Date 문자열 반환
//
//@return : string
//-----------------------------------------------------------------------------
Date.prototype.format = function (format) {
	var o = {
		"M+": this.getMonth() + 1, //month
		"d+": this.getDate(),    //day
		"h+": this.getHours(),   //hour
		"m+": this.getMinutes(), //minute
		"s+": this.getSeconds(), //second
		"q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
		"S": this.getMilliseconds() //millisecond
	}

	if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
  (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o) if (new RegExp("(" + k + ")").test(format))
		format = format.replace(RegExp.$1,
    RegExp.$1.length == 1 ? o[k] :
      ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

//-----------------------------------------------------------------------------
//Date 문자열 반환
//
//@return : string
//-----------------------------------------------------------------------------
Date.prototype.toFormattedString = function (f) {
	var nm = this.getMonthName();
	var nd = this.getDayName();
	f = f.replace(/yyyy/g, this.getFullYear());
	f = f.replace(/yy/g, String(this.getFullYear()).substr(2, 2));
	f = f.replace(/MMM/g, nm.substr(0, 3).toUpperCase());
	f = f.replace(/Mmm/g, nm.substr(0, 3));
	f = f.replace(/MM\*/g, nm.toUpperCase());
	f = f.replace(/Mm\*/g, nm);
	f = f.replace(/mm/g, String(this.getMonth() + 1).padLeft('0', 2));
	f = f.replace(/DDD/g, nd.substr(0, 3).toUpperCase());
	f = f.replace(/Ddd/g, nd.substr(0, 3));
	f = f.replace(/DD\*/g, nd.toUpperCase());
	f = f.replace(/Dd\*/g, nd);
	f = f.replace(/dd/g, String(this.getDate()).padLeft('0', 2));
	f = f.replace(/d\*/g, this.getDate());
	return f;
};


Date.prototype.getMonthName = function ()
{
  return this.toLocaleString().replace(/[^a-z]/gi,'');
};

Date.prototype.getDayName = function ()
{
  switch(this.getDay())
  {
      case 0: return '일';
      case 1: return '월';
      case 2: return '화';
      case 3: return '수';
      case 4: return '목';
      case 5: return '금';
      case 6: return '토';
  }
};

String.prototype.padLeft = function (value, size)
{
  var x = this;
  while (x.length < size) {x = value + x;}
  return x;
};


//-----------------------------------------------------------------------------
//날짜 일 더하기
//-----------------------------------------------------------------------------
Date.prototype.addDays = function(days) {
	var dat = new Date(this.valueOf());
	dat.setDate(dat.getDate() + days);
	return dat;
}

//-----------------------------------------------------------------------------
//날짜 월 더하기
//-----------------------------------------------------------------------------
Date.prototype.addMonth = function(month) {
	var dat = new Date(this.valueOf());
	dat.setMonth(dat.getMonth() + month);
	return dat;
}

//-----------------------------------------------------------------------------
//만나이를 계산한다
//-----------------------------------------------------------------------------
String.prototype.age = function() {
	var date = new Date();
	var year = date.getFullYear();
	var month = (date.getMonth() + 1);
	var day = date.getDate();
	if (month < 10) month = '0' + month;
	if (day < 10) day = '0' + day;
	var monthDay = month + day;

	var birth = this.trim().replace('-', '').replace('-', '');
	var birthdayy = birth.substr(0, 4);
	var birthdaymd = birth.substr(4, 4);

	var age = monthDay < birthdaymd ? year - birthdayy - 1 : year - birthdayy;
	return age;
}

//-----------------------------------------------------------------------------
//메일의 유효성을 체크 한다.
//
//@return : true(맞는 형식) | false(잘못된 형식)
//-----------------------------------------------------------------------------
String.prototype.mail = function () {
	//var em = this.trim().match(/^[_\-\.0-9a-zA-Z]{3,}@[-.0-9a-zA-z]{2,}\.[a-zA-Z]{2,4}$/);
	//return (em) ? true : false;
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(this.trim());
};

