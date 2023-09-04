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
         int count=0;
         List<WebSeries>webSeriesList = webSeriesRepository.findAll();
         User userObj = userRepository.findById(userId).get();
         Subscription subObj = subscriptionRepository.findById(userId).get();
         for(WebSeries obj : webSeriesList)
         {
             if(obj.getAgeLimit() <= userObj.getAge() && obj.getSubscriptionType()==subObj.getSubscriptionType())
             {
                 count++;
             }
         }

        return count;
    }


}
