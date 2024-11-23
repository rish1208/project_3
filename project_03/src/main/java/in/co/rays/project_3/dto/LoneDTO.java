package in.co.rays.project_3.dto;

import java.util.Date;

public class LoneDTO extends BaseDTO {
	
	private Date loneStartDate;
	private String customerId;
    private Integer loneAmount;
    private Integer interestRate;
    private String mobile;
    
	

	public Date getLoneStartDate() {
		return loneStartDate;
	}

	public void setLoneStartDate(Date loneStartDate) {
		this.loneStartDate = loneStartDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getLoneAmount() {
		return loneAmount;
	}

	public void setLoneAmount(Integer loneAmount) {
		this.loneAmount = loneAmount;
	}

	public Integer getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Integer interestRate) {
		this.interestRate = interestRate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
