package com.sixdee.imp.dto;

import java.util.Date;


public class UserLoginInfoDTO {

  private Long id;
  private String userName;
  private String passWord;
  private String isUsed;
  private Long counter;
  private Date createDate;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getIsUsed() {
    return isUsed;
  }

  public void setIsUsed(String isUsed) {
    this.isUsed = isUsed;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }



}
