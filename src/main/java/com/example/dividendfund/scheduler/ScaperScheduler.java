package com.example.dividendfund.scheduler;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.ScrapedResult;
import com.example.dividendfund.model.constants.CacheKey;
import com.example.dividendfund.persist.CompanyRepository;
import com.example.dividendfund.persist.DividendRepository;
import com.example.dividendfund.persist.entity.CompanyEntity;
import com.example.dividendfund.persist.entity.DividendEntity;
import com.example.dividendfund.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScaperScheduler {

    private final CompanyRepository companyRepository;
    private final Scraper scraper;
    private final DividendRepository dividendRepository;


    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "0 0 0 * * *")
    public void yahooFinanceScheduling(){

        // 저장된 회사 목록 조회
        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        //회사마다 배당금 update
        for(var company : companyEntityList){
            log.info("Scraping scheduler is start -> " + company.getName());
             ScrapedResult scrapedResult = scraper.scrap(new Company(company.getTicker(), company.getName()));

            scrapedResult.getDividendList().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists){
                            dividendRepository.save(e);
                            log.info("insert new dividend -> " + e.toString());
                        }
                    });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
