package sg.edu.nus.iss.app.day13workshop;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static sg.edu.nus.iss.app.day13workshop.util.IOUtil.*;

@SpringBootApplication
public class Day13workshopApplication {

	// Logger instance for this class
	private static final Logger logger = LoggerFactory.getLogger(Day13workshopApplication.class);

	public static void main(String[] args) {
		// Create a new SpringApplication instance using the current class as a source
		SpringApplication app = new SpringApplication(Day13workshopApplication.class);
		// Create a new DefaultApplicationArguments instance using the provided args
		DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
		// Get the option values for the "dataDir" option
		List<String> opsVal = appArgs.getOptionValues("dataDir");
		System.out.println(opsVal);
		// If the option values list is not empty
		if (opsVal != null) {
			// Log the first option value
			logger.info("" + (String) opsVal.get(0));
			// Create a directory with the first option value as its name
			createDir((String) opsVal.get(0));
		// If the option values list is empty	
		} else {
			// Log a warning message
			logger.warn("No data directory was provided");
			// Exit the application with a status code of 1
			System.exit(1);
		}

		// Run the SpringApplication instance
		app.run(args);
	}

}

