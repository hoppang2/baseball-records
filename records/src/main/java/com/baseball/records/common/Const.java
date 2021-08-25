package com.baseball.records.common;


import com.baseball.records.annotation.ErrorInfo;

public class Const {

    public static final class CODE {
        public static final String PUBLIC_BUCKET_KEY = "public";
    }

    public static final class ERROR_CODE {

        @ErrorInfo(description = "정상")
        public static final int OK = 200;

        @ErrorInfo(description = "인증오류")
        public static final int AUTH_ERROR = 401;


        @ErrorInfo(description = "파라메터 오류")
        public static final int PARAMETER_ERROR = 911;

        @ErrorInfo(description = "선수 정보 등록 ERROR")
        public static final int PLAYER_INFO_SAVE_ERROR = 101001;

        @ErrorInfo(description = "선수 정보 삭제 ERROR")
        public static final int PLAYER_INFO_DELETE_ERROR = 101002;

    }

    /**
     * 사용자 타입
     * @author theloca
     *
     */
    public static final class USER_TYPE {
    	public static final int SUPER_ADMIN = 0;	//슈퍼관리자
    	public static final int CONTRACT_ADMIN = 1;	//계약처 관리자
    	public static final int CONTRACT_USER = 2;	//계약처 회원
    }
}
