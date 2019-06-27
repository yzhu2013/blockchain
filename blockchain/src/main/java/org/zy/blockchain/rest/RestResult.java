package org.zy.blockchain.rest;

import java.io.Serializable;

public class RestResult implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean successful;
	
	private String errCode;
	
	private Object resultValue;
	
	private String resultHint;
	
	public static RestResult restSuccess(Object resultValue) {
		return new RestResult(true, null, resultValue);
	}
	
	public static RestResult restSuccess(Object resultValue, String resultHint) {
		return new RestResult(true, null, resultValue, resultHint);
	}
	
	public static RestResult restSuccess() {
		return new RestResult(true);
	}
	
	
	public static RestResult restFail() {
		return new RestResult(false);
	}
	
	public static RestResult restFail(String errCode) {
		return new RestResult(false, errCode);
	}
	
	public static RestResult restFail(String errCode, String resultHint) {
		return new RestResult(false, errCode, null, resultHint);
	}
	
	public RestResult(boolean successful) {
		this(successful, null);
	}

	public RestResult(boolean successful, String errCode) {
		this(successful, errCode, null);
	}

	public RestResult(boolean successful, String errCode, Object resultValue) {
		this(successful, errCode, resultValue, null);
	}

	public RestResult(boolean successful, String errCode, Object resultValue, String resultHint) {
		this.successful = successful;
		this.errCode = errCode;
		this.resultValue = resultValue;
		this.resultHint = resultHint;
	}
	
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public Object getResultValue() {
		return resultValue;
	}

	public void setResultValue(Object resultValue) {
		this.resultValue = resultValue;
	}

	public String getResultHint() {
		return resultHint;
	}

	public void setResultHint(String resultHint) {
		this.resultHint = resultHint;
	}

}
