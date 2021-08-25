package com.baseball.records.vo.rest;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeamVo {
	private int seqNo;
	private String teamNm;
	private String teamSubNm;
}
