package hotelmanagertests;

import hotelmanager.Gender;
import hotelmanager.Guest;
import hotelmanager.GuestComparator;
import hotelmanager.GuestManagerImpl;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Before
    public void setUp() {
        this.manager = new HotelManagerImpl();
        this.roomM = new RoomManagerImpl();
        this.guestM = new GuestManagerImpl();
        
        r1 = newRoom(5, 1, 1, "Nice room.");
        r2 = newRoom(3, 1, 2, "Great room with good smelt.");
        r3 = newRoom(4, 1, 3, "Bad room.");
        g1 = newGuest("Jaroslav", "Šlechtický", "123456789", Gender.MALE);
        g2 = newGuest("Klára", "Malinká", "453456789", Gender.FEMALE);
        g3 = newGuest("Václav", "Veliká", "123456789", Gender.MALE);
        g4 = newGuest("Petr", "Nedržbach", "345456789", Gender.MALE);
        g5 = newGuest("Jana", "Horšová", "456789789", Gender.FEMALE);
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
        assertEquals(true, (free1.size() == free2.size() + 1));
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
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            manager.accommodateGuestInRoom(guest, null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            manager.accommodateGuestInRoom(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

    }

    @Test
    public void removeGuestFromRoomTest() {
        Room room = newRoom(4, 1, 1, "Poznámka");
        Room room2 = newRoom(4, 2, 2, "Poznámka2");
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
        try {
            manager.removeGuestFromRoom(guest1, room);
            fail();
        } catch (Exception ex) {
            // OK
        }

        // removing guest from wrong room
        try {
            manager.removeGuestFromRoom(guest3, room2);
            fail();
        } catch (Exception ex) {
            // OK
        }

        // wrong arguments:
        try {
            manager.removeGuestFromRoom(guest3, null);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        try {
            manager.removeGuestFromRoom(null, room);
            fail();
        } catch (IllegalArgumentException ex) {
            // OK
        }

        try {
            manager.removeGuestFromRoom(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
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
        Collections.sort(result, new GuestComparator());
        Collections.sort(expected, new GuestComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionGuest(expected, result);
    }

    @Test
    public void getGuestsOfRoomWrongAttribute() {
        Room room = newRoom(5, 1, 1, "Fine");
        roomM.createNewRoom(room);
        Long roomId = room.getId();

        try {
            manager.getGuestsOfRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = roomM.getRoomById(roomId);
            room.setId(null);
            manager.getGuestsOfRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = roomM.getRoomById(roomId);
            room.setId(roomId - 1);
            manager.getGuestsOfRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    @Test
    public void findAllFreeRooms() {
        guestM.createNewGuest(g1);
        guestM.createNewGuest(g2);
        guestM.createNewGuest(g3);
        guestM.createNewGuest(g4);
        guestM.createNewGuest(g5);
        roomM.createNewRoom(r1);
        roomM.createNewRoom(r2);
        roomM.createNewRoom(r3);

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
        expected = Arrays.asList(r1, r3);
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
    }
}
