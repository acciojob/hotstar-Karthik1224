package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()).equals(true))
        {
            throw new Exception("Series is already present");
        }
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        Double ratings = getRatingsofProductionHouse(productionHouse.getWebSeriesList(),webSeriesEntryDto.getRating());
        productionHouse.setRatings(ratings);
        webSeries.setProductionHouse(productionHouse);

        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
        webSeriesList.add(webSeries);

        productionHouseRepository.save(productionHouse);
        return webSeries.getId();
    }
    public Double getRatingsofProductionHouse(List<WebSeries>lists,double rate)
    {
        double count=0;
        for(WebSeries obj: lists)
        {
            count += obj.getRating();
        }
        count += rate;
        int noOfSeries = lists.size()+1;
        return count/noOfSeries;

    }

}
