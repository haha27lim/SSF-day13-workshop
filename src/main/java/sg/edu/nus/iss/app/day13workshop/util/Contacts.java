package sg.edu.nus.iss.app.day13workshop.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import sg.edu.nus.iss.app.day13workshop.model.Contact;

// Utility class for managing contact information in a file system.
@Component("contacts")
public class Contacts {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(Contacts.class);
    
    /**
     * This method saves a contact to a file.
     *
     * @param ctc the contact to save
     * @param model the model to add the saved contact to
     * @param appArgs the application arguments
     * @param defaultDataDir the default data directory
     */
    public void saveContact(Contact ctc, Model model, ApplicationArguments appArgs, String defaultDataDir) {
        // Set the data file name to the contact's ID
        String dataFilename = ctc.getId();
        // Initialize a PrintWriter to write the contact's info to a file
        PrintWriter printWriter = null;
        try {
            // Create a FileWriter to write the contact's information to the file
            FileWriter fileWriter = new FileWriter(getDataDir(appArgs, defaultDataDir) + "/" + dataFilename);
            // Initialize the PrintWriter with the FileWriter
            printWriter = new PrintWriter(fileWriter); 
            // Write the contact's name, email, phone number, and date of birth to the file
            printWriter.println(ctc.getName());
            printWriter.println(ctc.getEmail());
            printWriter.println(ctc.getPhoneNumber());
            printWriter.println(ctc.getDateOfBirth().toString());
            // Close the PrintWriter
            printWriter.close();
        } catch (IOException e) {
            // Log any errors that occur
            logger.error(e.getMessage());
        }

        // Add the saved contact to the model
        model.addAttribute("contact", new Contact(ctc.getId(), ctc.getName(),
                ctc.getEmail(), ctc.getPhoneNumber()));
    }

    /**
     * This method gets a contact by ID and adds it to the model.
     *
     * @param model the model to add the retrieved contact to
     * @param contactId the ID of the contact to retrieve
     * @param appArgs the application arguments
     * @param defaultDataDir the default data directory
     */
    public void getContactById(Model model, String contactId, ApplicationArguments appArgs, String defaultDataDir) {
        // Initialize a new Contact object
        Contact ctc = new Contact();
        // Set the formatter for parsing the date of birth from the file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Get the file path for the contact's data file
            Path filePath = new File(getDataDir(appArgs, defaultDataDir) + "/" + contactId).toPath();
            // Set the charset to be used when reading the file
            Charset charset = Charset.forName("UTF-8");
            // Read all the lines from the file into a List of Strings
            List<String> stringList = Files.readAllLines(filePath, charset);
            // Set the contact's ID, name, email, and phone number from the file
            ctc.setId(contactId);
            ctc.setName(stringList.get(0));
            ctc.setEmail(stringList.get(1));
            ctc.setPhoneNumber(stringList.get(2));
            // Parse the date of birth from the file and set it in the contact object.
            LocalDate dob = LocalDate.parse(stringList.get(3), formatter);
            ctc.setDateOfBirth(dob);
        } catch (IOException e) {
            // Log any errors that occur
            logger.error(e.getMessage());
            // Throw a ResponseStatusException with a status code of 404 (Not Found) if the contact info is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Contact info not found");
        }

        // Add the retrieved contact object to the model
        model.addAttribute("contact", ctc);
    }

    /**
     * This method gets the data directory from the application arguments or uses the default data directory.
     *
     * @param appArgs the application arguments
     * @param defaultDataDir the default data directory
     * @return the data directory
     */
    private String getDataDir(ApplicationArguments appArgs, String defaultDataDir) {
        // Initialize the result to the default value
        String dataDirResult = "";
        // Initialize a list to hold the option values
        List<String> optValues = null;
        // Initialize an array to hold the option values
        String[] optValuesArr = null;
        // Get the set of option names from the application arguments
        Set<String> opsNames = appArgs.getOptionNames();
        // Convert the set to an array
        String[] optNamesArr = opsNames.toArray(new String[opsNames.size()]);
        // If there are any option names
        if (optNamesArr.length > 0) {
            // Get the first option value if it exists
            optValues = appArgs.getOptionValues(optNamesArr[0]);
            // Convert the list of option values to an array
            optValuesArr = optValues.toArray(new String[optValues.size()]);
            // Set the result to the first option value
            dataDirResult = optValuesArr[0];
        } else {
            // Use the default data directory if no option values exist
            dataDirResult = defaultDataDir;
        }

        // Return the result
        return dataDirResult;
    }

    // This method gets all contact names in a URI and adds them to the model
    public void getAllContactInURI(Model model, ApplicationArguments appArgs,
            String defaultDataDir) {
        // Get a set of data file names in the specified directory
        Set<String> dataFiles = listFilesUsingJavaIO(getDataDir(appArgs, defaultDataDir));
        // Print the set of data file names
        System.out.println("" + dataFiles);
        // Add the set of data file names to the model as an attribute
        model.addAttribute("contacts", dataFiles.toArray(new String[dataFiles.size()]));
    }

    // This method returns a set of file names in the specified directory
    public Set<String> listFilesUsingJavaIO(String dir) {
        // Return a stream of files in the specified directory, filter out directories, 
        // map the remaining files to their names, and collect the names in a set
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
