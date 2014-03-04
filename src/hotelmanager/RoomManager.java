package hotelmanager;

import java.util.List;

/**
 * Manager of entity Room
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface RoomManager {

	/**
	 * Creates new Room.
	 * @param room given room
	 */
	public void createNewRoom(Room room);

	/**
	 * Updates information about given room.
	 * @param room given room.
	 */
	public void updateRoom(Room room);

	/**
	 * Deletes given room.
	 * @param room given room
	 */
	public void deleteRoom(Room room);
        
        /**
         * Finds and returns all rooms.
         * @return list of all rooms
         */
	public List<Room> findAllRooms();

	/**
	 * Returns room with given ID
	 * @param id given ID
         * @return room with given ID
	 */
	public Room getRoomById(Long id);
}