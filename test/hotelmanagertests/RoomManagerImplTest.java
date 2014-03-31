package hotelmanagertests;

import hotelmanager.DBUtils;
import hotelmanager.Room;
import hotelmanager.RoomComparator;
import hotelmanager.RoomManagerImpl;
//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class testing all method in RoomManagerImpl

 * @author Petr
 */
public class RoomManagerImplTest {
    private RoomManagerImpl manager;
    private Room r1, r2, r3;
    
    //private Connection connection;
    private DataSource ds;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:HotelManagerTest;create=true");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        //--String url = "jdbc:derby://localhost:1527/HotelManagerDatabaseDemo;create=true";
        //--connection = DriverManager.getConnection(url, "HotelManager", "zFTY3Mv5GgZdiS");
        /*
        connection = DriverManager.getConnection("jdbc:derby:memory:HotelManagerDatabaseTest;create=true");
        connection.prepareStatement("CREATE TABLE ROOM ("
                + "id bigint primary key generated always as identity,"
                + "capacity int not null,"
                + "floor int not null,"
                + "number int not null,"
                + "note varchar(255))").executeUpdate();
        
        //--this.manager = new RoomManagerImpl();
        manager = new RoomManagerImpl(connection);
        */
        
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds, RoomManagerImpl.class.getResource("createTables.sql"));
        manager = new RoomManagerImpl();
        manager.setDataSource(ds);

        r1 = newRoom(5, 1, 1, "Nice room.");
        r2 = newRoom(5, 1, 2, "Great room with good smelt.");
        r3 = newRoom(4, 1, 3, "Bad room.");
    }
    
    @After
    public void tearDown() throws SQLException {
        /*
        connection.prepareStatement("DROP TABLE ROOM").executeUpdate();        
        connection.close();
        */
        DBUtils.executeSqlScript(ds, RoomManagerImpl.class.getResource("dropTables.sql"));
    }


    @Test
    public void createNewRoomTest() {
        manager.createNewRoom(r1);

        Long roomId = r1.getId();
        assertNotNull(roomId);                               //Test for roomId musnt be null (-it must be set in createNewRoom()
        Room result = manager.getRoomById(roomId);           //??neni to zde ale zavisly na jine metode? to by nemelo byt ne?
        assertEquals(r1, result);                            //Jsou stejné Grave a vysledek co jsem dostal podle id
        assertNotSame(r1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithNull() {
        manager.createNewRoom(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithWrongCapacityZero() {
        r1.setCapacity(0);
        manager.createNewRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithWrongCapacityNegative() {
        r1.setCapacity(-1);
        manager.createNewRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithWrongFloor() {
        r1.setFloor(-1);
        manager.createNewRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithWrongNumberZero() {
        r1.setNumber(0);
        manager.createNewRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithWrongNumberNegative() {
        r1.setNumber(-1);
        manager.createNewRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createRoomWithExitingId() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        r2.setId(roomId);
        manager.createNewRoom(r2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithExitingNumber() {
        manager.createNewRoom(r1);
        int roomNumber = r1.getNumber();
        r2.setNumber(roomNumber);
        manager.createNewRoom(r2); //Throw IllegalArgumentException - Can not be number which exiting in dtb
    }
    
    
    
    
    // Added HW3
    @Test
    public void createNewRoomWithNoteWithLength255() {
        String note = ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                "12345";
        r1.setNote(note);
        manager.createNewRoom(r1);

        Long roomId = r1.getId();
        assertNotNull(roomId);
        Room result = manager.getRoomById(roomId);
        
        assertEquals(r1.getCapacity(), result.getCapacity());
        assertEquals(r1.getFloor(), result.getFloor());
        assertEquals(r1.getNumber(), result.getNumber());
        assertEquals(note, result.getNote());
    }
    
    
    // Added HW3
    @Test(expected = IllegalArgumentException.class)
    public void createNewRoomWithNoteWithLength256() {
        String note = ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                ".................................................." + 
                "123456";
        r1.setNote(note);
        manager.createNewRoom(r1);
    }

    @Test
    public void updateRoomCapacity() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);

        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setCapacity(2);
        manager.updateRoom(r1);
        assertEquals(2, r1.getCapacity());
        assertEquals(1, r1.getFloor());
        assertEquals(1, r1.getNumber());
        assertEquals("Nice room.", r1.getNote());
        assertDeepEqualsRoom(r1, manager.getRoomById(roomId));  //??vyberu z dtb pomoci getRoomById a porovnam ho s room ( upravenym pomoci set) abych zjistit jestli update metoda to upravila i v dtb
            //Check if wasnt modificate other Room by update  
        assertDeepEqualsRoom(r2, manager.getRoomById(r2.getId()));
    }
    
    @Test
    public void updateRoomFloor() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);

        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setFloor(2);
        manager.updateRoom(r1);
        assertEquals(5, r1.getCapacity());
        assertEquals(2, r1.getFloor());
        assertEquals(1, r1.getNumber());
        assertEquals("Nice room.", r1.getNote());
        assertDeepEqualsRoom(r1, manager.getRoomById(roomId));
        assertDeepEqualsRoom(r2, manager.getRoomById(r2.getId()));
    }
        
    @Test
    public void updateRoomNumber() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setNumber(6);
        manager.updateRoom(r1);
        assertEquals(5, r1.getCapacity());
        assertEquals(1, r1.getFloor());
        assertEquals(6, r1.getNumber());
        assertEquals("Nice room.", r1.getNote());
        assertDeepEqualsRoom(r1, manager.getRoomById(roomId));
        assertDeepEqualsRoom(r2, manager.getRoomById(r2.getId()));
    }
    
    @Test
    public void updateRoomNote() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setNote("This is change note.");
        manager.updateRoom(r1);
        assertEquals(5, r1.getCapacity());
        assertEquals(1, r1.getFloor());
        assertEquals(1, r1.getNumber());
        assertEquals("This is change note.", r1.getNote());
        assertDeepEqualsRoom(r1, manager.getRoomById(roomId));
        assertDeepEqualsRoom(r2, manager.getRoomById(r2.getId()));
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithNull() {
        manager.updateRoom(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithNullId() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setId(null);
        manager.updateRoom(r1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongIdNegative() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setId(-1L);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongIdZero() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1 = manager.getRoomById(roomId);
        r1.setId(0L);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithExitingId() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        r2.setId(roomId);
        manager.updateRoom(r2);
    }
    
    
    // Added HW3
    @Test (expected = IllegalArgumentException.class)
    public void updateRoomtWithExitingNumber() {
        Long id1 = r1.getId();
        Long id2 = r2.getId();
        r1 = manager.getRoomById(id1);
        r2 = manager.getRoomById(id2);
        r2.setNumber(r1.getNumber()); // r2: number 1
        manager.updateRoom(r2);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongCapacityZero() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1.setCapacity(0);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongCapacityNegative() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1.setCapacity(-1);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongFloor() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1.setFloor(-1);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongNumberZero() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1.setNumber(0);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithWrongNumberNegative() {
        manager.createNewRoom(r1);
        
        Long roomId = r1.getId();
        assertNotNull(roomId);
        
        r1.setNumber(-1);
        manager.updateRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithExitingNumber() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        int roomNumber = r1.getNumber();
        r2.setNumber(roomNumber);
        manager.updateRoom(r2); //Throw IllegalArgumentException - Can not be number which exiting in dtb
    }    

    @Test
    public void deleteRoomTest() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);

        assertNotNull(manager.getRoomById(r1.getId()));
        assertNotNull(manager.getRoomById(r2.getId()));
        manager.deleteRoom(r1);
        assertNull(manager.getRoomById(r1.getId()));
        assertNotNull(manager.getRoomById(r2.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomWithNull() {
        manager.deleteRoom(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomWithNullId() {
        manager.createNewRoom(r1);
        Long roomId = r1.getId();
        r1 = manager.getRoomById(roomId);
        r1.setId(null);
        manager.deleteRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomWithWrongIdZero() {
        manager.createNewRoom(r1);
        Long roomId = r1.getId();
        r1 = manager.getRoomById(roomId);
        r1.setId(0L);
        manager.deleteRoom(r1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomWithWrongIdNegative() {
        manager.createNewRoom(r1);
        Long roomId = r1.getId();
        r1 = manager.getRoomById(roomId);
        r1.setId(-1L);
        manager.deleteRoom(r1);
    }
    
    @Test
    public void findAllRoomsTest() {
        assertTrue(manager.findAllRooms().isEmpty());

        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        manager.createNewRoom(r3);

        List<Room> result = manager.findAllRooms();
        List<Room> expected = Arrays.asList(r1, r2, r3);
        Collections.sort(result, new RoomComparator());
        Collections.sort(expected, new RoomComparator());
        assertEquals(expected, result);
        assertDeepEqualsCollectionRoom(expected, result);
    }

    @Test
    public void getRoomByIdTest() {
        assertNull(manager.getRoomById(1L));

        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        manager.createNewRoom(r3);

        Long roomId = r2.getId();

        Room result = manager.getRoomById(roomId);

        assertEquals(r2, result);
        assertDeepEqualsRoom(r2, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getRoomByIdWithNullId() {
        manager.createNewRoom(r1);
        
        manager.getRoomById(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getRoomByIdWithWrongIdZero() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        manager.getRoomById(0L);
    }
   
    @Test(expected = IllegalArgumentException.class)
    public void getRoomByIdWithWrongIdNegative() {
        manager.createNewRoom(r1);
        manager.createNewRoom(r2);
        
        manager.getRoomById(-1L);
    }
    static Room newRoom(int capacity, int floor, int number, String note) {
        Room room = new Room();
        room.setCapacity(capacity);
        room.setFloor(floor);
        room.setNumber(number);
        room.setNote(note);
        return room;
    }

    static void assertDeepEqualsRoom(Room expected, Room actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        assertEquals(expected.getFloor(), actual.getFloor());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getNote(), actual.getNote());
    }

    static void assertDeepEqualsCollectionRoom(List<Room> expectedList, List<Room> actualList) {
        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Room expected = expectedList.get(i);
            Room actual = actualList.get(i);
            assertDeepEqualsRoom(expected, actual);
        }
    }
}