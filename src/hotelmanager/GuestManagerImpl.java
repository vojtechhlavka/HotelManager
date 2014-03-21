package hotelmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements GuestManager.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class GuestManagerImpl implements GuestManager {
    public static final Logger logger = Logger.getLogger(GuestManagerImpl.class.getName());
    private Connection conn;
    
    public GuestManagerImpl(Connection conn) {
        this.conn = conn;
    } 
    
    public GuestManagerImpl() { // jen pro ted
    } 
    
    
    @Override
    public void createNewGuest(Guest guest) {
        if(guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }
        
        if(guest.getId() != null) {
            throw new IllegalArgumentException("Id of guest is already is set");
        }
        
        if(guest.getName() == null) {
            throw new IllegalArgumentException("Name of guest is null");
        }
        
        if(guest.getSurname() == null) {
            throw new IllegalArgumentException("Surname of guest is null");
        }
        
        if(guest.getIdentityCardNumber() == null) {
            throw new IllegalArgumentException("IdentityCardNumber of guest is null");
        }
        
        if(guest.getGender() == null) {
            throw new IllegalArgumentException("Gender of guest is null");
        }
        
        if(guest.getName().replaceAll("\\s", "").length() == 0) {
            throw new IllegalArgumentException("Name of guest has zero length");
        }
        
        
        
        if(guest.getSurname().replaceAll("\\s", "").length() == 0) {
            throw new IllegalArgumentException("Surname of guest has zero length");
        }
        
        if(guest.getIdentityCardNumber().replaceAll("\\s", "").length() < 9) {
            throw new IllegalArgumentException("IdentityCardNumber of guest has shorter length than 9");
        }
        
        if(guest.getName().replaceAll("\\s", "").length() > 50) {
            throw new IllegalArgumentException("Name of guest has longer length than 50");
        }
        
        if(guest.getSurname().replaceAll("\\s", "").length() > 50) {
            throw new IllegalArgumentException("Surname of guest has longer length than 50");
        }
        
        if(guest.getIdentityCardNumber().replaceAll("\\s", "").length() > 9) {
            throw new IllegalArgumentException("IdentityCardNumber of guest has longer length than 9");
        }
        
        Pattern p = Pattern.compile("[a-zA-Z[ěščřžýáíéůú]*]+");
        Matcher m = p.matcher(guest.getName());
        if( !m.find() ) {
            throw new IllegalArgumentException("Name contains wrong character");
        }
        m = p.matcher(guest.getSurname());
        if( !m.find() ) {
            throw new IllegalArgumentException("Surname contains wrong character");
        }
        
        p = Pattern.compile("[0-9]{9}");
        m = p.matcher(guest.getIdentityCardNumber());
        if( !m.find() ) {
            throw new IllegalArgumentException("IdentitycardNumber must contains only character [0-9]");
        }
        if(guest.getIdentityCardNumber().equals("000000000")) {
            throw new IllegalArgumentException("IdentitycardNumber can not be 000000000");
        }
        
        if( Arrays.asList(Gender.values()).contains(guest.getGender()) == false ) {
            throw new IllegalArgumentException("Gender must be MALE or FEMALE");
        }
                
        
        try(PreparedStatement st = conn.prepareStatement(
                "INSERT INTO guest (name, surname, identityCardNumber, gender) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            
            st.setString(1, guest.getName());
            st.setString(2, guest.getSurname());
            st.setString(3, guest.getIdentityCardNumber());
            st.setString(4, guest.getGender().name());
            int addedRows = st.executeUpdate();
            
            if(addedRows != 1) { //pridan jeden radek
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert guest " + guest);
            }
            
            ResultSet rs = st.getGeneratedKeys();
            guest.setId(getKey(rs, guest));
            
        } catch(SQLException ex) {
            throw new ServiceFailureException("Error when inserting guest " + guest, ex);
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        if(guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }
        
        if(guest.getId() == null) {
            throw new IllegalArgumentException("Guest id is null");
        }
                
        if(guest.getId() <= 0) {
            throw new IllegalArgumentException("Guest id must be positive");
        }
        
        if(guest.getName() == null) {
            throw new IllegalArgumentException("Name of guest is null");
        }
        
        if(guest.getSurname() == null) {
            throw new IllegalArgumentException("Surname of guest is null");
        }
        
        if(guest.getIdentityCardNumber() == null) {
            throw new IllegalArgumentException("IdentityCardNumber of guest is null");
        }
        
        if(guest.getGender() == null) {
            throw new IllegalArgumentException("Gender of guest is null");
        }
        
        if(guest.getName().replaceAll("\\s", "").length() == 0) {
            throw new IllegalArgumentException("Name of guest has zero length");
        }
        
        if(guest.getSurname().replaceAll("\\s", "").length() == 0) {
            throw new IllegalArgumentException("Surname of guest has zero length");
        }
        
        if(guest.getIdentityCardNumber().replaceAll("\\s", "").length() < 9) {
            throw new IllegalArgumentException("IdentityCardNumber of guest has shorter length than 9");
        }
        
        if(guest.getName().replaceAll("\\s", "").length() > 50) {
            throw new IllegalArgumentException("Name of guest has longer length than 50");
        }
        
        if(guest.getSurname().replaceAll("\\s", "").length() > 50) {
            throw new IllegalArgumentException("Surname of guest has longer length than 50");
        }
        
        if(guest.getIdentityCardNumber().replaceAll("\\s", "").length() > 9) {
            throw new IllegalArgumentException("IdentityCardNumber of guest has longer length than 9");
        }
        
        Pattern p = Pattern.compile("[a-zA-Z[ěščřžýáíéůú]*]+");
        Matcher m = p.matcher(guest.getName());
        if( !m.find() ) {
            throw new IllegalArgumentException("Name contains wrong character");
        }
        m = p.matcher(guest.getSurname());
        if( !m.find() ) {
            throw new IllegalArgumentException("Surname contains wrong character");
        }
        
        p = Pattern.compile("[0-9]{9}");
        m = p.matcher(guest.getIdentityCardNumber());
        if( !m.find() ) {
            throw new IllegalArgumentException("IdentitycardNumber must contains only character [0-9]");
        }
        if(guest.getIdentityCardNumber().equals("000000000")) {
            throw new IllegalArgumentException("IdentitycardNumber can not be 000000000");
        }
        
        if( Arrays.asList(Gender.values()).contains(guest.getGender()) == false ) {
            throw new IllegalArgumentException("Gender must be MALE or FEMALE");
        }
        
        
        
        Long tmp = getGuestWithGivenIdentityCard(guest.getIdentityCardNumber());
        if(tmp != null && tmp != guest.getId()) { // exists room with different ID but same number
            // ID cannot be changed
            throw new IllegalArgumentException("Another guest with given identityCardNumber "
                    + "(and different ID) already exists");
        }
        
        try(PreparedStatement st = conn.prepareStatement(
                "UPDATE guest SET name=?, surname=?, identityCardNumber=?, gender=? WHERE id=?")) {
            st.setString(1, guest.getName());
            st.setString(2, guest.getSurname());
            st.setString(3, guest.getIdentityCardNumber());
            st.setString(4, guest.getGender().name());
            st.setLong(5, guest.getId());
            int updatedGuest = st.executeUpdate();
            if(updatedGuest != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "updated when trying to update guest " + guest);
            }
        } catch(SQLException ex) {
            throw new ServiceFailureException("Errow when updating guest" + guest, ex);
        }
        
    }
    
    public Long getGuestWithGivenIdentityCard(String number) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT id,name, surname, identityCardNumber, gender FROM guest WHERE identityCardNumber = ?");
            st.setString(1, number);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Guest guest = newGuestFromResult(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same identityCardNumber found "
                            + "(source number: " + number + ", found " + guest + " and " + newGuestFromResult(rs));                    
                }            
                
                return guest.getId(); // vrátí ID daného pokoje (kontrakt)
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
    public void deleteGuest(Guest guest) {
        if(guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }
        if(guest.getId() == null) {
            throw new IllegalArgumentException("Id of guest is null");
        }
        if(guest.getId() <= 0) {
            throw new IllegalArgumentException("Id of guest must be positive");
        }
        
        try {
            getGuestById(guest.getId());
        } catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException("Guest does not exist");
        }
        
        try(PreparedStatement st = conn.prepareStatement(
                "DELETE FROM guest WHERE id = ?")) {
            st.setLong(1, guest.getId());
            int delteRow = st.executeUpdate();
            
            if(delteRow != 1 ){
                throw new ServiceFailureException("Internal Error: More rows "
                        + "deleted when trying to delete guest " + guest);
            }
        } catch(SQLException ex) {
            throw new ServiceFailureException("Errow when deleting guest" + guest, ex);
        }
    }

    @Override
    public List<Guest> findAllGuests() {
        try(PreparedStatement st = conn.prepareStatement(
                "SELECT id, name, surname, identityCardNumber, gender FROM guest")) {
            ResultSet rs = st.executeQuery();
            
            List<Guest> guests = new ArrayList<Guest>();
            while(rs.next()) {
                guests.add(newGuestFromResult(rs));
            }
            
            return guests;
        } catch(SQLException ex) {
            throw new ServiceFailureException("Error when select all guests", ex);
        }
    }

    @Override
    public Guest getGuestById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id is null");
        }
        
        if(id <=0L) {
            throw new IllegalArgumentException("Id must be positive");
        }
        
        try(PreparedStatement st = conn.prepareStatement(
                "SELECT id, name, surname, identityCardNumber, gender FROM guest WHERE id = ?")) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
                Guest guest = newGuestFromResult(rs);
                
                if(rs.next()) {
                    throw new IllegalArgumentException("Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + guest + " and " + newGuestFromResult(rs));     
                }
                
                return guest;                
            } else {
                throw new IllegalArgumentException("For id " + id + "wan not found guest");
            }
            
        } catch(SQLException ex) {
            throw new ServiceFailureException("Error when finding guest with id: " + id, ex);
        }
    }

    private Guest newGuestFromResult(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setId(rs.getLong(1));
        guest.setName(rs.getString(2));
        guest.setSurname(rs.getString(3));
        guest.setIdentityCardNumber(rs.getString(4));
        Gender gender = null;
        guest.setGender(Gender.valueOf(rs.getString(5)));

   
        return guest;
    }

    private Long getKey(ResultSet rs, Guest guest) throws SQLException, ServiceFailureException {
        if(rs.next()) {
            if(rs.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + guest
                        + " - wrong key fields count: " + rs.getMetaData().getColumnCount());
            }
            Long result = rs.getLong(1);
            if(rs.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + guest
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert grave " + guest
                    + " - no key found");
        }
    }
}