package com.e_commerce.made2automade.UserController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.e_commerce.made2automade.Dao.ProductRepository;
import com.e_commerce.made2automade.Dao.UserRepositry;
import com.e_commerce.made2automade.Entities.Product;
import com.e_commerce.made2automade.Entities.User;

import jakarta.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepositry userRepositry;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void commonForAll(Model model, Principal principal) {
        String email_id = principal.getName();
        User user = userRepositry.getUserByEmail(email_id);
        model.addAttribute("user", user);
        model.addAttribute("title", "User-Dashboard");
    }

    // This is add contact handler
    @RequestMapping("/addContact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add-Contact");
        model.addAttribute("contact", new Product());
        return "normal/add_contact";
    }

    // This is save contact handler
    @RequestMapping(value = "/save_contact", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute("contact") Product contact, BindingResult result, Model m,
            @RequestParam("profileimage") MultipartFile file, Principal principal) {
        m.addAttribute("success", false);
        m.addAttribute("error", false);
        if (result.hasErrors()) {
            m.addAttribute("error", true);
            m.addAttribute("contact", contact);
            return "normal/add_contact";
        }
        try {
            String email_id = principal.getName();
            User user = userRepositry.getUserByEmail(email_id);
            if (file.isEmpty()) {
                contact.setImage("contact.png");
            } else {
                File saveFile = new ClassPathResource("static/Img").getFile();
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());
            }
            contact.setUser(user);
            user.getContact().add(contact);
            userRepositry.save(user);
            m.addAttribute("success", true);
            m.addAttribute("contact", new Product());
        } catch (Exception e) {
            m.addAttribute("error", true);
            System.out.println("Exception is--------------------" + e.getMessage());
        }
        return "normal/add_contact";

    }

    // This is show contact handler
    @GetMapping("/showcontact/{page}")
    public String showcontact(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "user-Contact");
        String email = principal.getName();
        User user = userRepositry.getUserByEmail(email);
        PageRequest resultPage = PageRequest.of(page, 2);
        Page<Product> contact = productRepository.getAllContactsByUserId(user.getId(), resultPage);
        model.addAttribute("contacts", contact);
        model.addAttribute("currpage", page);
        model.addAttribute("totlepage", contact.getTotalPages());
        System.out.println(contact);
        return "normal/show_contact";
    }

    // This is show picture handler
    @GetMapping("/showProfile/{page}")
    public String showPicture(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Contact-Profile");
        Optional<Product> contactoptional = productRepository.findById(page);
        Product contact = contactoptional.get();
        String username = principal.getName();
        User user = userRepositry.getUserByEmail(username);
        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("profileData", contact);
        }
        System.out.println(contact);
        return "normal/show_profile";
    }

    // This is for update contact
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("contact") Product updatedContact,
            @RequestParam("profileimage") MultipartFile file, Principal principal, Model model) {
        model.addAttribute("title", "Update-Contact");
        Product contact = productRepository.findById(id).orElse(null);
        if (contact == null ) {
            // Handle case where contact is not found
            return "redirect:/user/show_contact/0";
        }

        // Check if the current user is the owner of the contact
        String username = principal.getName();
        User user = userRepositry.getUserByEmail(username);
        if (user.getId() != contact.getUser().getId()) {
            // Handle case where current user is not the owner of the contact
            return "redirect:/user/show_contact/0";
        }

        try {
            if (!file.isEmpty()) {
                // Save the new image if it's not empty
                File saveFile = new ClassPathResource("static/Img").getFile();
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                updatedContact.setImage(file.getOriginalFilename());
            } else {
                // Keep the existing image if no new image is provided
                updatedContact.setImage(contact.getImage());
            }

            // Update the contact details
            updatedContact.setId(id);
            updatedContact.setUser(user);
            productRepository.save(updatedContact);

            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", true);
            System.out.println("Error updating contact: " + e.getMessage());
        }

        return "normal/update";
    }

    // This is for proccess update form
    @PostMapping("/proccess-update")
    public String submitUpdate(@ModelAttribute("contact") Product contact,
            @RequestParam("cid") Integer cid,
            @RequestParam("profileimage") MultipartFile file, Principal principal, Model model) {
        model.addAttribute("success", false);
        model.addAttribute("error", false);
        try {
            Product oldcontact = productRepository.findById(cid).get();
            if (!file.isEmpty()) {
                // for old image
                File deletefile = new ClassPathResource("static/Img").getFile();
                File tempfile = new File(deletefile, oldcontact.getImage());
                tempfile.delete();
                // for new Image
                File saveFile = new ClassPathResource("static/Img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());

            } else {
                contact.setImage(oldcontact.getImage());
            }
            User user = userRepositry.getUserByEmail(principal.getName());
            contact.setUser(user);
            contact.setId(cid);
            productRepository.save(contact);
            System.out.println(contact);
            model.addAttribute("success", true);
            return "normal/update";
        } catch (Exception e) {
            model.addAttribute("error", true);
            System.out.println("----------------------");
            return "normal/update";
        }

    }

    // This is for delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model, Principal principal) {
        model.addAttribute("title", "Delete-Contact");
        Product contact = productRepository.findById(id).get();
        String username = principal.getName();
        User user = userRepositry.getUserByEmail(username);
        if (user.getId() == contact.getUser().getId()) {
            productRepository.delete(contact);
            System.out.println("delete------------------");
        }
        return "redirect:/user/showcontact/0";
    }

    // This is for show user
    @GetMapping("/show-user")
    public String showUser(Model model, Principal principal) {
        model.addAttribute("title", "User-Details");
        User user = userRepositry.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "normal/show_user";
    }

    // This is for profile picture
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("title", "User-Home");
        return "normal/profile";
    }

    // This is for change password
    @GetMapping("/forget-password")
    public String forgetPassword(Model model, Principal principal) {
        model.addAttribute("title", "Forget-Password");
        return "normal/reset_password";
    }

    // This is for change password
    @PostMapping("/doreset")
    public String doChange(@RequestParam("password") String firstPassword,
            @RequestParam("conformPassword") String conformPassword,
            Model model, Principal principal) {
        model.addAttribute("error", false);
        model.addAttribute("success", false);
        if (!firstPassword.equals(conformPassword)) {
            model.addAttribute("error", true);
            return "normal/reset_password";
        } else {
            User user = userRepositry.getUserByEmail(principal.getName());
            user.setPassword(bCryptPasswordEncoder.encode(firstPassword));
            userRepositry.save(user);
            model.addAttribute("success", true);
            return "normal/reset_password";
        }
    }
}
