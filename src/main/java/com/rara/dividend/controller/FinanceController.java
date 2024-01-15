package com.rara.dividend.controller;

import com.rara.dividend.service.impl.FinanceServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

	private final FinanceServiceImpl financeServiceImpl;

	@ApiOperation(value = "입력한 회사의 배당금 정보 조회")
	@GetMapping("/dividend/{companyName}")
	public ResponseEntity<?> searchFinance(@PathVariable @ApiParam(value = "조회하려는 회사명") String companyName) {
		var result = financeServiceImpl.getDividendByCompanyName(companyName);
		return ResponseEntity.ok(result);
	}
}
