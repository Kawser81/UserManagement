package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.model.UserRequestDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WebController {

    private final UserService userService;

//    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    // Home page with suggestions and search
    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String searchId, @RequestParam(required = false) String error) {
        List<User> users = userService.getAllUsers();  // Fetch here
        model.addAttribute("users", users);            // Add to Model
        model.addAttribute("searchId", searchId != null ? searchId : "");
        model.addAttribute("error", error);
        return "index";
    }

    // Handle search: If valid ID, redirect to detail; else, back to home with error
    @GetMapping("/search")
    public String searchUser(@RequestParam Long id, Model model) {
        try {
            User user = userService.getUserById(id);  // Throws exception if not found
            return "redirect:/users/" + id;  // Success: Go to detail page
        } catch (RuntimeException e) {
            return "redirect:/?searchId=" + id + "&error=User not found with ID: " + id;
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList";  // Your existing list template
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "userDetail";  // Your existing detail template
    }

    // Update: Show form with current data
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";  // New template
    }

    // Handle update form submission
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserRequestDTO userRequestDTO, Model model) {
        boolean updated = userService.updateUser(id, userRequestDTO);
        if (updated) {
            return "redirect:/users/" + id;  // Back to detail with success
        } else {
            model.addAttribute("error", "Update failed");
            return "editUser";  // Show form again with error
        }
    }

    // Delete: Show confirmation form
    @GetMapping("/users/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "deleteUser";  // New template
    }

    // Handle delete
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";  // Back to home
    }

    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userRequestDTO", new UserRequestDTO());  // Empty form
        return "createUser";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserRequestDTO userRequestDTO, RedirectAttributes redirectAttributes) {
        User newUser = userService.createUser(userRequestDTO);
        redirectAttributes.addFlashAttribute("success", "User created successfully!");
        return "redirect:/users/" + newUser.getId();  // Or "redirect:/" for home
    }


}
