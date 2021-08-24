package com.baseball.records.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class _DefaultService {

	@Autowired
    @Qualifier("sqlSessionTemplatePrimary")
    public SqlSession sql;

}
