package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot=new Spot();
        Optional<ParkingLot> parkingLot=parkingLotRepository1.findById(parkingLotId);
        if(parkingLot.isEmpty())
        {
            throw new RuntimeException("ParkingLot Id Invalid");
        }
        ParkingLot parkingLot1=parkingLot.get();
        List<Spot> spotList=parkingLot1.getSpotList();
        spot.setParkingLot(parkingLot1);
        spot.setPricePerHour(pricePerHour);
        if(numberOfWheels<2)
        {
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels>=2&&numberOfWheels<4)
        {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else{
            spot.setSpotType(SpotType.OTHERS);
        }
        spotRepository1.save(spot);
        spot.setOccupied(false);
        spotList.add(spot);
        parkingLot1.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot1);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
         Optional<Spot> spotOptional=spotRepository1.findById(spotId);
         if(spotOptional.isEmpty())
         {
             throw new RuntimeException("Invalid Spot ID");
         }
         Spot spot=spotOptional.get();
         ParkingLot parkingLot=spot.getParkingLot();
         List<Spot> spotList=parkingLot.getSpotList();
         spotList.remove(spot);
         parkingLot.setSpotList(spotList);
         parkingLotRepository1.save(parkingLot);
         spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<Spot> spotOptional=spotRepository1.findById(spotId);
        if(spotOptional.isEmpty())
        {
            throw new RuntimeException("Spot ID invalid");
        }
        Spot spot=spotOptional.get();
        Optional<ParkingLot> parkingLotOptional=parkingLotRepository1.findById(parkingLotId);
        if(parkingLotOptional.isEmpty())
        {
            throw new RuntimeException("Parking Spot Invalid");
        }
        ParkingLot parkingLot=parkingLotOptional.get();
        List<Spot> spotList=parkingLot.getSpotList();
        spotList.remove(spot);

        spot.setPricePerHour(pricePerHour);

        Spot s1=spotRepository1.save(spot);
        spotList.add(s1);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        Optional<ParkingLot> parkingLotOptional=parkingLotRepository1.findById(parkingLotId);
        if(parkingLotOptional.isEmpty())
        {
            throw new RuntimeException("Parking Id not found");
        }
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
