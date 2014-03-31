package hotelmanagertests;

import hotelmanager.DBUtils;
import hotelmanager.Gender;
import hotelmanager.Guest;
import hotelmanager.GuestComparator;
import hotelmanager.GuestManagerImpl;
import hotelmanager.HotelManager;
import hotelmanager.HotelManagerImpl;
import hotelmanager.Room;
import hotelmanager.RoomComparator;
import hotelmanager.RoomManagerImpl;
import static hotelmanagertests.RoomManagerImplTest.assertDeepEqualsRoom;
import static hotelmanagertests.RoomManagerImplTest.assertDeepEqualsCollectionRoom;
import static hotelmanagertests.RoomManagerImplTest.newRoom;
import static hotelmanagertests.GuestManagerImplTest.assertDeepEqualsGuest;
import static hotelmanagertests.GuestManagerImplTest.assertDeepEqualsCollectionGuest;
import static hotelmanagertests.GuestManagerImplTest.newGuest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class testing all method in HotelManagerImpl
 *
 * @author Petr
 */
public class HotelManagerImplTest {
    private HotelManagerImpl manager;
    private RoomManagerImpl roomM;
    private GuestManagerImpl guestM;
    private Room r1, r2, r3;
    private Guest g1, g2, g3, g4, g5;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:HotelManagerDatabaseTest;create=true");
        return ds;
    }
    
    
    @Before
    public void setUp()  throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds, HotelManager.class.getResource("createTables.sql"));
        
        this.manager = new HotelManagerImpl();
        this.manager.setDataSource(ds);
        this.guestM = new GuestManagerImpl();
        this.guestM.setDataSource(ds);
        this.roomM = new RoomManagerImpl();
        this.roomM.setDataSource(ds);
        
        
        
        r1 = newRoom(5, 1, 1, "Nice room.");
        r2 = newRoom(3, 1, 2, "Great room with good smelt.");
        r3 = newRoom(4, 1, 3, "Bad room.");
        g1 = newGuest("Jaroslav", "Šlechtický", "123456789", Gender.MALE);
        g2 = newGuest("Klára", "Malinká", "453456789", Gender.FEMALE);
        g3 = newGuest("Václav", "Veliká", "127456789", Gender.MALE);
        g4 = newGuest("Petr", "Nedržbach", "345456789", Gender.MALE);
        g5 = newGuest("Jana", "Horšová", "456789789", Gender.FEMALE);
        
        guestM.createNewGuest(g1);
        guestM.createNewGuest(g2);
        guestM.createNewGuest(g3);
        guestM.createNewGuest(g4);
        guestM.createNewGuest(g5);
        roomM.createNewRoom(r1);
        roomM.createNewRoom(r2);
        roomM.createNewRoom(r3);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, HotelManager.class.getResource("dropTables.sql"));
    }

    
    /**
     * Testing accomodateGuestInRoom
     * AccomodateGuestInRoom method must accomodat sou getFreeRooms must return less room, and method getGuestOfRoom must return all guests in room
     * 
     * @author Petr
     */
    @Test
    public void accommodateGuestInRoomTest() {
        assertTrue(manager.getGuestsOfRoom(r1).isEmpty());
            //test if free rooms before and after+1 are equal
        List<Room> free1 = manager.findAllFreeRooms();
        
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1); // pridano aby pokoj byl komplente plny
        manager.accommodateGuestInRoom(g4, r1); // pridano aby pokoj byl komplente plny
        manager.accommodateGuestInRoom(g5, r1); // pridano aby pokoj byl komplente plny
        
        List<Room> free2 = manager.findAllFreeRooms();        
        assertEquals(true, (free1.size() == free2.size() + 1));
        
            //Test Equals to Coolection, get and made
        List<Guest> guestsOfR1 = manager.getGuestsOfRoom(r1);
        List<Guest> expected = Arrays.asList(g1, g2, g3, g4, g5);
        
        Collections.sort(guestsOfR1, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, guestsOfR1);
        assertDeepEqualsCollectionGuest(expected, guestsOfR1);
    }

    /**
     * Testing accomodateGuestInRoom with null
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNullRoom() {
        manager.accommodateGuestInRoom(g1, null);
    }
    
    /**
     * Testing accomodateGuestInRoom with null
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNullGuest() {
        manager.accommodateGuestInRoom(null, r1);
    }
    
    /**
     * Testing accomodateGuestInRoom with null
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNullGuestAndRoom() {
        manager.accommodateGuestInRoom(null, null);
    }
    
    /**
     * Testing accomodateGuestInRoom with null r1 id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNulIdR() {
        r1.setId(null);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
    /**
     * Testing accomodateGuestInRoom with null g1 id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNulIdG() {
        g1.setId(null);
        manager.accommodateGuestInRoom(g1, r1);
    }
    /**
     * Testing accomodateGuestInRoom with null g1 id and r1 id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithNulIdRG() {
        r1.setId(null);
        g1.setId(null);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
    /**
     * Testing accomodateGuestInRoom with WrongId
     * Good Zero
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdGZ() {
        g1.setId(0L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
        /**
     * Testing accomodateGuestInRoom with WrongId
     * Good Negative
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdGN() {
        g1.setId(-1L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
    /**
     * Testing accomodateGuestInRoom with WrongId
     * Zero Good
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdZG() {
        r1.setId(0L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
     /**
     * Testing accomodateGuestInRoom with WrongId
     * Negative Good
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdNG() {
        r1.setId(-1L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
     /**
     * Testing accomodateGuestInRoom with WrongId
     * zero zero
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdZZ() {
        r1.setId(0L);
        g1.setId(0L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
 /**
     * Testing accomodateGuestInRoom with WrongId
     * zero Negative
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdZN() {
        r1.setId(0L);
        g1.setId(-1L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
    /**
     * Testing accomodateGuestInRoom with WrongId
     * Negative zero
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdNZ() {
        r1.setId(-1L);
        g1.setId(0L);
        manager.accommodateGuestInRoom(g1, r1);
    }
    
     /**
     * Testing accomodateGuestInRoom with WrongId
     * Negative Negative
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomWithWrongIdNN() {
        r1.setId(-1L);
        g1.setId(-1L);
        manager.accommodateGuestInRoom(g1, r1);
    }  
    
    /**
     * Testing accomodateGuestInRoom Overfull
     * If we accomodate more guests than is capacity -> throw IllegalArgumentException
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInRoomOverfull() {
        manager.accommodateGuestInRoom(g1, r2);
        manager.accommodateGuestInRoom(g2, r2);
        manager.accommodateGuestInRoom(g3, r2);
        manager.accommodateGuestInRoom(g4, r2); //throw excpeiton
    }    
    
    /**
     * Testing accomodateGuestInRoom - accomodate A Guest to more Room-> throw IllegalArgumentException
     * Firt guest must be removed
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void accommodateGuestInMoreRooms() {
        manager.accommodateGuestInRoom(g1, r2);
        manager.accommodateGuestInRoom(g1, r3);//throw excpeiton
    }    
    
    /**
     * @author Vojta
     */
    @Test
    public void removeGuestFromRoomTest() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        // removing 1 guest:
        manager.removeGuestFromRoom(g1, r1);
        List<Guest> guests = manager.getGuestsOfRoom(r1);
        assertEquals(2, guests.size());
        List<Guest> expected = Arrays.asList(g2, g3);
        Collections.sort(guests, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, guests);
        assertDeepEqualsCollectionGuest(expected, guests);
        
        // removing all remaining guests:
        manager.removeGuestFromRoom(g2, r1);
        manager.removeGuestFromRoom(g3, r1);
        guests = manager.getGuestsOfRoom(r1);
        assertNotNull(guests); // No guests
        assertEquals(0, guests.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeRemovedGuest() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        manager.removeGuestFromRoom(g1, r1);
        manager.removeGuestFromRoom(g1, r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeGuestFromWrongRoom() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        manager.removeGuestFromRoom(g3, r2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeGuestFromNullRoom() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        manager.removeGuestFromRoom(g3, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeNullGuest() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        manager.removeGuestFromRoom(null, r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeNullGuestFromNullRoom() {
        manager.removeGuestFromRoom(null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeGuestWithNullIdFromRoom() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        Long id = g1.getId();
        Guest guest = guestM.getGuestById(id);
        guest.setId(null);
        manager.removeGuestFromRoom(guest, r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void removeGuestFromRoomWithNullId() {
        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        Long id = r1.getId();
        Room room = roomM.getRoomById(id);
        room.setId(null);
        manager.removeGuestFromRoom(g1, room);
    }
    
    
    /**
     * Testing GetGuestOfRoom method
     * 
     * @author Petr
     */
    @Test
    public void getGuestsOfRoomTest() {
        assertTrue(manager.getGuestsOfRoom(r1).isEmpty());
        assertTrue(manager.getGuestsOfRoom(r2).isEmpty());
        assertTrue(manager.getGuestsOfRoom(r3).isEmpty());

        manager.accommodateGuestInRoom(g1, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g3, r1);
        manager.accommodateGuestInRoom(g4, r3);
        
        assertFalse(manager.getGuestsOfRoom(r1).isEmpty());
        assertTrue(manager.getGuestsOfRoom(r2).isEmpty());
        assertFalse(manager.getGuestsOfRoom(r3).isEmpty());

        List<Guest> result1 = manager.getGuestsOfRoom(r1);
        List<Guest> expected1 = Arrays.asList(g1, g2, g3);
        
        List<Guest> result2 = manager.getGuestsOfRoom(r3);
        List<Guest> expected2 = Arrays.asList(g4);
        
        Collections.sort(result1, new GuestComparator());
        Collections.sort(expected1, new GuestComparator());
        assertEquals(expected1, result1);
        assertDeepEqualsCollectionGuest(expected1, result1);
        
        Collections.sort(result2, new GuestComparator());
        Collections.sort(expected2, new GuestComparator());
        assertEquals(expected2, result2);
        assertDeepEqualsCollectionGuest(expected2, result2);
    }

    /**
     * Testing GetGuestOfRoom method if is given null
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void getGuestsOfRoomWithNull() {
        manager.getGuestsOfRoom(null);
    }
    
    /**
     * Testing GetGuestOfRoom method if is given null Id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void getGuestsOfRoomWithNullId() {
        r1.setId(null);
        manager.getGuestsOfRoom(r1);
    }
    
    /**
     * Testing GetGuestOfRoom method if is given wrong id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void getGuestsOfRoomWithWrongIdZero() {
        r1.setId(0L);
        manager.getGuestsOfRoom(r1);
    }
    
    /**
     * Testing GetGuestOfRoom method if is given wrong id
     * 
     * @author Petr
     */
    @Test(expected = IllegalArgumentException.class)
    public void getGuestsOfRoomWithWrongIdNegative() {
        r1.setId(-1L);
        manager.getGuestsOfRoom(r1);
    }
    
    /**
     * Testing finding free rooms
     * 
     * @author Petr
     */
    @Test
    public void findAllFreeRoomsTest() {
            //When rooms are all free
        List<Room> result = manager.findAllFreeRooms();
        List<Room> expected = Arrays.asList(r1, r2, r3);
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
        
        manager.accommodateGuestInRoom(g1, r2);
        manager.accommodateGuestInRoom(g3, r2);
        manager.accommodateGuestInRoom(g4, r2);
        manager.accommodateGuestInRoom(g5, r1);
        
            //When we accommodate some Guest to room
        result = manager.findAllFreeRooms();
        expected = Arrays.asList(r1, r3); // pokoj r1 je prohlasen za volny pokud ma alespon 1 volne misto
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
    }
    
    /**
     * Testing finding free rooms without createRoom
     * 
     * @author Petr
     */
    @Test
    public void findAllFreeRoomsWithoutRoomTest() {
        roomM.deleteRoom(r1);
        roomM.deleteRoom(r2);
        roomM.deleteRoom(r3);
        
        List<Room> result = manager.findAllFreeRooms();
        List<Room> expected = Arrays.asList();
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
    }
    
    /**
     * Testing finding free rooms with all full rooms
     * 
     * @author Petr
     */
    @Test
    public void findAllFreeRoomsWithFullRoomTest() {
        Guest g6 = newGuest("Jana", "Horšová", "456789189", Gender.FEMALE);        
        guestM.createNewGuest(g6);
        Guest g7 = newGuest("Janaa", "Horšaová", "476181789", Gender.FEMALE);        
        guestM.createNewGuest(g7);
        Guest g8 = newGuest("Janaaa", "Horšovaá", "419189789", Gender.FEMALE);        
        guestM.createNewGuest(g8);
        Guest g9 = newGuest("Petr", "Horš", "719789719", Gender.MALE);        
        guestM.createNewGuest(g9);
        Guest g10 = newGuest("Pavel", "Horš", "781789789", Gender.MALE);        
        guestM.createNewGuest(g10);
        Guest g11 = newGuest("Pavela", "Horšaa", "789189781", Gender.MALE);        
        guestM.createNewGuest(g11);
        Guest g12 = newGuest("Pavelaa", "Horša", "789789712", Gender.MALE);        
        guestM.createNewGuest(g12);
        
        manager.accommodateGuestInRoom(g1, r2);
        manager.accommodateGuestInRoom(g3, r2);
        manager.accommodateGuestInRoom(g4, r2);
        manager.accommodateGuestInRoom(g5, r1);
        manager.accommodateGuestInRoom(g2, r1);
        manager.accommodateGuestInRoom(g6, r1);
        manager.accommodateGuestInRoom(g7, r1);
        manager.accommodateGuestInRoom(g8, r1);
        manager.accommodateGuestInRoom(g9, r3);
        manager.accommodateGuestInRoom(g10, r3);
        manager.accommodateGuestInRoom(g11, r3);
        manager.accommodateGuestInRoom(g12, r3);
        
            //When have full rooms
        List<Room> result = manager.findAllFreeRooms();
        List<Room> expected = Arrays.asList();
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
    }
}
