package hotelmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        
        /* //proc nejde?
        if( getGuestById(guest.getId()) != null ) {
            throw new IllegalArgumentException("This guest was created already");
        }
        */
        
        
        try(PreparedStatement st = conn.prepareStatement(
                "INSERT INTO guest (name, surname, identityCardNumber, gender) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            
            st.setString(1, guest.getName());
            st.setString(2, guest.getSurname());
            st.setString(3, guest.getIdentityCardNumber());
            st.setString(4, guest.getGender().toString());
            int addedRows = st.executeUpdate();
            
            if(addedRows != 1) { //pridan jeden radek
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert guest " + guest);
            }
            
          //  ResultSet rs = st.getGeneratedKeys(); // predelat s osetrenim
          //  guest.setId(rs.getLong(1));
            
        } catch(SQLException ex) {
            throw new ServiceFailureException("Error when inserting guest " + guest, ex);
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteGuest(Guest guest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Guest> findAllGuests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Guest getGuestById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}