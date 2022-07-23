package com.example.dividendfund.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @GetMapping("/finance/devidend/{companyName}")
    public ResponseEntity<?> searchFinanace(@PathVariable String companyName){
        return null;
    }

    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyWord){
        return null;
    }

    @GetMapping("/company")
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    @PostMapping("/compnay")
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
