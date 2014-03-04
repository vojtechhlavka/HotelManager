package hotelmanager;

import java.util.List;

/**
 * Main manager of hotel
 * @author Petr Domkar & Vojtech Hlavka
 */
public interface HotelManager {

	/**
	 * Accommodates given guest into given room.
	 * @param guest given guest
	 * @param room give room
	 */
	public void accommodateGuestInRoom(Guest guest, Room room);

	/**
	 * Removes given guest from given room.
	 * @param guest given guest
	 * @param room given room
	 */
	public void removeGuestFromRoom(Guest guest, Room room);

	/**
	 * Shows number of room, floor, capacity and guests accomodated in given room.
	 * @param room given room
	 */
	public void showInformationAboutRoom(Room room);
        
        /**
         * Finds and returns all free rooms in hotel.
         * @return list of free rooms
         */
	public List<Room> findAllFreeRooms();

}