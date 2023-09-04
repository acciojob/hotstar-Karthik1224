package com.driver.transformer;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.WebSeries;

public class WebSeriesTransformer {
    public static WebSeries convertDtoToEntity(WebSeriesEntryDto entryDto){
        WebSeries webSeries=new WebSeries();
        webSeries.setSeriesName(entryDto.getSeriesName());
        webSeries.setAgeLimit(entryDto.getAgeLimit());
        webSeries.setRating(entryDto.getRating());
        webSeries.setSubscriptionType(entryDto.getSubscriptionType());

        return webSeries;
    }
}