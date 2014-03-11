package hotelmanager;

import java.util.Comparator;

/**
 * Comparator to Guest compare by Id
 * 
 * @author Petr
 */
public class GuestComparator implements Comparator<Guest> {
    @Override
    public int compare(Guest o1, Guest o2) {
        return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
    }
}