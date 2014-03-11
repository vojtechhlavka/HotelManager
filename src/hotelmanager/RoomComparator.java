package hotelmanager;

import java.util.Comparator;

/**
 * Comparator to Room compare by Id
 *
 * @author Petr
 */
public class RoomComparator implements Comparator<Room> {

    @Override
    public int compare(Room o1, Room o2) {
        return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
    }
}
