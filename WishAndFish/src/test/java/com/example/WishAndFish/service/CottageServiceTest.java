package com.example.WishAndFish.service;

import com.example.WishAndFish.constants.AddressConstants;
import com.example.WishAndFish.constants.BoatsConstants;
import com.example.WishAndFish.constants.ClientConstants;
import com.example.WishAndFish.constants.CottageConstants;
import com.example.WishAndFish.dto.*;
import com.example.WishAndFish.model.*;
import com.example.WishAndFish.repository.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.WishAndFish.constants.AddressConstants.*;
import static com.example.WishAndFish.constants.AddressConstants.DB_ADDRESS_ID;
import static com.example.WishAndFish.constants.CottageConstants.*;
import static com.example.WishAndFish.constants.FishingAdventureConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CottageServiceTest {
    @Mock
    private CottageRepository cottageRepositoryMock;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private CottageOwnerRepository cottageOwnerRepository;

    @Mock
    private Cottage cottageMock;

    @InjectMocks
    private CottageService cottageService;

    @InjectMocks
    private CottageOwnerService cottageOwnerService;

    @Test
    public void testGetAllFromOwner() {
        // 1. Definisanje ponašanja mock objekata
        Cottage c1 = new Cottage();
        c1.setId(id1);
        c1.setName(CottageConstants.name);
        User u = new User();
        u.setId(CottageConstants.id2);
        u.setEmail(CottageConstants.email);
        c1.setCottageOwner(new CottageOwner(u));
        c1.setAddress(new Address (AddressConstants.street,AddressConstants.streetNumber,
                AddressConstants.postalCode,AddressConstants.city,AddressConstants.country,
                AddressConstants.lng,AddressConstants.ltd));
        c1.setDeleted(false);

        Cottage c2 = new Cottage();
        c2.setId(CottageConstants.id2);
        c2.setName(CottageConstants.name2);
        c2.setAddress(new Address (AddressConstants.street,AddressConstants.streetNumber,
                AddressConstants.postalCode,AddressConstants.city,AddressConstants.country,
                AddressConstants.lng,AddressConstants.ltd));
        User u2 = new User();
        u2.setId(CottageConstants.id3);
        u2.setEmail(CottageConstants.email2);
        c2.setCottageOwner(new CottageOwner(u2));
        c2.setDeleted(false);


        when(cottageRepositoryMock.findAll()).thenReturn(Arrays.asList(c1, c2));
        when(userRepositoryMock.findByEmail(u.getEmail())).thenReturn(u);

        // 2. Akcija
        List<CottageDisplayDTO> cottages = cottageOwnerService.getCottagesFromOwner(u.getEmail());

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        assertThat(cottages).hasSize(1);
        assertEquals(id1, cottages.get(0).getId());

		/*
		Možemo verifikovati ponašanje mokovanih objekata pozivanjem verify* metoda.
		 */
        verify(cottageRepositoryMock, times(1)).findAll();
        verify(userRepositoryMock, times(1)).findByEmail(u.getEmail());

        verifyNoMoreInteractions(cottageRepositoryMock);
    }


    @Test
    public void getByIdTest() {
        Cottage c = new Cottage();
        c.setId(id1);
        c.setName(CottageConstants.name3);
        c.setBedsPerRoom(CottageConstants.beds);
        c.setNumberOfRooms(CottageConstants.rooms);
        when(cottageRepositoryMock.findById(id1)).thenReturn(Optional.of(cottageMock));

        Cottage cottage = cottageService.findCottage(id1);

        assertEquals(cottage, cottageMock);
        verify(cottageRepositoryMock, times(1)).findById(id1);
        verifyNoMoreInteractions(cottageRepositoryMock);
    }


    @Test
    public void testSearch() {
        Address address = new Address(DB_STREET, DB_NUMBER, DB_POSTAL, DB_CITY, DB_COUNTRY);
        address.setId(DB_ADDRESS_ID);

        Cottage c1 = new Cottage(id1, name, rating, price, address);
        Cottage c2 = new Cottage(id2, name2, rating, price, address);
        Cottage c3 = new Cottage(id3, name3, rating, price, address);

        when(cottageRepositoryMock.findAll((Sort.by(Sort.Direction.ASC, "pricePerDay")))).thenReturn(Arrays.asList(c1,c2,c3));

        CottageDTO dto = new CottageDTO("ho", "", "3.4", "150.5");

        List<Cottage> cottages = cottageService.search(dto);

        assertThat(cottages).hasSize(1);
        Assertions.assertEquals(cottages.get(0).getName(), name3);

        verify(cottageRepositoryMock, times(1)).findAll((Sort.by(Sort.Direction.ASC, "pricePerDay")));
        verifyNoMoreInteractions(cottageRepositoryMock);
    }

    @Test
    public void testSearchEmpty() {

        Address address = new Address(DB_STREET, DB_NUMBER, DB_POSTAL, DB_CITY, DB_COUNTRY);
        address.setId(DB_ADDRESS_ID);

        Cottage c1 = new Cottage(id1, name, rating, price, address);
        Cottage c2 = new Cottage(id2, name2, rating, price, address);
        Cottage c3 = new Cottage(id3, name3, rating, price, address);

        when(cottageRepositoryMock.findAll((Sort.by(Sort.Direction.ASC, "pricePerDay")))).thenReturn(Arrays.asList(c1,c2,c3));

        CottageDTO dto = new CottageDTO("ho", "", "3.4", "150.0");

        List<Cottage> cottages = cottageService.search(dto);

        assertThat(cottages).hasSize(0);

        verify(cottageRepositoryMock, times(1)).findAll((Sort.by(Sort.Direction.ASC, "pricePerDay")));
        verifyNoMoreInteractions(cottageRepositoryMock);
    }


}
