package hotelmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * This class implements HotelManager.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class HotelManagerImpl implements HotelManager {

    public static final Logger logger = Logger.getLogger(RoomManagerImpl.class.getName());
   
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }    

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
        
    @Override
    public void accommodateGuestInRoom(Guest guest, Room room) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        if(guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }        
        if(guest.getId() == null) {
            throw new IllegalArgumentException("ID of guest is null");
        }        
        if(guest.getId() <= 0L) {
            throw new IllegalArgumentException("ID of guest must be a positive integer");
        }
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }        
        if(room.getId() == null) {
            throw new IllegalArgumentException("ID of room is null");
        }
        if(room.getId() <= 0L) {
            throw new IllegalArgumentException("ID of room must be a positive integer");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            //##//conn = connection; // added
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            checkIfRoomHasSpace(conn, room);
            
            st = conn.prepareStatement(
                    "UPDATE Guest SET roomId = ? WHERE id = ? AND roomId IS NULL"); // předělat!!!
            st.setLong(1, room.getId());
            st.setLong(2, guest.getId());
            int count = st.executeUpdate();
            if(count == 0) {
            //if(count != 1) {
                throw new IllegalArgumentException("Guest " + guest + 
                        " not found or it is already placed in some room");
            }
            DBUtils.checkUpdatesCount(count, guest, false);            
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when putting guest into room";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
            /*
            // added:
            if(st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
        }
    }
    
    private static void checkIfRoomHasSpace(Connection conn, Room room) throws IllegalArgumentException, SQLException {
        PreparedStatement st = null;
        try {
            /*
            st = conn.prepareStatement(
                    "SELECT capacity, COUNT(Body.id) as bodiesCount " +
                    "FROM Grave LEFT JOIN Body ON Grave.id = Body.graveId " +
                    "WHERE Grave.id = ? " +
                    "GROUP BY Grave.id, capacity");
            */
            st = conn.prepareStatement( /* predelat !!! */
                    "SELECT capacity, COUNT(Guest.id) as guestsCount " +
                    "FROM Room LEFT JOIN Guest ON Room.id = Guest.roomId " +
                    "WHERE Room.id = ? " +
                    "GROUP BY Room.id, capacity");
            st.setLong(1, room.getId());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getInt("capacity") <= rs.getInt("guestsCount")) {
                    throw new IllegalArgumentException("Room " + room + " is already full");
                }
            } else {
                throw new IllegalArgumentException("Room " + room + " does not exist in the database");
            }
        } finally {
            DBUtils.closeQuietly(null, st); // chyba ???
            // pridano:
            /*
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
            // Nee!! st jeste bude potřeba v materske metode
        }
    }

    @Override
    public void removeGuestFromRoom(Guest guest, Room room) {
        checkDataSource();
        
        if(!getGuestsOfRoom(room).contains(guest)) {
            throw new IllegalArgumentException("In this room is not this guest");
        } 
        
        if(!getGuestsOfRoom(room).contains(guest)) {
            throw new IllegalArgumentException("In this room is not this guest");
        } 
        
        validateGuest(guest);
        validateRoom(room);
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                "UPDATE GUEST SET ROOMID=NULL WHERE ID=? AND ROOMID=?");
            st.setLong(1, guest.getId());
            st.setLong(2, room.getId());
            
            int updatedRows = st.executeUpdate();
            if(updatedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "updated when trying to remove guest " + guest + " from room " + room);
            }
            conn.commit();
        } catch(SQLException ex) {
            String msg = "Error when removing guest from room";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Guest> getGuestsOfRoom(Room room) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        //////checkDataSource();        
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }        
        if(room.getId() == null) {
            throw new IllegalArgumentException("ID of room is null");
        }
        if(room.getId() <= 0L) {
            throw new IllegalArgumentException("ID of room must be a positive integer");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            /*
            st = conn.prepareStatement(
                    "SELECT Body.id, name, born, died, vampire " +
                    "FROM Body JOIN Grave ON Grave.id = Body.graveId " +
                    "WHERE Grave.id = ?");
            */
            
            st = conn.prepareStatement( // predelat !!! //
                    "SELECT Guest.id, name, surname, identityCardNumber, gender " +
                    "FROM Guest JOIN Room ON Room.id = Guest.roomId " +     // zde byla chyba: Guest.roomId
                    "WHERE Room.id = ?");
            st.setLong(1, room.getId());
            return GuestManagerImpl.getListOfGuestFromSt(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find bodies in room " + room + " && " + ex;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st); // chyba ???
            /*
            // added:
            if(st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            */
        }
    }


    @Override
    public List<Room> findAllFreeRooms() {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT ROOM.ID, CAPACITY, FLOOR, NUMBER, NOTE FROM ROOM"
                    + "LEFT JOIN GUEST ON ROOM.ID = GUEST.ROOMID"
                    + "GROUP BY ROOM.ID, CAPACITY, FLOOR, NUMBER, NOTE"
                    + "HAVING COUNT(GUEST.ID) < CAPACITY"); 
            
            return RoomManagerImpl.executeQueryForMultipleRooms(st);  
        } catch (SQLException ex) {
            String msg = "Error when trying to find free rooms";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    
    private void validateRoom(Room room) throws IllegalArgumentException {
        if(room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        if(room.getId() <=0L) {
            throw new IllegalArgumentException("Id of room must be positive");
        }
        if(room.getId() == null) {
            throw new IllegalArgumentException("Id of room is null");
        }
    }

    private void validateGuest(Guest guest) throws IllegalArgumentException {
        if(guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }        
        if(guest.getId() == null) {
            throw new IllegalArgumentException("Id of guest is null");
        }
        if(guest.getId() <=0L) {
            throw new IllegalArgumentException("Id of guest must be positive");
        }
    }
}