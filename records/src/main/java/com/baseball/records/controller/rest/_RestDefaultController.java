package com.baseball.records.controller.rest;


import com.baseball.records.vo.RestResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class _RestDefaultController {

    private final Logger mLogger = LoggerFactory.getLogger(_RestDefaultController.class);
    protected String _ver = "1.0.0";

    @Autowired
    @Qualifier("sqlSessionTemplatePrimary")
    public SqlSession sql;

//    @Autowired
//    ErrorCodeManager errorCodeManager;

    public ResponseEntity<?> httpError(int errorCode, String message) {
        RestResponseVo vo = getResponseVo();
        vo.message = message;
        vo.resultCode = 0;
        return ResponseEntity.status(errorCode).body(vo);
    }

    public ResponseEntity<?> succuss() {
        return succuss(0, null);
    }

    public ResponseEntity<?> succuss(Object payload) {
        return succuss(0, payload);
    }

    public ResponseEntity<?> succuss(int resultCode, Object payload) {

        RestResponseVo vo = getResponseVo();
        vo.resultCode = resultCode;
        if (payload != null) {
            vo.payload = payload;
        }
        vo.success = true;
        return ResponseEntity.status(200).body(vo);
    }

    public ResponseEntity<?> error(int errorCode) {
        return error(errorCode, null, null);
    }

    public ResponseEntity<?> error(int errorCode, String errorMessage) {
        return error(errorCode, errorMessage, null);
    }

    public ResponseEntity<?> error(int errorCode, String errorMessage, Object payload) {
        RestResponseVo vo = getResponseVo();
//        String sysErrorMessage = errorCodeManager.get(errorCode);
        String sysErrorMessage = "error";

        mLogger.info("ERROR CODE : {} : {}", errorCode, sysErrorMessage);

        if (StringUtils.isNotBlank(errorMessage)) {
            if (StringUtils.isBlank(sysErrorMessage)) {
                vo.message = errorMessage;
            } else {
                vo.message = sysErrorMessage + " - " + errorMessage;
            }
        } else {
            vo.message = sysErrorMessage;
        }

        vo.resultCode = 0;
        if (payload != null) {
            vo.payload = payload;
        }
        vo.errorCode = errorCode;
        return ResponseEntity.status(200).body(vo);
    }

    public RestResponseVo getResponseVo() {
        RestResponseVo vo = new RestResponseVo();
        vo.version = _ver;
        return vo;
    }
}