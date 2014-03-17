package hotelmanager;

import java.util.Objects;

/**
 * This entity class represents Room. Room has capacity, number of floor, number
 * on floor and some note. One Room could contain zero or more Guest. But
 * maximum acommodate guest must be lower than capacity.
 *
 * @author Petr Domkar & Vojtech Hlavka
 */
public class Room {

    private Long id;
    private int capacity;
    private int floor;
    private int number;
    private String note;

    /**
     * Constructor of entity room with no parameters.
     */
    public Room() {
    }

    public Long getId() {
        return id;
    }

    /**
     * Sets id of this room
     *
     * @param id id of this room which must be a positive integer and diffferent
     * from other IDs
     */
    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets capacity of room
     *
     * @param capacity maximal capacity of room which must be a positive integer
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFloor() {
        return floor;
    }

    /**
     * Sets floor of room
     *
     * @param floor floor of room which must be a non-negative integer
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Sets number (label) of room
     *
     * @param number number of this this which should be different from other
     * numbers of rooms
     */
    public void setNumber(int number) {
        this.number = number;
    }

    public String getNote() {
        return note;
    }

    /**
     * Sets optional note to this room
     *
     * @param note note to this room
     *
     */
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", capacity=" + capacity + ", floor=" + floor + ", number=" + number + "}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Room)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}