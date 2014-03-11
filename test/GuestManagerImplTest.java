/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import hotelmanager.Gender;
import hotelmanager.Guest;
import hotelmanager.GuestComparator;
import hotelmanager.GuestManagerImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vojta
 */
public class GuestManagerImplTest {

    private GuestManagerImpl guestManager;

    @Before
    public void setUp() {
        guestManager = new GuestManagerImpl();
    }

    @Test
    public void createNewGuest() {
        Guest guest = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
        List<Guest> guests = null;
        guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());

        // znovu pridani stejneho hosta

        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void createGuestWithWrongArguments() {
        try {
            guestManager.createNewGuest(null);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        Guest guest = newGuest(null, "Příjmení", "123456789", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", null, "123456789", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", null, Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", "-12345678", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", "-123456789", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", "@#*<>&ß$€", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("", "Příjmení", "123456789", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "", "123456789", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", "", Gender.MALE);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = newGuest("Jméno", "Příjmení", "123456789", null);
        try {
            guestManager.createNewGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void updateGuest() {
        Guest g1 = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        Guest g2 = newGuest("JménoB", "PříjmeníB", "987654321", Gender.FEMALE);
        guestManager.createNewGuest(g1);
        guestManager.createNewGuest(g2);
        Long guestID = g1.getId();

        Guest guest = guestManager.getGuestById(guestID);
        guest.setName("nove");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());

        guest.setSurname("noveB");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("noveB", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());

        guest.setIdentityCardNumber("012345678");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("noveB", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());

        guest.setGender(Gender.FEMALE);
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("noveB", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.FEMALE, guest.getGender());
    }

    @Test
    public void updateGuestWithWrongArguments() // similar to createGuestWithWrongArguments
    {
        Guest g1 = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        Guest g2 = newGuest("JménoB", "PříjmeníB", "987654321", Gender.FEMALE);
        guestManager.createNewGuest(g1);
        guestManager.createNewGuest(g2);
        Long guestID = g1.getId();

        Guest guest = guestManager.getGuestById(guestID);
        try {
            guestManager.updateGuest(null);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setName(null);
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setSurname(null);
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setIdentityCardNumber(null);
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setIdentityCardNumber("-12345678");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setIdentityCardNumber("-123456789");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setIdentityCardNumber("@#*<>&ß$€");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setName("");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setSurname("");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setIdentityCardNumber("");
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        guest = guestManager.getGuestById(guestID);
        try {
            guest.setGender(null);
            guestManager.updateGuest(guest);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void deleteGuest() {
        Guest guest = newGuest("JmenoA", "PrijmeniA", "111111111", Gender.MALE);
        Guest guest2 = newGuest("JmenoB", "PrijmeniB", "222222222", Gender.FEMALE);
        guestManager.createNewGuest(guest);
        guestManager.createNewGuest(guest2);

        guestManager.deleteGuest(guest);
        List<Guest> guests = null;
        guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());

        // delete deleted guest
        try {
            guestManager.deleteGuest(guest);
            fail();
        } catch (Exception ex) {
            // OK
        }

        // wrong atributes:
        try {
            guestManager.deleteGuest(null);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void findAllGuestsTest() {
        Guest g1 = newGuest("JmenoA", "PrijmeniA", "111111111", Gender.MALE);
        Guest g2 = newGuest("JmenoB", "PrijmeniB", "222222222", Gender.FEMALE);
        Guest g3 = newGuest("JmenoC", "PrijmeniC", "333333333", Gender.MALE);

        guestManager.createNewGuest(g1);
        guestManager.createNewGuest(g2);
        guestManager.createNewGuest(g3);

        List<Guest> result = guestManager.findAllGuests();
        List<Guest> expected = Arrays.asList(g1, g2, g3);
        Collections.sort(result, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, result);
        assertDeepEquals(expected, result);
    }

    @Test
    public void getGuestByIdTest() {
        Guest guest = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        Guest guest2 = newGuest("JménoB", "PříjmeníB", "987654321", Gender.FEMALE);

        guestManager.createNewGuest(guest);
        guestManager.createNewGuest(guest2);

        Long id = guest.getId();
        Guest result = guestManager.getGuestById(id);
        assertEquals(guest, result);

        try {
            result = guestManager.getGuestById(Long.valueOf(-1));
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    private static Guest newGuest(String name, String surname, String identityCardNumber, Gender gender) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setIdentityCardNumber(identityCardNumber);
        guest.setGender(gender);
        return guest;
    }

    private void assertDeepEquals(List<Guest> expectedList, List<Guest> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Guest expected = expectedList.get(i);
            Guest actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Guest expected, Guest actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getIdentityCardNumber(), actual.getIdentityCardNumber());
        assertEquals(expected.getGender(), actual.getGender());
    }
}
