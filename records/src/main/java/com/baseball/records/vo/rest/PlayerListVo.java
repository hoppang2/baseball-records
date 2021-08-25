package com.baseball.records.vo.rest;

import lombok.Data;

@Data
public class PlayerListVo {

	private int seqNo;
	private String playerNm;
	private int playerPosition;
	private String playerPositionNm;
	private int teamSeqNo;
	private String teamNm;
	private String teamSubNm;
}
