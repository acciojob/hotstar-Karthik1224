package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User userObj = userRepository.save(user);
        return userObj.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user =userRepository.findById(userId).get();
        Integer age=0;
        if(user.getAge()<=18)age=18;
        else age=Integer.MAX_VALUE;
        List<WebSeries> webSeriesList= webSeriesRepository.findAll();

        Integer count=0;

        for(WebSeries webSeries:webSeriesList){
            if(webSeries.getAgeLimit()<=age)count++;
        }


        return count;
    }


}
