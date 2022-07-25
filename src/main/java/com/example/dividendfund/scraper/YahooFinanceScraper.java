package com.example.dividendfund.scraper;

import com.example.dividendfund.model.Company;
import com.example.dividendfund.model.Dividend;
import com.example.dividendfund.model.ScrapedResult;
import com.example.dividendfund.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{

    private static final String STATIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;
    @Override
    public ScrapedResult scrap(Company company){
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);
        try{
            long endTime =  System.currentTimeMillis() / 1000;
            String url = String.format(STATIC_URL, company.getTicker(), START_TIME, endTime);
            Connection connection =  Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);
            Element tbody = tableEle.children().get(1);
            List<Dividend> dividendList = new ArrayList<>();

            for( Element e : tbody.children()){
                String txt = e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",",""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if(month <0){
                    throw new RuntimeException("unexpected Month value" + splits[0]);
                }

                dividendList.add(new Dividend(LocalDateTime.of(year, month, day,0,0), dividend));

            }
            scrapResult.setDividendList(dividendList);

        } catch (IOException e){
            e.printStackTrace();
        }
        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try{
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("H1").get(0);
            String[] titles = titleEle.text().split(" - ");//[1].trim();
            String title = titles[0].trim();
            if(titles.length > 1){
                title = titles[1].trim();
            }

            return new Company(ticker, title);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
