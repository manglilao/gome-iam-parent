package com.gome.iam.domain.user;

import java.io.Serializable;

public class OAUserInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2664892098366828833L;

	private String deptCode;

    private String deptName;

    private String email;
    
    private String status;
    
    private String userId;
    
    private String userName;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
}