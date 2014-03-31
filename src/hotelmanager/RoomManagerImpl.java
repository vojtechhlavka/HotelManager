package hotelmanager;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
//import java.util.logging.SimpleFormatter;

/**
 * This class implements RoomManager.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class RoomManagerImpl implements RoomManager {
    
    public static final Logger logger = Logger.getLogger(RoomManagerImpl.class.getName());
    //private Connection connection;
    private DataSource dataSource;
    
    public RoomManagerImpl(Connection connection) /*throws IOException*/ {
        ; //this.connection = connection;
        
        /*
        //logger.addHandler(new FileHandler("Logging.txt"));
        
        FileHandler fileHandler = new FileHandler("Logging.log", 2000, 5);
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileHandler.setFormatter(formatterTxt);
        logger.addHandler(fileHandler);
        
        //logger.log(Level.SEVERE, "zpráva");
        */
    }
    
    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        ; //connection = dataSource.getConnection();
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    public RoomManagerImpl() /*for now kvuli chybe*/ {

    }
    
    @Override
    public void createNewRoom(Room room) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        
        if(room.getId() != null) {
            throw new IllegalArgumentException("ID of room is already set");
        }
        
        validate(room);
        
        if(getRoomWithGivenNumber(room.getNumber()) != null) {
            throw new IllegalArgumentException("Room with given number already exists");
        }
        
        /*
        if(room.getNote() == null) {
            room.setNote("");
        }
        */
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO ROOM (capacity,floor,number,note) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, room.getCapacity());
            st.setInt(2, room.getFloor());
            st.setInt(3, room.getNumber());
            st.setString(4, room.getNote());
            
            int count = st.executeUpdate();
            //if (count != 1) {
            //    throw new ServiceFailureException("Internal Error: More rows "
            //            + "inserted when trying to insert room " + room);
            //}            
            //ResultSet keyRS = st.getGeneratedKeys();
            //room.setId(getKey(keyRS,room));
            DBUtils.checkUpdatesCount(count, room, true);
            Long id = DBUtils.getId(st.getGeneratedKeys());
            room.setId(id);
            conn.commit();
            
        } catch (SQLException ex) {
            String msg = "Error when inserting room " + room.toString();
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    private Long getKey(ResultSet keyRS, Room room) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert room " + room
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert room " + room
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert room " + room
                    + " - no key found");
        }
    }

    @Override
    public void updateRoom(Room room) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        
        if(room.getId() == null) {
            throw new IllegalArgumentException("ID of room must be set");
        }
        
        if(room.getId() <= 0L) {
            throw new IllegalArgumentException("ID of room must be a positive integer");
        }
        
        validate(room);
        
        Long tmp = getRoomWithGivenNumber(room.getNumber());
        if(tmp != null && tmp != room.getId()) { // exists room with different ID but same number
            // ID cannot be changed
            throw new IllegalArgumentException("Another room with given number "
                    + "(and different ID) already exists");
        }
        
        /*
        if(room.getNote() == null) {
            room.setNote("");
        }
        */
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE room SET capacity=?,floor=?,number=?,note=? " + 
                    "WHERE id = ?");
            st.setInt(1, room.getCapacity());
            st.setInt(2, room.getFloor());
            st.setInt(3, room.getNumber());
            st.setString(4, room.getNote());
            st.setLong(5, room.getId());
            
            /*
            int updatedRows = st.executeUpdate();
            if (updatedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "updated when trying to update room " + room);
            }
            */
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, room, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating room " + room.toString();
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    /**
     * Method which returns ID of room which has given number 
     *    or null when no room with given number exists
     * @param number given number
     * @returns ID of room with given number or null when such room does not exists
    */
    public Long getRoomWithGivenNumber(int number) {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,capacity,floor,number,note FROM room WHERE number = ?");
            st.setInt(1, number);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Room room = restoreRoomFromResultSet(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same number found "
                            + "(source number: " + number + ", found " + room + " and " + restoreRoomFromResultSet(rs));                    
                }            
                
                return room.getId(); // vrátí ID daného pokoje (kontrakt)
            } else {
                return null; // pokoj s danym cislem neexistuje (kontrakt)
            }
            
        } catch (SQLException ex) {
            String msg = "Error when retrieving room with number " + number;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            //-----------------------DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void deleteRoom(Room room) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        
        if(room.getId() == null) {
            throw new IllegalArgumentException("ID of room is null");
        }
        
        if(room.getId() <= 0) {
            throw new IllegalArgumentException("ID of room must be a positive integer");
        }
        
        // overovani zda dany pokoj vubec existuje???
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM room WHERE id = ?");
            st.setLong(1, room.getId());
            
            /*
            int deletedRows = st.executeUpdate();
            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "deleted when trying to delete room " + room);
            }
            */
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, room, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting room " + room.toString();
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Room> findAllRooms() {
        // throw new UnsupportedOperationException("Not supported yet.");
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,capacity,floor,number,note FROM room");
            ResultSet rs = st.executeQuery();
            
            List<Room> result = new ArrayList<Room>();
            while (rs.next()) {
                result.add(restoreRoomFromResultSet(rs));
            }
            return result;
            
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all room", ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    private Room restoreRoomFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setCapacity(rs.getInt("capacity"));
        room.setFloor(rs.getInt("floor"));
        room.setNumber(rs.getInt("number"));
        room.setNote(rs.getString("note"));
        return room;
    }

    @Override
    public Room getRoomById(Long id) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        if(id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        
        if(id <= 0L) {
            throw new IllegalArgumentException("ID must be a positive integer");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,capacity,floor,number,note FROM room WHERE id = ?");
            st.setLong(1, id);
            
            /*
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Room room = restoreRoomFromResultSet(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + room + " and " + restoreRoomFromResultSet(rs));                    
                }            
                
                return room;
            } else {
                return null; // testy vyzaduji "null"
            }
            */
            return executeQueryForSingleRoom(st);
            
        } catch (SQLException ex) {
            String msg = "Error when getting room with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    
    static Room executeQueryForSingleRoom(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Room result = rowToRoom(rs);                
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more rooms with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    static List<Room> executeQueryForMultipleRooms(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Room> result = new ArrayList<Room>();
        while (rs.next()) {
            result.add(rowToRoom(rs));
        }
        return result;
    }
    
    private static Room rowToRoom(ResultSet rs) throws SQLException {
        Room result = new Room();
        result.setId(rs.getLong("id"));
        result.setCapacity(rs.getInt("capacity"));
        result.setFloor(rs.getInt("floor"));
        result.setNumber(rs.getInt("number"));
        result.setNote(rs.getString("note"));
        return result;
    }
    
    private static void validate(Room room) throws IllegalArgumentException {
        
        if(room.getNote().length() > 255) {
            throw new IllegalArgumentException("The note is too long");
        }
        
        if(room.getFloor() < 0) {
            throw new IllegalArgumentException("Floor of room must be a non-negative number");
        }
        
        if(room.getNumber() <= 0) {
            throw new IllegalArgumentException("Number of room must be a positive number");
        }
        
        if(room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity of room must be a positive number");
        }
    }

}