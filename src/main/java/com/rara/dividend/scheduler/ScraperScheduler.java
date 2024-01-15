package com.rara.dividend.scheduler;

import com.rara.dividend.model.Company;
import com.rara.dividend.model.ScrapedResult;
import com.rara.dividend.persist.entity.CompanyEntity;
import com.rara.dividend.persist.entity.DividendEntity;
import com.rara.dividend.persist.repository.CompanyRepository;
import com.rara.dividend.persist.repository.DividendRepository;
import com.rara.dividend.model.constants.CacheKey;
import com.rara.dividend.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final Scraper yahooFinanceScraper;
	private final DividendRepository dividendRepository;

	@Scheduled(cron = "${scheduler.scrap.yahoo}")
	@CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
	public void yahooFinanceScheduling() {
		log.info("scraping scheduler is started");

		// 저장된 회사 목록 조회
		List<CompanyEntity> companies = companyRepository.findAll();

		// 회사마다 배당금 정보를 새로 스크래핑
		for (var company : companies) {
			ScrapedResult scrapedResult= yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

			//스크래핑한 배당금 정보 중 데이터베이스에 없는 값을 저장
			scrapedResult.getDividends().stream()
					.map(e -> new DividendEntity(company.getId(), e))     // 모델을 엔티티로 매핑
					.forEach(e -> {                                       // 엘리먼트를 하나씩 레파지토리에 삽입
						boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
						if (!exists) {
							dividendRepository.save(e);
							log.info("insert new dividend -> " + e.toString());
						}
					});

			// 연속적 스크래핑 대상 사이트의 서버에 요청 날리지 않도록 일시정지
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}


