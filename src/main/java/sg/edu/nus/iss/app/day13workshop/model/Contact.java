package sg.edu.nus.iss.app.day13workshop.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

// A class representing a contact in an address book.
public class Contact implements Serializable {
    
    // The serial version UID is used for serialization.
    private static final long serialVersionUID = 1L;

    // Fields:
    // The name of the contact.
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters")
    private String name;

    // The email address of the contact.
    @Email(message = "Invalid Email")
    private String email;

    // The phone number of the contact.
    @Size(min = 7, message = "Phone number must be at least 7 digit.")
    private String phoneNumber;

    // The unique ID of the contact.
    private String id;

    // The date of birth of the contact.
    @Past(message = "Date of birth must not be future")
    @NotNull(message = "Date of Birth must be mandatory")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate dateOfBirth;

    // The age of the contact.
    @NotNull(message = "User's age cannot be null.")
    @Min(value = 10, message = "Must be above 10 years old")
    @Max(value = 100, message = "Must be below 100 years old")
    private int age;

    // Constructors:
    // Creates a new Contact with a randomly generated ID.
    public Contact() {
        // Generate a random ID for the new contact.
        this.id = this.generateId(8);
    }

    /**
     * Creates a new Contact with the specified name, email, phone number, and date of birth.
     * The ID is generated randomly.
     *
     * @param name the name of the contact
     * @param email the email address of the contact
     * @param phoneNumber the phone number of the contact
     * @param dateOfBirth the date of birth of the contact
     */
    public Contact(String name, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.id = this.generateId(8);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Creates a new Contact with the specified ID, name, email, phone number, and date of birth.
     *
     * @param id the ID of the contact
     * @param name the name of the contact
     * @param email the email address of the contact
     * @param phoneNumber the phone number of the contact
     * @param dateOfBirth the date of birth of the contact
     */
    public Contact(String id, String name, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // Methods
    /**
     * Generates a random ID with the specified number of characters.
     *
     * @param numChars the number of characters in the ID
     * @return the generated ID
     */
    private synchronized String generateId(int numChars) {
        // Create a new random number generator
        Random r = new Random();
        // Create a new string builder
        StringBuilder sb = new StringBuilder();
        // Keep generating random hexadecimal strings until the desired number of characters is reached
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        // Return the generated ID
        return sb.toString().substring(0, numChars);
    }

    
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        // Calculate the age of the contact based on the date of birth.
        int calculatedAge = 0;
        if ((dateOfBirth != null)) {
            calculatedAge = Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        this.age = calculatedAge;
        this.dateOfBirth = dateOfBirth;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
