package com.sixdee.imp.dto;

import java.util.Date;

public class UserIdentifierDTO {
	

  private Long id;
	private Long loyaltyID;                          
	private String userName;
	private String passWord;
	private Date createDate;
	private Long counter;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLoyaltyID() {
		return loyaltyID;
	}
	public void setLoyaltyID(Long loyaltyID) {
		this.loyaltyID = loyaltyID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
	}

    public Date getCreateDate() {
    return createDate;
  }
    
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  @Override
  public String toString() {
    return "UserIdentifierDTO [id=" + id + ", loyaltyID=" + loyaltyID + ", userName=" + userName
        + ", passWord=" + passWord + ", createDate=" + createDate + ", counter=" + counter + "]";
  }
	
	
}
