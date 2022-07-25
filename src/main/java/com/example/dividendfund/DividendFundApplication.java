package com.example.dividendfund;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.scraper.YahooFinanceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

import static org.jsoup.Jsoup.connect;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendFundApplication {
	public static void main(String[] args) {
		SpringApplication.run(DividendFundApplication.class, args);
	}

}
