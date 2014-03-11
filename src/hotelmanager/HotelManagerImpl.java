package hotelmanager;

import java.util.List;

/**
 * This class implements HotelManager.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class HotelManagerImpl implements HotelManager {

    @Override
    public void accommodateGuestInRoom(Guest guest, Room room) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeGuestFromRoom(Guest guest, Room room) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Guest> getGuestsOfRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Room> findAllFreeRooms() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}