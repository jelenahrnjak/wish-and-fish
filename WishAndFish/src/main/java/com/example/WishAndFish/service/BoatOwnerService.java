package com.example.WishAndFish.service;

import com.example.WishAndFish.dto.AddressDTO;
import com.example.WishAndFish.dto.BoatDTO;
import com.example.WishAndFish.dto.CottageDTO;
import com.example.WishAndFish.dto.UserDTO;
import com.example.WishAndFish.model.*;
import com.example.WishAndFish.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoatOwnerService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    LoyaltyCategoryRepository loyaltyCategoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoatOwnerRepository boatOwnerRepository;

    @Autowired
    private BoatRepository boatRepository;

    public User save(UserDTO requestUser) {
        AddressDTO a = requestUser.getAddress();
        BoatOwner user = new BoatOwner(passwordEncoder.encode(requestUser.getPassword()), requestUser.getEmail(), requestUser.getName(), requestUser.getSurname(), requestUser.getPhoneNumber());
        Address address = new Address(a.getStreet(),a.getStreetNumber(),a.getPostalCode(),a.getCityName(), a.getCountryName(),a.getLongitude(),a.getLatitude());
        user.setAddress(address);
        user.setLoyaltyCategory(loyaltyCategoryRepository.findByLevel(1));
        user.setVerificationCode(requestUser.getVerificationCode());
        user.setReasonForRegistration(requestUser.getReasonForRegistration());

        Role role = roleRepository.findByName(requestUser.getRoleName());
        user.setRole(role);

        return this.boatOwnerRepository.save(user);
    }

    public List<BoatDTO> getBoatsFromOwner(String email){
        List<BoatDTO> ret = new ArrayList<BoatDTO>();
        for(Boat b: boatRepository.findAll()){
            User u = userRepository.findByEmail(b.getBoatOwner().getEmail());
            if(u.getEmail().equals(email)){
                ret.add(new BoatDTO(b));
            }
        }
        return  ret;
    }
}
