package com.rara.dividend.scraper;

import com.rara.dividend.model.Company;
import com.rara.dividend.model.ScrapedResult;

public interface Scraper {
	Company scrapCompanyByTicker(String ticker);
	ScrapedResult scrap(Company company);
}
