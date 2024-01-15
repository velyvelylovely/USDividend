package com.rara.dividend.exception.impl;

import com.rara.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoExistTickerException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 ticker 입니다.";
	}
}
