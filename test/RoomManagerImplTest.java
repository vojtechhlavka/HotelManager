
import hotelmanager.Room;
import hotelmanager.RoomComparator;
import hotelmanager.RoomManagerImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr
 */
public class RoomManagerImplTest {

    private RoomManagerImpl manager;

    @Before
    public void setUp() {
        this.manager = new RoomManagerImpl();

        Room r1 = newRoom(5, 1, 1, "Great");
        Room r2 = newRoom(4, 1, 2, "Great");
        Room r3 = newRoom(5, 1, 3, "Bad");
    }

    @Test
    public void createNewRoom() {
        Room room = newRoom(5, 1, 1, "Good room");
        manager.createNewRoom(room);

        Long roomId = room.getId();
        assertNotNull(roomId);                      //Test for roomId musnt be null (-it must be set in createNewRoom()
        Room result = manager.getRoomById(roomId);  //??neni to zde ale zavisly na jine metode? to by nemelo byt ne?
        assertEquals(room, result);                 //Jsou stejné Grave a vysledek co jsem dostal podle id
        assertNotSame(room, result);
    }

    @Test
    public void updateRoom() {
        Room room = newRoom(5, 1, 1, "Nice room");
        Room room2 = newRoom(4, 2, 11, "PrettyRoom");
        manager.createNewRoom(room);
        manager.createNewRoom(room2);

        Long roomId = room.getId();
        room = manager.getRoomById(roomId);
        room.setCapacity(1);
        manager.updateRoom(room);
        assertEquals(1, room.getCapacity());
        assertEquals(1, room.getFloor());
        assertEquals(1, room.getNumber());
        assertEquals("Nice room", room.getNote());
        assertDeepEquals(room, manager.getRoomById(roomId));  //??vyberu z dtb pomoci getRoomById poku a porovnam ho s room ( upravenym pomoci set)
        //abych zjistit jestli update metoda to upravila i v dtb

        room = manager.getRoomById(roomId);
        room.setFloor(2);
        manager.updateRoom(room);
        assertEquals(1, room.getCapacity());
        assertEquals(2, room.getFloor());
        assertEquals(1, room.getNumber());
        assertEquals("Nice room", room.getNote());
        assertDeepEquals(room, manager.getRoomById(roomId));

        room = manager.getRoomById(roomId);
        room.setNumber(2);
        manager.updateRoom(room);
        assertEquals(1, room.getCapacity());
        assertEquals(2, room.getFloor());
        assertEquals(2, room.getNumber());
        assertEquals("Nice room", room.getNote());
        assertDeepEquals(room, manager.getRoomById(roomId));

        room = manager.getRoomById(roomId);
        room.setNote(null);
        assertEquals(1, room.getCapacity());
        assertEquals(2, room.getFloor());
        assertEquals(2, room.getNumber());
        assertNull(room.getNote());
        assertDeepEquals(room, manager.getRoomById(roomId));

        //Check if wasnt modificate other Room by update        
        assertDeepEquals(room2, manager.getRoomById(room2.getId()));
    }

    //dobre v novim junit
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomGood() {
        manager.updateRoom(null);
    }

    @Test
    public void updateRoomWithWrongAttribute() {
        Room room = newRoom(5, 1, 1, "Fine");
        manager.createNewRoom(room);
        Long roomId = room.getId();

        try {
            manager.updateRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK - ošetřeno null vyhozením výjimky
        }

        try {
            room = manager.getRoomById(roomId);
            room.setId(null);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setId(roomId - 1);             //test na nastavení Id který uz je pouzito
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setCapacity(0);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setCapacity(-1);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setFloor(-1);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setNumber(0);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setNumber(-1);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    @Test
    public void deleteRoom() {
        Room room = newRoom(5, 1, 1, "Fine");
        Room room2 = newRoom(4, 1, 2, "OK");
        manager.createNewRoom(room);
        manager.createNewRoom(room2);

        assertNotNull(manager.getRoomById(room.getId()));
        assertNotNull(manager.getRoomById(room2.getId()));
        manager.deleteRoom(room);
        assertNull(manager.getRoomById(room.getId()));
        assertNotNull(manager.getRoomById(room2.getId()));
    }

    @Test
    public void deleteRoomWithWrongAttribute() {
        Room room = newRoom(4, 1, 1, "Yes!!");
        manager.createNewRoom(room);

        try {
            manager.deleteRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room.setId(null);
            manager.deleteRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //Ok
        }
        try {
            room.setId(0L);
            manager.deleteRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //Ok
        }
    }

    @Test
    public void findAllRooms() {
        assertTrue(manager.findAllRooms().isEmpty());

        Room r1 = newRoom(5, 1, 1, "Great");
        Room r2 = newRoom(4, 1, 2, "Great.");
        Room r3 = newRoom(5, 1, 3, "Great");
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        manager.createNewRoom(r3);

        List<Room> result = manager.findAllRooms();
        List<Room> expected = Arrays.asList(r1, r2, r3);
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEquals(expected, result);
    }

    @Test
    public void getRoomByIdTest() {
        assertNull(manager.getRoomById(1l));

        Room r1 = newRoom(5, 1, 1, "Great");
        Room r2 = newRoom(4, 1, 2, "Great.");
        Room r3 = newRoom(5, 1, 3, "Great");
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        manager.createNewRoom(r3);

        Long roomId = r2.getId();

        Room result = manager.getRoomById(roomId);

        assertEquals(r3, result);
        assertDeepEquals(r3, result);
    }

    private Room newRoom(int capacity, int floor, int number, String note) {
        Room room = new Room();
        room.setCapacity(capacity);
        room.setFloor(floor);
        room.setNumber(number);
        room.setNote(note);
        return room;
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
}
