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
    public void findAllFreeRooms() {
            //Creating
        Room room = newRoom(5, 1, 1, "A");
        Room room2 = newRoom(5, 1, 1, "A");
        Room room3 = newRoom(5, 1, 1, "A");
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
     
    private static Comparator<Room> idComparator = new Comparator<Room>() {

        @Override
        public int compare(Room o1, Room o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
        
    };
}
