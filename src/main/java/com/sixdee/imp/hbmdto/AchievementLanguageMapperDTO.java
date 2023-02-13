package com.sixdee.imp.hbmdto;

/**
 * 
 * @author @jith
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>March 08,2018 04:23:10 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.sql.Timestamp;


public class AchievementLanguageMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int Id ;
	private String languageID;
	private String achievmentName;
	private String achievementDesc;
	private String achievementImage;
	private String achievmentId;
	private AchievementMasterDTO achievementMasterDTO;
	
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public String getAchievmentName() {
		return achievmentName;
	}
	public void setAchievmentName(String achievmentName) {
		this.achievmentName = achievmentName;
	}
	public String getAchievementDesc() {
		return achievementDesc;
	}
	public void setAchievementDesc(String achievementDesc) {
		this.achievementDesc = achievementDesc;
	}
	public String getAchievementImage() {
		return achievementImage;
	}
	public void setAchievementImage(String achievementImage) {
		this.achievementImage = achievementImage;
	}
	public String getAchievmentId() {
		return achievmentId;
	}
	public void setAchievmentId(String achievmentId) {
		this.achievmentId = achievmentId;
	}
	public AchievementMasterDTO getAchievementMasterDTO() {
		return achievementMasterDTO;
	}
	public void setAchievementMasterDTO(AchievementMasterDTO achievementMasterDTO) {
		this.achievementMasterDTO = achievementMasterDTO;
	}
	
	
	
	
	
	
	
}
