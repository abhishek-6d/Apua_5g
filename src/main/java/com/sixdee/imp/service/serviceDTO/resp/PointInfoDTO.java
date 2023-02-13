package com.sixdee.imp.service.serviceDTO.resp;

public class PointInfoDTO	extends ResponseDTO 
{
	private PointInfoDetailsDTO[] statusPoints ;
	private PointInfoDetailsDTO[] rewardPoints ;
	
	public PointInfoDetailsDTO[] getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(PointInfoDetailsDTO[] rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public PointInfoDetailsDTO[] getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(PointInfoDetailsDTO[] statusPoints) {
		this.statusPoints = statusPoints;
	}
}
