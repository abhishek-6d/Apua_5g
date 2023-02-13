/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Param;

/**
 * @author rahul.kr
 *
 */
public class LeaderBoardDetailsDTO {

	private String position = null;
	private String name = null;
	private String points = "0";
	private Param parameters = null;
	
	
	public Param getParameters() {
		return parameters;
	}
	public void setParameters(Param parameters) {
		this.parameters = parameters;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	
}
