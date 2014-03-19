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
//import java.util.logging.SimpleFormatter;

/**
 * This class implements RoomManager.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class RoomManagerImpl implements RoomManager {
    
    public static final Logger logger = Logger.getLogger(RoomManagerImpl.class.getName());
    private Connection connection;
    
    public RoomManagerImpl(Connection connection) /*throws IOException*/ {
        this.connection = connection;
        
        /*
        //logger.addHandler(new FileHandler("Logging.txt"));
        
        FileHandler fileHandler = new FileHandler("Logging.log", 2000, 5);
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileHandler.setFormatter(formatterTxt);
        logger.addHandler(fileHandler);
        
        //logger.log(Level.SEVERE, "zpráva");
        */
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
        
        if(room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity of room must be a positive number");
        }
        
        if(room.getFloor() < 0) {
            throw new IllegalArgumentException("Floor of room must be a non-negative number");
        }
        
        if(room.getNumber() <= 0) {
            throw new IllegalArgumentException("Number of room must be a positive number");
        }
        
        if(getRoomWithGivenNumber(room.getNumber()) != null) {
            throw new IllegalArgumentException("Room with given number already exists");
        }
        
        /*
        if(room.getNote() == null) {
            room.setNote("");
        }
        */
        
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "INSERT INTO ROOM (capacity,floor,number,note) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, room.getCapacity());
            st.setInt(2, room.getFloor());
            st.setInt(3, room.getNumber());
            st.setString(4, room.getNote());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "inserted when trying to insert room " + room);
            }            
            
            ResultSet keyRS = st.getGeneratedKeys();
            room.setId(getKey(keyRS,room));
            
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting room " + room, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
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
        
        if(room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity of room must be a positive number");
        }
        
        if(room.getFloor() < 0) {
            throw new IllegalArgumentException("Floor of room must be a non-negative number");
        }
        
        if(room.getNumber() <= 0) {
            throw new IllegalArgumentException("Number of room must be a positive number");
        }
        
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
        
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "UPDATE room SET capacity=?,floor=?,number=?,note=? " + 
                    "WHERE id = ?");
            st.setInt(1, room.getCapacity());
            st.setInt(2, room.getFloor());
            st.setInt(3, room.getNumber());
            st.setString(4, room.getNote());
            st.setLong(5, room.getId());
            
            int updatedRows = st.executeUpdate();
            if (updatedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "updated when trying to update room " + room);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when updating room " + room, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Method which returns ID of room which has given number 
     *    or null when no room with given number exists
     * @param number given number
     * @returns ID of room with given number or null when such room does not exists
    */
    public Long getRoomWithGivenNumber(int number) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
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
            throw new ServiceFailureException(
                    "Error when retrieving room with number " + number, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
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
        
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "DELETE FROM room WHERE id = ?");
            st.setLong(1, room.getId());
            
            int deletedRows = st.executeUpdate();
            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "deleted when trying to delete room " + room);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when deleting room " + room, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public List<Room> findAllRooms() {
        // throw new UnsupportedOperationException("Not supported yet.");
        
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
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
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
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
        
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "SELECT id,capacity,floor,number,note FROM room WHERE id = ?");
            st.setLong(1, id);
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
            
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving room with id " + id, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}