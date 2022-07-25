package com.example.dividendfund.scraper;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
