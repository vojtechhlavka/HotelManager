package hotelmanager;

import java.util.List;

/**
 * Manager of entity Guest
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface GuestManager {

	/**
	 * Creates new guest.
	 * @param guest given guest
	 */
	public void createNewGuest(Guest guest);

	/**
	 * Updates information about given guest.
	 * @param guest given guest
	 */
	public void updateGuest(Guest guest);

	/**
	 * Deletes given guest.
	 * @param guest given guest
	 */
	public void deleteGuest(Guest guest);
        
        /**
         * Finds and returns all guests.
         * @return list of all guests.
         */
	public List<Guest> findAllGuests();

	/**
	 * Returns guets with given ID
	 * @param id given ID
         * @return guest with given ID
	 */
	public Guest getGuestById(Long id);
}