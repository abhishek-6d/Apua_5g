/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

/**
 * @author rahul.kr
 *
 */
public class GetMyGamingGiftRespDTO extends CommonRespDTO {

	private GamingGiftsRecieved gamingGiftsRecieved = null;

	public GamingGiftsRecieved getGamingGiftsRecieved() {
		return gamingGiftsRecieved;
	}

	public void setGamingGiftsRecieved(GamingGiftsRecieved gamingGiftsRecieved) {
		this.gamingGiftsRecieved = gamingGiftsRecieved;
	}
	
	
}
