package com.example.dividendfund.exception.impl;

import com.example.dividendfund.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoCompanyException extends AbstractException {

    @Override
    public int getStatusCode(){
        return HttpStatus.BAD_REQUEST.value();
    }
    @Override
    public String getMessage(){
        return "존재하지않은 회사명입니다.";
    }
}
