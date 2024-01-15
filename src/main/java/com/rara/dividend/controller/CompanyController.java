package com.rara.dividend.controller;

import com.rara.dividend.model.Company;
import com.rara.dividend.persist.entity.CompanyEntity;
import com.rara.dividend.model.constants.CacheKey;
import com.rara.dividend.service.impl.CompanyServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
	private final CompanyServiceImpl companyServiceImpl;
	private final CacheManager redisCacheManager;

	@ApiOperation(value = "자동완성 기능으로 입력한 키워드에 해당하는 회사 목록 조회(10개)")
	@GetMapping("autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam @ApiParam(value = "검색 키워드") String keyword) {
		var result = companyServiceImpl.autocomplete(keyword);

		return ResponseEntity.ok(result);
	}

	@ApiOperation(value = "서비스에서 관리중인 모든 회사 목록 조회")
	@GetMapping
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<?> searchCompany(final Pageable pageable) {
		Page<CompanyEntity> companies = companyServiceImpl.getAllCompany(pageable);
		return ResponseEntity.ok(companies);
	}

	@ApiOperation(value = "새로운 회사 정보 저장")
	@PostMapping
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> adCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();
		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = companyServiceImpl.save(ticker);
		companyServiceImpl.addAutocompleteKeyword(company.getName());

		return ResponseEntity.ok(company);
	}

	@ApiOperation(value = "회사 정보 삭제")
	@DeleteMapping("/{ticker}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(@PathVariable @ApiParam(value = "삭제하고 싶은 회사의 ticker") String ticker) {
		String companyName = companyServiceImpl.deleteCompany(ticker);
		this.clearFinanceCache(companyName);

		return ResponseEntity.ok(companyName);
	}

	private void clearFinanceCache(String companyName) {
		redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
	}

}
