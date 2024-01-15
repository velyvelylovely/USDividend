package com.rara.dividend.service.impl;

import com.rara.dividend.exception.impl.AlreadyExistCompanyException;
import com.rara.dividend.exception.impl.NoCompanyException;
import com.rara.dividend.exception.impl.NoExistTickerException;
import com.rara.dividend.model.Company;
import com.rara.dividend.model.ScrapedResult;
import com.rara.dividend.persist.entity.CompanyEntity;
import com.rara.dividend.persist.entity.DividendEntity;
import com.rara.dividend.persist.repository.CompanyRepository;
import com.rara.dividend.persist.repository.DividendRepository;
import com.rara.dividend.scraper.Scraper;
import com.rara.dividend.service.CompanyService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final Trie trie;
	private final Scraper yahooFinanceScraper;
	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	@Override
	public Company save(String ticker) {

 		boolean exists= companyRepository.existsByTicker(ticker);
		if (exists) {
			throw new AlreadyExistCompanyException();
		}

		 return storeCompanyAndDividend(ticker);
	}

	@Override
	public Page<CompanyEntity> getAllCompany(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

	@Override
	public void addAutocompleteKeyword(String keyword) {
		trie.put(keyword, null);
	}

	// 자동완성 Trie 방식
	@Override
	public List<String> autocomplete(String keyword) {
		return (List<String>) trie.prefixMap(keyword).keySet()
									.stream()
									.limit(10)
									.collect(Collectors.toList());
	}

	// 자동완성 Like 방식
	@Override
	public List<String> getCompanyNamesByKeyword(String keyword) {
		Pageable limit = PageRequest.of(0, 10);
		Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
		return companyEntities.stream()
						.map(e -> e.getName())
						.collect(Collectors.toList());
	}

	private void deleteAutocompleteKeyword(String keyword) {
		trie.remove(keyword);
	}

	private Company storeCompanyAndDividend(String ticker) {
		// ticker 를 기준으로 회사를 스크래핑
		 Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);
		if (ObjectUtils.isEmpty(company)) {
			throw new NoExistTickerException();
		}

		// 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
		ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

		// 스크래핑 결과
		CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
		List<DividendEntity> dividendEntities  = scrapedResult.getDividends().stream()
														.map(e -> new DividendEntity(companyEntity.getId(), e))
														.collect(Collectors.toList());

		dividendRepository.saveAll(dividendEntities);
		return company;
	}

	@Override
	public String deleteCompany(String ticker) {
		var company = companyRepository.findByTicker(ticker)
												.orElseThrow(() -> new NoCompanyException());

		dividendRepository.deleteAllByCompanyId(company.getId());
		companyRepository.delete(company);
		this.deleteAutocompleteKeyword(company.getName());

		return company.getName();
	}
}
