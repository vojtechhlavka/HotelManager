package hotelmanager;

/**
 * Entity of room
 * @author Petr Domkar & Vojtech Hlavka
 */
public class Room {

	private Long id;
	private int number;
	private int capacity;
	private int floor;
	private String note;
        
        /**
         * Constructor of entity room with no parameters.
         */
        public Room()
        {
        }
        
        /**
         * Constructor of entity room with 5 parameters.
         * @param id ID of room
         * @param number number of room
         * @param capacity capacity of room
         * @param floor floor of room
         * @param note note about room
         */
        /*
        public Room(int number, int capacity, int floor, String note)
        {
            this.number = number;
            this.capacity = capacity;
            this.floor = floor;
            this.note = note;
        }
        */
}