package com.example.dividendfund.web;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.constants.CacheKey;
import com.example.dividendfund.persist.entity.CompanyEntity;
import com.example.dividendfund.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final CacheManager redisCacheManager;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        var result = companyService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companyEntityList = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntityList);
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        Company company = companyService.save(ticker);
        companyService.addAutoCompleteKeyword(company.getName());
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker){
        String companyName = companyService.deleteCompany(ticker);
        clearFinanaceCache(companyName);
        return ResponseEntity.ok(companyName);
    }
    public void clearFinanaceCache(String companyName){
        redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}
