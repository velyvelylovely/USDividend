package com.rara.dividend.service;

import com.rara.dividend.model.ScrapedResult;

public interface FinanceService {

	ScrapedResult getDividendByCompanyName(String companyName);
}
