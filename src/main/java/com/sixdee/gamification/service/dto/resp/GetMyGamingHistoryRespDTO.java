/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

/**
 * @author rahul.kr
 *
 */
public class GetMyGamingHistoryRespDTO extends CommonRespDTO{

	private GamingHistoryDTO gamingHistoryDTO = null;

	public GamingHistoryDTO getGamingHistoryDTO() {
		return gamingHistoryDTO;
	}

	public void setGamingHistoryDTO(GamingHistoryDTO gamingHistoryDTO) {
		this.gamingHistoryDTO = gamingHistoryDTO;
	}
	
	
}
