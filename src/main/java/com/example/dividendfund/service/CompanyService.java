package com.example.dividendfund.service;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.ScrapedResult;
import com.example.dividendfund.persist.CompanyRepository;
import com.example.dividendfund.persist.DividendRepository;
import com.example.dividendfund.persist.entity.CompanyEntity;
import com.example.dividendfund.persist.entity.DividendEntity;
import com.example.dividendfund.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie;
    private final Scraper scraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker){

        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return storeCompanyAndDividend(ticker);
    }
    public Page<CompanyEntity> getAllCompany(final Pageable pageable){
        return companyRepository.findAll(pageable);
    }
    private Company storeCompanyAndDividend(String ticker){
        //회사 스크래핑
        Company company = scraper.scrapCompanyByTicker(ticker);

        if(company.equals(null)){
            throw new RuntimeException("failed to scrap ticker " + ticker);
        }
        //배당금 스크래핑
        ScrapedResult scrapedResult = scraper.scrap(company);
        //결과 반환
        CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
         List<DividendEntity> dividendEntityList= scrapedResult.getDividendList().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());
         dividendRepository.saveAll(dividendEntityList);
        return company;
    }

    public List<String> getCompanyNamesByKeyword(String keyword){
        Pageable limit = QPageRequest.of(0, 10);

        return companyRepository.findByNameStartingWithIgnoreCase(keyword, limit)
                .stream().map(e -> e.getName())
                .collect(Collectors.toList());
    }

    public void addAutoCompleteKeyword(String keyword){
        trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword){
        return (List<String>) trie.prefixMap(keyword).keySet()
                .stream().collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword){
        trie.remove(keyword);
    }
}
