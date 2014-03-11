package hotelmanager;

import java.util.List;

/**
 * This service allows to manipulate with Guests.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface GuestManager {

    /**
     * Store Guest to DB. Id for new Guest is automatically generate and add to
     * object Guest
     *
     * @param guest guest to be created
     * @throws IllegalArgumentException when guest is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void createNewGuest(Guest guest);

    /**
     * Updates information about guest in database
     *
     * @param guest guest to be updated in database
     * @throws IllegalArgumentException when guest or id of guest is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void updateGuest(Guest guest);

    /**
     * Deletes guest from database
     *
     * @param guest guest which would be deleted from database
     * @throws IllegalArgumentException when guest or id of guest is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void deleteGuest(Guest guest);

    /**
     * Return List of all Guests in hotel(database)
     *
     * @return list of all guests.
     * @throws ServiceFailureException when db operation fails.
     */
    public List<Guest> findAllGuests();

    /**
     * Returns guest by id
     *
     * @param id primary key of finded guest
     *
     * @return guest with given id or null if such guest was not find
     * @throws IllegalArgumentException when id is null
     */
    public Guest getGuestById(Long id);
}