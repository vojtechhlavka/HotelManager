package hotelmanagertests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Přidaný komentář!
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
    private Guest guest1, guest2, guest3;

    @Before
    public void setUp() {
        guestManager = new GuestManagerImpl();
        guest1 = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        guest2 = newGuest("JménoB", "PříjmeníB", "987654321", Gender.FEMALE);
        guest3 = newGuest("JmenoC", "PrijmeniC", "333333333", Gender.MALE);
        guestManager.createNewGuest(guest1);
        guestManager.createNewGuest(guest2);
        guestManager.createNewGuest(guest3);
    }

    @Test
    public void createNewGuestTest() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "111111111", Gender.MALE);
        guestManager.createNewGuest(guest);
        List<Guest> guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(4, guests.size());
        Long id = guest.getId();
        guest = guestManager.getGuestById(id);
        assertNotNull(guest);
        assertEquals("Novéjméno", guest.getName());
        assertEquals("Novépříjmení", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createSameGuestAgain() {
        guestManager.createNewGuest(guest1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNull() {
        guestManager.createNewGuest(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNullName() {
        Guest guest = newGuest(null, "Novépříjmení", "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNullSurname() {
        Guest guest = newGuest("Novéjméno", null, "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNullIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", null, Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithSpaceName() {
        Guest guest = newGuest(" ", "Novépříjmení", "123456789", Gender.FEMALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithSpaceSurname() {
        Guest guest = newGuest("Novéjméno", " ", "123456789", Gender.FEMALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWIthSpaceIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", " ", Gender.FEMALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNegativeIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "-12345678", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNegativeAndLongIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "-123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithTooShortIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "12345678", Gender.MALE); // only 8 digits
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithTooLongIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "1234567890", Gender.MALE); // more than 9 digits
        guestManager.createNewGuest(guest);
    }
    
    @Test
    public void createGuestWithIdentityCardNumberBeginningWithZero() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "012345678", Gender.MALE);
        guestManager.createNewGuest(guest); // OK
        List<Guest> guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(4, guests.size()); // 3+1
        Long id = guest.getId();
        guest = guestManager.getGuestById(id);
        assertNotNull(guest);
        assertEquals("Novéjméno", guest.getName());
        assertEquals("Novépříjmení", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithZeroIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "000000000", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithAplhabetIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "abcdefghi", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithWrongIdentiyCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "@#*<>&ß$€", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNoName() {
        Guest guest = newGuest("", "Novépříjmení", "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNoSurname() {
        Guest guest = newGuest("Novéjméno", "", "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNoIdentityCardNumber() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "", Gender.MALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithNullGender() {
        Guest guest = newGuest("Novéjméno", "Novépříjmení", "123456789", null);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithWrongName() {
        Guest guest = newGuest("123", "Novépříjmení", "123456789", Gender.FEMALE);
        guestManager.createNewGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createGuestWithWrongSurname() {
        Guest guest = newGuest("Novéjméno", "123", "123456789", Gender.FEMALE);
        guestManager.createNewGuest(guest);
    }

    @Test
    public void updateNameOfGuestTest() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setName("Nové");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(id);
        assertEquals("Nové", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test
    public void updateSurnameOfGuest() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setSurname("Nové");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(id);
        assertEquals("Jméno", guest.getName());
        assertEquals("Nové", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test
    public void updateIdentityCardNumberOfGuest() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("012345678");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(id);
        assertEquals("Jméno", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test
    public void updateGenderOfGuest() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setGender(Gender.FEMALE);
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(id);
        assertEquals("Jméno", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.FEMALE, guest.getGender());
    }

    @Test (expected = IllegalArgumentException.class)
    public void updateNullGuest() {
        guestManager.updateGuest(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNullName() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setName(null);
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNullSurname() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setSurname(null);
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNullIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber(null);
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithSpaceName() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setName(" ");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithSpaceSurname() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setSurname(" ");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWIthSpaceIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber(" ");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void udpateGuestWithNegativeIdentityCardNumber () {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("-12345678");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNegateveAndLongIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("-123456789");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithTooShortIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("12345678"); // only 8 digits
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithTooLongIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("1234567890"); // more than 9 digits
        guestManager.updateGuest(guest);
    }
    
    @Test
    public void updateGuestWithIdentityCardNumberBeginningWithZero() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("012345678");
        guestManager.updateGuest(guest); // OK
        guest = guestManager.getGuestById(id);
        assertEquals("Jméno", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithZeroIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("000000000");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithAplhabetIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("abcdefghi");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithIncorrectIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("@#*<>&ß$€");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNoName() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setName("");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNoSurname() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setSurname("");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNoIdentityCardNumber() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setIdentityCardNumber("");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNullGender() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setGender(null);
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNullId() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setId(null);
        guestManager.updateGuest(guest);
    }
    
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithWrongName() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setName("123");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithWrongSurname() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setSurname("123");
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithNegativeId() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setId(-1L);
        guestManager.updateGuest(guest);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void updateGuestWithUsedId() {
        Long id1 = guest1.getId();
        Long id2 = guest2.getId();
        Guest guest = guestManager.getGuestById(id1);
        guest.setId(id2);
        guestManager.updateGuest(guest);
    }
    
    @Test
    public void updateGuestWithSameId() {
        Long id = guest1.getId();
        Guest guest = guestManager.getGuestById(id);
        guest.setId(id);
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(id);
        assertEquals("Jméno", guest.getName());
        assertEquals("Příjmení", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
    }
    
    @Test
    public void deleteGuestTest() {
        guestManager.deleteGuest(guest1);
        List<Guest> guests = guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(2, guests.size());
        // checking whether the correct guest was deleted
        List<Guest> expected = Arrays.asList(guest2, guest3);
        Collections.sort(guests, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, guests);
        assertDeepEqualsCollectionGuest(expected, guests);
        // deleting all remaining guests:
        guestManager.deleteGuest(guest2);
        guestManager.deleteGuest(guest3);
        guests = guestManager.findAllGuests();
        assertNotNull(guests); // No guests
        assertEquals(0, guests.size());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void deleteDeletedGuest() {
        guestManager.deleteGuest(guest1);
        guestManager.deleteGuest(guest1);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void deleteNullGuest() {
        guestManager.deleteGuest(null);
    }

    @Test
    public void findAllGuestsTest() {
        List<Guest> result = guestManager.findAllGuests();
        List<Guest> expected = Arrays.asList(guest1, guest2, guest3);
        Collections.sort(result, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionGuest(expected, result);
    }

    @Test
    public void getGuestByIdTest() {
        Long id = guest1.getId();
        Guest result = guestManager.getGuestById(id);
        assertEquals(guest1, result);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void getGuestByNullId() {
        Guest result = guestManager.getGuestById(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void getGuestByNegativeId() {
        Guest result = guestManager.getGuestById(-1L);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void getGuestByZeroId() {
        Guest result = guestManager.getGuestById(0L);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void getGuestByNonexistingId() {
        Long id = guest1.getId();
        guestManager.deleteGuest(guest1); // ID id doesn't exist now
        Guest guest = guestManager.getGuestById(id);
    }

    static Guest newGuest(String name, String surname, String identityCardNumber, Gender gender) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setIdentityCardNumber(identityCardNumber);
        guest.setGender(gender);
        return guest;
    }

    static void assertDeepEqualsCollectionGuest(List<Guest> expectedList, List<Guest> actualList) {
        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Guest expected = expectedList.get(i);
            Guest actual = actualList.get(i);
            assertDeepEqualsGuest(expected, actual);
        }
    }

    static void assertDeepEqualsGuest(Guest expected, Guest actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getIdentityCardNumber(), actual.getIdentityCardNumber());
        assertEquals(expected.getGender(), actual.getGender());
    }
}