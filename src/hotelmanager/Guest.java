package hotelmanager;

/**
 * Entity of Guest
 * @author Petr Domkar & Vojtech Hlavka
 */
public class Guest {

	private Long id;
	private String name;
	private String surname;
	private String identityCardNumber;
	private Gender gender;
        
        /**
         * Constructor of entity Guest with no parameters.
         */
        public Guest()
        {
        }
        
        /**
         * Constructor of entity Guest with 5 parameters.
         * @param name name of guest
         * @param surname surname of guest
         * @param identityCardNumber number of identity cartd of guest
         * @param gender gender of guest
         */
        public Guest(String name, String surname, String identityCardNumber, Gender gender)
        {
            this.name = name;
            this.surname = surname;
            this.identityCardNumber = identityCardNumber;
            this.gender = gender;
        }
}