import hotelmanager.Gender;
import hotelmanager.Guest;
import hotelmanager.GuestManagerImpl;
import hotelmanager.HotelManagerImpl;
import hotelmanager.Room;
import hotelmanager.RoomManagerImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Petr
 */
public class HotelManagerImplTest {
    
    private HotelManagerImpl manager;
    private RoomManagerImpl roomM;
    private GuestManagerImpl guestM;
      
    
    @Before
    public void setUp() {
        this.manager = new HotelManagerImpl();
        this.roomM = new RoomManagerImpl();
        this.guestM = new GuestManagerImpl();
    }
  
    @Test
    public void accommodateGuestInRoom() {
        Room room = newRoom(5, 1, 1, "B");
        Room room2 = newRoom(5, 1, 2, "B");
        Guest guest = newGuest("Lukáš", "Novák", "4569746/6447", Gender.FEMALE);
        guestM.createNewGuest(guest);
        roomM.createNewRoom(room);
        roomM.createNewRoom(room2);
        
        List<Room> free1 = manager.findAllFreeRooms();
        
        manager.accommodateGuestInRoom(guest, room);
        
        List<Room> free2 = manager.findAllFreeRooms();
            //test if free rooms before and after+1 are equal
        assertEquals(true, (free1.size() == free2.size()+1) );
            //jeste otestovat zda kdyz vypisu inf tak tam bude dany guest
    }
    
    @Test
    public void accommodateGuestInRoomWithWrongAttribute() {
        Room room = newRoom(5, 1, 1, "B");
        Guest guest = newGuest("Lukáš", "Novák", "4569746/6447", Gender.FEMALE);
        guestM.createNewGuest(guest);
        roomM.createNewRoom(room);      

        try {
            manager.accommodateGuestInRoom(null, room);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }
        
        try {
            manager.accommodateGuestInRoom(guest, null);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }
        
        try {
            manager.accommodateGuestInRoom(null, null);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }

    }
    
    
    
    @Test
    public void removeGuestFromRoomTest()
    {
        Room room = newRoom(4,1,1,"Poznámka");
        Room room2 = newRoom(4,2,2,"Poznámka2");
        Guest guest1 = newGuest("Jméno", "Příjmení", "123456789", Gender.MALE);
        Guest guest2 = newGuest("Jméno2", "Příjmení2", "987654321", Gender.FEMALE);
        Guest guest3 = newGuest("Jméno3", "Příjmení3", "111111111", Gender.MALE);
        
        manager.accommodateGuestInRoom(guest1, room);
        manager.accommodateGuestInRoom(guest2, room);
        manager.accommodateGuestInRoom(guest3, room);
        
        manager.removeGuestFromRoom(guest1, room);
        List<Guest> guests = manager.getGuestsOfRoom(room);
        assertEquals(2, guests.size());
        
        manager.removeGuestFromRoom(guest2, room);
        guests = manager.getGuestsOfRoom(room);
        assertEquals(1, guests.size());
        
        // removing removed guest:
        try
        {
            manager.removeGuestFromRoom(guest1, room);
            fail();
        }
        catch (Exception ex)
        {
            // OK
        }
        
        // removing guest from wrong room
        try
        {
            manager.removeGuestFromRoom(guest3, room2);
            fail();
        }
        catch (Exception ex)
        {
            // OK
        }
        
        // wrong arguments:
        try
        {
            manager.removeGuestFromRoom(guest3, null);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        try
        {
            manager.removeGuestFromRoom(null, room);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        try
        {
            manager.removeGuestFromRoom(null, null);
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            // OK
        }
        
        // checking the number of guest after wrong operatoins:
        guests = manager.getGuestsOfRoom(room);
        assertEquals(1, guests.size());
    }
    
    
    
    @Test
    public void getGuestsOfRoom() {
        Room room = newRoom(5, 1, 1, "A");
        Guest guest = newGuest("Jan", "Obršálek", "12454977/4567", Gender.MALE);
        Guest guest2 = newGuest("Jan", "Novotný", "78964977/7867", Gender.MALE);
        Guest guest3 = newGuest("Petra", "Nováková", "12857987/4589", Gender.FEMALE);
        guestM.createNewGuest(guest);
        guestM.createNewGuest(guest2);
        guestM.createNewGuest(guest3);
        roomM.createNewRoom(room);
        
        Long roomId = room.getId();
        room = roomM.getRoomById(roomId);
        
        manager.accommodateGuestInRoom(guest, room);
        manager.accommodateGuestInRoom(guest2, room);
        manager.accommodateGuestInRoom(guest3, room);
        
        List<Guest> result = manager.getGuestsOfRoom(room);
        List<Guest> expected = Arrays.asList(guest, guest2, guest3);
        assertEquals(expected, result);
        assertDeepEqualsGuest(expected, result);        
    }
    
    @Test
    public void getGuestsOfRoomWrongAttribute() {
        Room room = newRoom(5, 1, 1, "Fine");
        roomM.createNewRoom(room);
        Long roomId = room.getId();
        
        try {
            manager.getGuestsOfRoom(null);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }
        
        try {
            room = roomM.getRoomById(roomId);
            room.setId(null);
            manager.getGuestsOfRoom(room);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }
        
        try {
            room = roomM.getRoomById(roomId);
            room.setId(roomId-1);
            manager.getGuestsOfRoom(room);
            fail();
        } catch(IllegalArgumentException ex) {
            //OK
        }
    }
    
    @Test
    public void findAllFreeRooms() {
            //Creating
        Room room = newRoom(5, 1, 1, "A");
        Room room2 = newRoom(5, 1, 2, "A");
        Room room3 = newRoom(5, 1, 3, "A");
        Guest guest = newGuest("Jan", "Obršálek", "12454977/4567", Gender.MALE);
        guestM.createNewGuest(guest);
        roomM.createNewRoom(room);
        roomM.createNewRoom(room2);
        roomM.createNewRoom(room3);
                        
            //AccommodateGuest to room
        manager.accommodateGuestInRoom(guest, room);
            
            //Find All free rooms
        List<Room> result = manager.findAllFreeRooms();
            //Testing
        List<Room> expected = Arrays.asList(room2, room3);
        Collections.sort(result, idComparator);
        Collections.sort(expected, idComparator);
        assertEquals(expected, result);
        assertDeepEquals(expected, result);        
    }
    
    private Room newRoom(int capacity, int floor, int number, String note) {
        Room room = new Room();
        room.setCapacity(capacity);
        room.setFloor(floor);
        room.setNumber(number);
        room.setNote(note);        
        return room;        
    }

    private Guest newGuest(String name, String surname, String identityCardNumber, Gender gender) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setIdentityCardNumber(identityCardNumber);   
        guest.setGender(gender);
        return guest;   
    }
    
    private void assertDeepEqualsGuest(Guest expected, Guest actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getIdentityCardNumber(), actual.getIdentityCardNumber());
        assertEquals(expected.getGender(), actual.getGender());
    }
    
    private void assertDeepEquals(Room expected, Room actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        assertEquals(expected.getFloor(), actual.getFloor());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getNote(), actual.getNote());
    }
    
    private void assertDeepEquals(List<Room> expectedList, List<Room> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Room expected = expectedList.get(i);
            Room actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }
    
    private void assertDeepEqualsGuest(List<Guest> expectedList, List<Guest> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Guest expected = expectedList.get(i);
            Guest actual = actualList.get(i);
            assertDeepEqualsGuest(expected, actual);
        }
    }
     
    private static Comparator<Room> idComparator = new Comparator<Room>() {

        @Override
        public int compare(Room o1, Room o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
        
    };
}
