package hotelmanager;

import java.util.Comparator;

/**
 *
 * @author Petr
 */
public class GuestComparator implements Comparator<Guest> {
    @Override
    public int compare(Guest o1, Guest o2) {
        return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
    }
}