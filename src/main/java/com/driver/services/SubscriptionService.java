package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.transformer.SubscriptionTransformer;
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
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = SubscriptionTransformer.convertDtoToEntity(subscriptionEntryDto);
        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int noOfScreen = subscription.getNoOfScreensSubscribed();

        int priceOdSubscription = 0;

        if(subscriptionType.equals(SubscriptionType.BASIC)){
            priceOdSubscription = 500 + (200 * noOfScreen);
        }else if(subscriptionType.equals(SubscriptionType.PRO)){
            priceOdSubscription = 800 + (250 * noOfScreen);
        }else{
            priceOdSubscription = 1000 + (350 * noOfScreen);
        }
        subscription.setTotalAmountPaid(priceOdSubscription);
        subscription.setUser(user);
        Date date = new Date();
        subscription.setStartSubscriptionDate(date);

        user.setSubscription(subscription);

        return subscription.getTotalAmountPaid();
    }
    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType().toString().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }

        Subscription subscription=user.getSubscription();
        Integer previousFair=subscription.getTotalAmountPaid();
        Integer currentFair;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            currentFair =previousFair+300+(50*subscription.getNoOfScreensSubscribed());
        }else {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            currentFair=previousFair+200+(100*subscription.getNoOfScreensSubscribed());
        }

        subscription.setTotalAmountPaid(currentFair);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);

        return currentFair-previousFair;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        Integer totalRevenue = 0;
        for(Subscription subscription : subscriptionList){
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
