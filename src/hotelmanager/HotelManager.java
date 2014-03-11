package hotelmanager;

import java.util.List;

/**
 * This service allows manimulate with associations between Room and Guest
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface HotelManager {

    /**
     * Accommodates guest into given room. When accommodate count of guests in
     * this room is equals capacity, the guest will not be accommodate
     *
     * @param guest guest to be accommodate
     * @param room room where the guest will be acommodate
     * @throws IllegalArgumentException when guest or room is null or when guest
     * is already accommodate in some room, when room is already full or when
     * guest or room have null id or do not exist in database
     * @throws ServiceFailureException when db operation fails.
     */
    public void accommodateGuestInRoom(Guest guest, Room room);

    /**
     * Removes guest from given room.
     *
     * @param guest guest to be remove from room
     * @param room room for removing given guest
     * @throws IllegalArgumentException when guest or room is null or when guest
     * or room have null id or do not exist in database
     * @throws ServiceFailureException when db operation fails.
     */
    public void removeGuestFromRoom(Guest guest, Room room);

    /**
     * Return List of all guests from given room
     *
     * @param room room where we find guests
     * @return List with Guests from giver room
     * @throws IllegalArgumentException when room is null or when room have null
     * id or do not exist in database
     * @throws ServiceFailureException when db operation fails.
     */
    public List<Guest> getGuestsOfRoom(Room room);

    /**
     * Return List of all all free rooms in hotel.
     *
     * @return List of free rooms
     * @throws ServiceFailureException when db operation fails.
     */
    public List<Room> findAllFreeRooms();
}