package hotelmanager;

import java.util.Objects;

/**
 * This entity class represents Guest. Guest has name, surname,
 * identityCardNumber. Guest could be accommodate in Room
 *
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
    public Guest() {
    }

    public Long getId() {
        return id;
    }

    /**
     * Sets ID of this guest
     *
     * @param id id of this Guest which must be a positive integer and different
     * from other IDs
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets name of guest
     *
     * @param name name of guest which must be nonempty string consisting of
     * only letters of alphabet
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    /**
     * Sets surname of guest
     *
     * @param surname surname of guest which must be nonempty string consisting
     * of only letters of alphabet
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    /**
     * Sets identity card number of guest
     *
     * @param identityCardNumber number of guest's identity card which must be
     * string consisting of just 9 digits
     */
    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public Gender getGender() {
        return gender;
    }

    /**
     * Sets gender of guest
     *
     * @param gender gender of guest which must be from enum {MALE,FEMALE}
     *
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Guest{" + "id=" + id + ", name=" + name + ", surname=" + surname + ", identityCardNumber=" + identityCardNumber + ", gender=" + gender + "}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Guest other = (Guest) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}