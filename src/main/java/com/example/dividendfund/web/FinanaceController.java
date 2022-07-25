package com.example.dividendfund.web;

import com.example.dividendfund.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanaceController {

    private final FinanceService financeService;

    @GetMapping("/devidend/{companyName}")
    public ResponseEntity<?> searchFinanace(@PathVariable String companyName){
        var result = financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }

}
