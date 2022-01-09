package com.example.WishAndFish.service;

import com.example.WishAndFish.dto.BoatDTO;
import com.example.WishAndFish.model.Boat;
import com.example.WishAndFish.repository.BoatRepository;
import com.example.WishAndFish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BoatDTO> findAll() {

        List<BoatDTO> ret = new ArrayList<BoatDTO>();
        for(Boat b : boatRepository.findAll((Sort.by(Sort.Direction.ASC, "name")))){
            if(!b.isDeleted()){
            ret.add(new BoatDTO(b));}
        };

        return ret;
    }

    public List<BoatDTO> search(BoatDTO dto) {
        List<BoatDTO> ret = new ArrayList<BoatDTO>();
        double rating = 0;
        try{
            rating = Double.parseDouble(dto.getRating());
        }catch (Exception e){
            System.out.println("Error with parsing rating");
        }
        for(Boat b : boatRepository.findAll((Sort.by(Sort.Direction.ASC, "name")))){
            if(checkBoatForSearch(b,dto,rating) && !b.isDeleted()){
                ret.add(new BoatDTO(b));}
        }

        return ret;
    }

    private boolean checkBoatForSearch(Boat b, BoatDTO dto,double rating){

        if(checkStrings(b.getName(),dto.getName()) && checkStrings(b.getDescription(),dto.getDescription()) && checkStrings(b.getAddress().toString(),dto.getAddress()) && checkRating(b.getRating(),rating)){
            return true;
        }
        return false;
    }

    private boolean checkStrings(String boat, String search){
        if(search==null){
            return true;
        }
        if(search.isEmpty() || boat.toLowerCase().contains(search.toLowerCase())){
            return true;
        }
        return false;
    }

    private boolean checkRating(Double boat, Double search){
        if(boat >= search || boat==0 || search > 5){
            return true;
        }
        return false;
    }

}
