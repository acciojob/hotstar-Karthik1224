package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subObj = new Subscription();
        if(subscriptionEntryDto.getSubscriptionType()== SubscriptionType.BASIC)
        {
            subObj.setTotalAmountPaid(500);
        }
        else if(subscriptionEntryDto.getSubscriptionType()==SubscriptionType.PRO)
        {
            subObj.setTotalAmountPaid(800);
        }
        else {
            subObj.setTotalAmountPaid(1000);
        }
        subObj.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subObj.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subObj.setStartSubscriptionDate(new Date());
        User userObj = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subObj.setUser(userObj);
        userObj.setSubscription(subObj);
        userRepository.save(userObj);
        return subObj.getTotalAmountPaid();
    }
    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User userObj = userRepository.findById(userId).get();
        Subscription subObj = subscriptionRepository.findById(userId).get();
        Integer diff = 0;
        if(subObj.getSubscriptionType()==SubscriptionType.ELITE)
        {
            throw new Exception("Already the best Subscription");
        }
        else if(subObj.getSubscriptionType()==SubscriptionType.BASIC)
        {
            diff = 800-500;
            subObj.setSubscriptionType(SubscriptionType.PRO);
        }
        else if(subObj.getSubscriptionType()==SubscriptionType.PRO)
        {
            diff = 1000-800;
            subObj.setSubscriptionType(SubscriptionType.ELITE);
        }
        subscriptionRepository.save(subObj);
        return diff;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        Integer totalRevenue = 0;
        List<Subscription>subscriptions = subscriptionRepository.findAll();
        for(Subscription obj:subscriptions)
        {
            totalRevenue += obj.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
