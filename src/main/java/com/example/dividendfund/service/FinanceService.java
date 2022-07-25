package com.example.dividendfund.service;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.Dividend;
import com.example.dividendfund.model.ScrapedResult;
import com.example.dividendfund.model.constants.CacheKey;
import com.example.dividendfund.persist.CompanyRepository;
import com.example.dividendfund.persist.DividendRepository;
import com.example.dividendfund.persist.entity.CompanyEntity;
import com.example.dividendfund.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key="#companyName", value= CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName){
        log.info("search company ->" + companyName);
        //회사정보 조회
        CompanyEntity companyEntity = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("invalid companyname"));
        //회사id로 배당금 조회
        List<DividendEntity> dividendEntityList =dividendRepository.findAllByCompanyId(companyEntity.getId());

        List<Dividend> dividendList = new ArrayList<>();
        for(var entity : dividendEntityList){
            dividendList.add(new Dividend(entity.getDate(), entity.getDividend()));
        }

        //결과 반환
        return new ScrapedResult(new Company(companyEntity.getTicker(), companyEntity.getName()),
                dividendList);
    }
}
