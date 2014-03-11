package hotelmanager;

import java.util.List;

/**
 * The service allows to manimulate wiht Rooms
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface RoomManager {

    /**
     * Store Room to DB. Id for new Room is automatically generate and add to
     * object Room
     *
     * @param room room to be created
     * @throws IllegalArgumentException when room is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void createNewRoom(Room room);

    /**
     * Updates information about room in database
     *
     * @param room room to be updated in database
     * @throws IllegalArgumentException when room or id of room is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void updateRoom(Room room);

    /**
     * Deletes room from database
     *
     * @param room room which would be deleted from database
     * @throws IllegalArgumentException when room or id of room is null
     * @throws ServiceFailureException when db operation fails.
     */
    public void deleteRoom(Room room);

    /**
     * Return List of all Rooms in hotel(database)
     *
     * @return list of all rooms.
     * @throws ServiceFailureException when db operation fails.
     */
    public List<Room> findAllRooms();

    /**
     * Return Room by id
     *
     * @param id Primary key of finded room in database
     * @return guest with given id or null if such room was not find
     * @throws IllegalArgumentException when id is null
     */
    public Room getRoomById(Long id);
}