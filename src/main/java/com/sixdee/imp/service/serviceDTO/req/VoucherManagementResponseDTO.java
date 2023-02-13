/**
 * 
 */
package com.sixdee.imp.service.serviceDTO.req;

import com.sixdee.imp.service.httpcall.dto.Response;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

/**
 * @author rahul.kr
 *
 */
public class VoucherManagementResponseDTO extends ResponseDTO {
	
	private String voucherValue = null;
	private String typeOfVoucher = null;
	private String merchantName = null;
	private VoucherStatus voucherStatus;
	
	
	public VoucherStatus getVoucherStatus() {
		return voucherStatus;
	}
	public void setVoucherStatus(VoucherStatus voucherStatus) {
		this.voucherStatus = voucherStatus;
	}
	public String getVoucherValue() {
		return voucherValue;
	}
	public void setVoucherValue(String voucherValue) {
		this.voucherValue = voucherValue;
	}
	public String getTypeOfVoucher() {
		return typeOfVoucher;
	}
	public void setTypeOfVoucher(String typeOfVoucher) {
		this.typeOfVoucher = typeOfVoucher;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
}
