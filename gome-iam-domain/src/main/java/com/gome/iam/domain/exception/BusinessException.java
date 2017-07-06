package com.gome.iam.domain.exception;


/**
 * 业务操作异常，必须初始化异常消息。
 * @author yintongjiang
 * @date 2016/11/22
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7098068479494978885L;

	/**
	 * 构造函数
	 * @param message
	 */
	public BusinessException(String message){
		super(message);
	}
}
