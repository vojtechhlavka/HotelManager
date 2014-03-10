import hotelmanager.Gender;
import hotelmanager.Guest;
import hotelmanager.GuestManagerImpl;
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
    public void setUp()
    {
        guestManager = new GuestManagerImpl();
    }
    
    @Test
    public void createNewGuest()
    {
        Guest guest = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        guestManager.createNewGuest(guest);
        List<Guest> guests = null;
        guests = guestManager.findAllGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());
        
        // znovu pridani stejneho hosta
        
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
    }
    
    @Test
    public void createGuestWithWrongArguments()
    {
        try
        {
            guestManager.createNewGuest(null);
            fail();
        } 
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        Guest guest = newGuest(null, "Příjmení", "123456789", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", null, "123456789", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "Příjmení", null, Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "Příjmení", "-12345678", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "Příjmení", "@#*<>&ß$€", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("", "Příjmení", "123456789", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "", "123456789", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "Příjmení", "", Gender.MALE);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        guest = newGuest("Jméno", "Příjmení", "123456789", null);
        try
        {
            guestManager.createNewGuest(guest);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
    }
    
    @Test
    public void updateGuest()
    {
        Guest g1 = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        Guest g2 = newGuest("Jméno2", "Příjmení2", "987654321", Gender.FEMALE);
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
        
        guest.setSurname("nove2");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("nove2", guest.getSurname());
        assertEquals("123456789", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
        
        guest.setIdentityCardNumber("012345678");
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("nove2", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.MALE, guest.getGender());
        
        guest.setGender(Gender.FEMALE);
        guestManager.updateGuest(guest);
        guest = guestManager.getGuestById(guestID);
        assertEquals("nove", guest.getName());
        assertEquals("nove2", guest.getSurname());
        assertEquals("012345678", guest.getIdentityCardNumber());
        assertEquals(Gender.FEMALE, guest.getGender());
    }
    
    @Test
    public void deleteGuest()
    {
        Guest guest = new Guest();
        guestManager.createNewGuest(guest);
        guestManager.deleteGuest(guest);
        List<Guest> guests = null;
        guests = guestManager.findAllGuests();
        assertNull(guests);
    }
    
    private static Guest newGuest(String name, String surname, String identityCardNumber, Gender gender)
    {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setIdentityCardNumber(identityCardNumber);
        guest.setGender(gender);
        return guest;
    }
}
