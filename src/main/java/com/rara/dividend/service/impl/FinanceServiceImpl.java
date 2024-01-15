package com.rara.dividend.service.impl;

import com.rara.dividend.exception.impl.NoCompanyException;
import com.rara.dividend.model.Company;
import com.rara.dividend.model.Dividend;
import com.rara.dividend.model.ScrapedResult;
import com.rara.dividend.persist.entity.CompanyEntity;
import com.rara.dividend.persist.entity.DividendEntity;
import com.rara.dividend.persist.repository.CompanyRepository;
import com.rara.dividend.persist.repository.DividendRepository;
import com.rara.dividend.service.FinanceService;
import com.rara.dividend.model.constants.CacheKey;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceServiceImpl implements FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	@Override
	@Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
	public ScrapedResult getDividendByCompanyName(String companyName) {
		log.info("search company -> " + companyName);

		// 1. 회사명을 기준으로 회사 정보 조회
		CompanyEntity company = companyRepository.findByName(companyName)
								.orElseThrow(NoCompanyException::new);

		// 2. 조회된 회사 ID 로 배당금 정보 조회
		List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

		// 3. 결과 조합 후 반환
		List<Dividend> dividends = dividendEntities.stream()
													.map(e -> new Dividend(e.getDate(), e.getDividend()))
													.collect(Collectors.toList());

		return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividends);
	}
}
