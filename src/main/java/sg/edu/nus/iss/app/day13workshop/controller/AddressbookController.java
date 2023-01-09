package sg.edu.nus.iss.app.day13workshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

// Import the Contacts and Contact classes from the model package
import sg.edu.nus.iss.app.day13workshop.model.Contact;
import sg.edu.nus.iss.app.day13workshop.util.Contacts;

// Mark this class as a Spring MVC controller and map it to the '/addressbook' URI
@Controller
@RequestMapping(path = "/addressbook")
public class AddressbookController {

    // Autowire an instance of Contacts class
    @Autowired
    Contacts ctcz;

    // Autowire an instance of ApplicationArguments class
    @Autowired
    ApplicationArguments appArgs;

    // Inject the value of the 'test.data.dir' property into this field
    @Value("${test.data.dir}")
    private String dataDir;

    // Map GET requests to the '/addressbook' URI to this method
    @GetMapping
    public String showAddressbookForm(Model model) {
        // Add an empty Contact object to the model
        model.addAttribute("contact", new Contact());
        // Return the 'addressbook' view
        return "addressbook";
    }

    // Map POST requests to the '/addressbook' URI to this method
    @PostMapping
    public String saveContact(@Valid Contact contact, BindingResult binding, Model model) {
        // If there are errors in the contact object, return the 'addressbook' view
        if (binding.hasErrors()) {
            return "addressbook";
        }

        // Save the contact and return the 'showContact' view
        ctcz.saveContact(contact, model, appArgs, dataDir);
        return "showContact";
    }

    // Map GET requests to the '/addressbook/{contactId}' URI to this method
    @GetMapping("{contactId}")
    public String getContactById(Model model, @PathVariable String contactId) {
        // Get the contact by id and return the 'showContact' view
        ctcz.getContactById(model, contactId, appArgs, dataDir);
        return "showContact";
    }

    // Map GET requests to the '/addressbook/list' URI to this method
    @GetMapping(path = "/list")
    public String getAllContacts(Model model) {
        // Get all contacts and return the 'contacts' view
        ctcz.getAllContactInURI(model, appArgs, dataDir);
        return "contacts";
    }
}
