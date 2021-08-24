package com.baseball.records.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baseball.records.vo.rest.TeamVo;

@Service
public class TeamService extends _DefaultService{

	public List<TeamVo> getTeamList() {
		return sql.selectList("team.getTeamList");
	}

	public TeamVo getTeamInfo(int seqNo) {
		return sql.selectOne("team.getTeamInfo", seqNo);
	}

}
