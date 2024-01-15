package com.rara.dividend.exception.impl;

import com.rara.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistCompanyException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "이미 존재하는 회사 정보입니다.";
	}
}
